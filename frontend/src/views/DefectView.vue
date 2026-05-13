<template>
  <div class="defect-container">
    <div class="page-header">
      <h2>缺陷管理</h2>
      <div class="header-actions">
        <el-input v-model="searchKeyword" placeholder="搜索缺陷" clearable class="search-input">
          <template #append>
            <el-button @click="loadDefects">搜索</el-button>
          </template>
        </el-input>
        <el-button type="primary" @click="showCreateModal = true">新建缺陷</el-button>
      </div>
    </div>

    <el-card shadow="hover">
      <el-table :data="defects" stripe>
        <el-table-column prop="defectId" label="缺陷ID" width="120" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="severity" label="严重程度" width="100">
          <template #default="{ row }">
            <el-tag :type="getSeverityColor(row.severity)" size="small">{{ getSeverityName(row.severity) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="getPriorityColor(row.priority)" size="small">{{ row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusColor(row.status)" size="small">{{ getStatusName(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reporter" label="报告人" width="100" />
        <el-table-column prop="assignee" label="经办人" width="100" />
        <el-table-column prop="relatedReqId" label="关联需求" width="120" />
        <el-table-column prop="relatedTestCaseId" label="关联用例" width="120" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDefect(row)">查看</el-button>
            <el-button type="success" link @click="editDefect(row)">编辑</el-button>
            <el-button v-if="row.status !== 'RESOLVED'" type="warning" link @click="resolveDefect(row)">解决</el-button>
            <el-button type="danger" link @click="deleteDefect(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showCreateModal" :title="editingItem ? '编辑缺陷' : '新建缺陷'" width="700px">
      <el-form :model="defectForm" label-width="120px">
        <el-form-item label="缺陷ID">
          <el-input v-model="defectForm.defectId" :disabled="!!editingItem" />
        </el-form-item>
        <el-form-item label="标题" required>
          <el-input v-model="defectForm.title" />
        </el-form-item>
        <el-form-item label="严重程度">
          <el-select v-model="defectForm.severity">
            <el-option label="致命" value="CRITICAL" />
            <el-option label="严重" value="SEVERE" />
            <el-option label="一般" value="NORMAL" />
            <el-option label="轻微" value="MINOR" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="defectForm.priority">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="defectForm.status">
            <el-option label="新建" value="NEW" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="处理中" value="IN_PROGRESS" />
            <el-option label="已解决" value="RESOLVED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="defectForm.type">
            <el-option label="代码缺陷" value="CODE" />
            <el-option label="逻辑错误" value="LOGIC" />
            <el-option label="界面问题" value="UI" />
            <el-option label="性能问题" value="PERFORMANCE" />
            <el-option label="安全漏洞" value="SECURITY" />
          </el-select>
        </el-form-item>
        <el-form-item label="报告人">
          <el-input v-model="defectForm.reporter" />
        </el-form-item>
        <el-form-item label="经办人">
          <el-input v-model="defectForm.assignee" />
        </el-form-item>
        <el-form-item label="关联需求">
          <el-input v-model="defectForm.relatedReqId" placeholder="如 REQ-001" />
        </el-form-item>
        <el-form-item label="关联测试用例">
          <el-input v-model="defectForm.relatedTestCaseId" placeholder="如 TEST-001" />
        </el-form-item>
        <el-form-item label="模块">
          <el-input v-model="defectForm.module" />
        </el-form-item>
        <el-form-item label="复现步骤">
          <el-textarea v-model="defectForm.reproduceSteps" rows="4" placeholder="每步一行" />
        </el-form-item>
        <el-form-item label="描述">
          <el-textarea v-model="defectForm.description" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateModal = false">取消</el-button>
        <el-button type="primary" @click="saveDefect">{{ editingItem ? '保存修改' : '创建缺陷' }}</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showDetailModal" title="缺陷详情" width="700px">
      <el-descriptions :column="2" border v-if="selectedDefect">
        <el-descriptions-item label="缺陷ID">{{ selectedDefect.defectId }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ selectedDefect.title }}</el-descriptions-item>
        <el-descriptions-item label="严重程度">{{ getSeverityName(selectedDefect.severity) }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ selectedDefect.priority }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusName(selectedDefect.status) }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ getTypeName(selectedDefect.type) }}</el-descriptions-item>
        <el-descriptions-item label="报告人">{{ selectedDefect.reporter }}</el-descriptions-item>
        <el-descriptions-item label="经办人">{{ selectedDefect.assignee }}</el-descriptions-item>
        <el-descriptions-item label="关联需求">{{ selectedDefect.relatedReqId }}</el-descriptions-item>
        <el-descriptions-item label="关联用例">{{ selectedDefect.relatedTestCaseId }}</el-descriptions-item>
        <el-descriptions-item label="模块">{{ selectedDefect.module }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedDefect.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="复现步骤" :span="2">{{ selectedDefect.reproduceSteps }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ selectedDefect.description }}</el-descriptions-item>
        <el-descriptions-item v-if="selectedDefect.resolvedBy" label="解决人" :span="2">{{ selectedDefect.resolvedBy }}</el-descriptions-item>
        <el-descriptions-item v-if="selectedDefect.resolvedAt" label="解决时间" :span="2">{{ selectedDefect.resolvedAt }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="showDetailModal = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { defectAPI } from '../api'
import { ElMessage } from 'element-plus'

const searchKeyword = ref('')
const defects = ref([])
const showCreateModal = ref(false)
const showDetailModal = ref(false)
const editingItem = ref(null)
const selectedDefect = ref(null)

const defectForm = reactive({
  defectId: '',
  title: '',
  description: '',
  severity: 'NORMAL',
  priority: 'MEDIUM',
  status: 'NEW',
  type: 'CODE',
  reproduceSteps: '',
  relatedReqId: '',
  relatedTestCaseId: '',
  module: '',
  reporter: '',
  assignee: ''
})

const loadDefects = async () => {
  try {
    const params = {}
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    const response = await defectAPI.getAll(params)
    defects.value = response.data || []
  } catch (error) {
    console.error('加载缺陷失败:', error)
    ElMessage.error('加载缺陷失败')
  }
}

const createDefect = async () => {
  try {
    await defectAPI.create(defectForm)
    ElMessage.success('创建缺陷成功')
    resetForm()
    showCreateModal.value = false
    loadDefects()
  } catch (error) {
    console.error('创建缺陷失败:', error)
    ElMessage.error('创建缺陷失败')
  }
}

const updateDefect = async () => {
  try {
    await defectAPI.update(editingItem.value.id, defectForm)
    ElMessage.success('更新缺陷成功')
    resetForm()
    showCreateModal.value = false
    loadDefects()
  } catch (error) {
    console.error('更新缺陷失败:', error)
    ElMessage.error('更新缺陷失败')
  }
}

const saveDefect = () => {
  if (!defectForm.title) {
    ElMessage.warning('请输入缺陷标题')
    return
  }
  if (editingItem.value) {
    updateDefect()
  } else {
    createDefect()
  }
}

const deleteDefect = async (item) => {
  try {
    await defectAPI.delete(item.id)
    ElMessage.success('删除缺陷成功')
    loadDefects()
  } catch (error) {
    console.error('删除缺陷失败:', error)
    ElMessage.error('删除缺陷失败')
  }
}

const resolveDefect = async (item) => {
  try {
    await defectAPI.resolve(item.id, 'admin')
    ElMessage.success('缺陷已解决')
    loadDefects()
  } catch (error) {
    console.error('解决缺陷失败:', error)
    ElMessage.error('解决缺陷失败')
  }
}

const viewDefect = (item) => {
  selectedDefect.value = item
  showDetailModal.value = true
}

const editDefect = (item) => {
  editingItem.value = item
  Object.assign(defectForm, item)
  showCreateModal.value = true
}

const resetForm = () => {
  editingItem.value = null
  Object.keys(defectForm).forEach(key => {
    defectForm[key] = ''
  })
  defectForm.severity = 'NORMAL'
  defectForm.priority = 'MEDIUM'
  defectForm.status = 'NEW'
  defectForm.type = 'CODE'
}

const getSeverityColor = (severity) => {
  const colors = { CRITICAL: 'danger', SEVERE: 'danger', NORMAL: 'warning', MINOR: 'info' }
  return colors[severity] || 'info'
}

const getSeverityName = (severity) => {
  const names = { CRITICAL: '致命', SEVERE: '严重', NORMAL: '一般', MINOR: '轻微' }
  return names[severity] || severity
}

const getPriorityColor = (priority) => {
  const colors = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
  return colors[priority] || 'info'
}

const getStatusColor = (status) => {
  const colors = { NEW: 'info', CONFIRMED: 'warning', IN_PROGRESS: 'primary', RESOLVED: 'success', CLOSED: 'success' }
  return colors[status] || 'info'
}

const getStatusName = (status) => {
  const names = { NEW: '新建', CONFIRMED: '已确认', IN_PROGRESS: '处理中', RESOLVED: '已解决', CLOSED: '已关闭' }
  return names[status] || status
}

const getTypeName = (type) => {
  const names = { CODE: '代码缺陷', LOGIC: '逻辑错误', UI: '界面问题', PERFORMANCE: '性能问题', SECURITY: '安全漏洞' }
  return names[type] || type
}

onMounted(() => {
  loadDefects()
})
</script>

<style scoped>
.defect-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-input {
  width: 300px;
}
</style>