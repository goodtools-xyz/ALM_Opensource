<template>
  <div class="testcase-container">
    <div class="page-header">
      <h2>测试用例管理</h2>
      <div class="header-actions">
        <el-input v-model="searchKeyword" placeholder="搜索测试用例" clearable class="search-input">
          <template #append>
            <el-button @click="loadTestCases">搜索</el-button>
          </template>
        </el-input>
        <el-button type="primary" @click="showCreateModal = true">新建测试用例</el-button>
      </div>
    </div>

    <el-card shadow="hover">
      <el-table :data="testCases" stripe>
        <el-table-column prop="caseId" label="用例ID" width="120" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeColor(row.type)" size="small">{{ getTypeName(row.type) }}</el-tag>
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
        <el-table-column prop="relatedReqId" label="关联需求" width="120" />
        <el-table-column prop="relatedDesignId" label="关联设计" width="120" />
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="testEngineer" label="测试人员" width="120" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewTestCase(row)">查看</el-button>
            <el-button type="success" link @click="editTestCase(row)">编辑</el-button>
            <el-button type="danger" link @click="deleteTestCase(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showCreateModal" :title="editingItem ? '编辑测试用例' : '新建测试用例'" width="700px">
      <el-form :model="testCaseForm" label-width="120px">
        <el-form-item label="用例ID">
          <el-input v-model="testCaseForm.caseId" :disabled="!!editingItem" />
        </el-form-item>
        <el-form-item label="标题" required>
          <el-input v-model="testCaseForm.title" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="testCaseForm.type">
            <el-option label="功能测试" value="FUNCTIONAL" />
            <el-option label="性能测试" value="PERFORMANCE" />
            <el-option label="回归测试" value="REGRESSION" />
            <el-option label="安全测试" value="SECURITY" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="testCaseForm.priority">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="testCaseForm.status">
            <el-option label="待执行" value="PENDING" />
            <el-option label="通过" value="PASS" />
            <el-option label="失败" value="FAIL" />
            <el-option label="阻塞" value="BLOCKED" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联需求">
          <el-input v-model="testCaseForm.relatedReqId" placeholder="如 REQ-001" />
        </el-form-item>
        <el-form-item label="关联设计">
          <el-input v-model="testCaseForm.relatedDesignId" placeholder="如 DES-001" />
        </el-form-item>
        <el-form-item label="模块">
          <el-input v-model="testCaseForm.module" />
        </el-form-item>
        <el-form-item label="测试人员">
          <el-input v-model="testCaseForm.testEngineer" />
        </el-form-item>
        <el-form-item label="前置条件">
          <el-textarea v-model="testCaseForm.preconditions" rows="3" />
        </el-form-item>
        <el-form-item label="测试步骤">
          <el-textarea v-model="testCaseForm.steps" rows="4" placeholder="每步一行" />
        </el-form-item>
        <el-form-item label="预期结果">
          <el-textarea v-model="testCaseForm.expectedResult" rows="3" />
        </el-form-item>
        <el-form-item label="描述">
          <el-textarea v-model="testCaseForm.description" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateModal = false">取消</el-button>
        <el-button type="primary" @click="saveTestCase">{{ editingItem ? '保存修改' : '创建用例' }}</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showDetailModal" title="测试用例详情" width="700px">
      <el-descriptions :column="2" border v-if="selectedTestCase">
        <el-descriptions-item label="用例ID">{{ selectedTestCase.caseId }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ selectedTestCase.title }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ getTypeName(selectedTestCase.type) }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ selectedTestCase.priority }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusName(selectedTestCase.status) }}</el-descriptions-item>
        <el-descriptions-item label="关联需求">{{ selectedTestCase.relatedReqId }}</el-descriptions-item>
        <el-descriptions-item label="关联设计">{{ selectedTestCase.relatedDesignId }}</el-descriptions-item>
        <el-descriptions-item label="模块">{{ selectedTestCase.module }}</el-descriptions-item>
        <el-descriptions-item label="测试人员">{{ selectedTestCase.testEngineer }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedTestCase.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="前置条件" :span="2">{{ selectedTestCase.preconditions }}</el-descriptions-item>
        <el-descriptions-item label="测试步骤" :span="2">{{ selectedTestCase.steps }}</el-descriptions-item>
        <el-descriptions-item label="预期结果" :span="2">{{ selectedTestCase.expectedResult }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ selectedTestCase.description }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="showDetailModal = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { testCaseAPI } from '../api'
import { ElMessage } from 'element-plus'

const searchKeyword = ref('')
const testCases = ref([])
const showCreateModal = ref(false)
const showDetailModal = ref(false)
const editingItem = ref(null)
const selectedTestCase = ref(null)

const testCaseForm = reactive({
  caseId: '',
  title: '',
  description: '',
  preconditions: '',
  steps: '',
  expectedResult: '',
  type: 'FUNCTIONAL',
  priority: 'MEDIUM',
  status: 'PENDING',
  relatedReqId: '',
  relatedDesignId: '',
  module: '',
  testEngineer: ''
})

const loadTestCases = async () => {
  try {
    const params = {}
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    const response = await testCaseAPI.getAll(params)
    testCases.value = response.data || []
  } catch (error) {
    console.error('加载测试用例失败:', error)
    ElMessage.error('加载测试用例失败')
  }
}

const createTestCase = async () => {
  try {
    await testCaseAPI.create(testCaseForm)
    ElMessage.success('创建测试用例成功')
    resetForm()
    showCreateModal.value = false
    loadTestCases()
  } catch (error) {
    console.error('创建测试用例失败:', error)
    ElMessage.error('创建测试用例失败')
  }
}

const updateTestCase = async () => {
  try {
    await testCaseAPI.update(editingItem.value.id, testCaseForm)
    ElMessage.success('更新测试用例成功')
    resetForm()
    showCreateModal.value = false
    loadTestCases()
  } catch (error) {
    console.error('更新测试用例失败:', error)
    ElMessage.error('更新测试用例失败')
  }
}

const saveTestCase = () => {
  if (!testCaseForm.title) {
    ElMessage.warning('请输入测试用例标题')
    return
  }
  if (editingItem.value) {
    updateTestCase()
  } else {
    createTestCase()
  }
}

const deleteTestCase = async (item) => {
  try {
    await testCaseAPI.delete(item.id)
    ElMessage.success('删除测试用例成功')
    loadTestCases()
  } catch (error) {
    console.error('删除测试用例失败:', error)
    ElMessage.error('删除测试用例失败')
  }
}

const viewTestCase = (item) => {
  selectedTestCase.value = item
  showDetailModal.value = true
}

const editTestCase = (item) => {
  editingItem.value = item
  Object.assign(testCaseForm, item)
  showCreateModal.value = true
}

const resetForm = () => {
  editingItem.value = null
  Object.keys(testCaseForm).forEach(key => {
    testCaseForm[key] = ''
  })
  testCaseForm.type = 'FUNCTIONAL'
  testCaseForm.priority = 'MEDIUM'
  testCaseForm.status = 'PENDING'
}

const getTypeColor = (type) => {
  const colors = { FUNCTIONAL: 'primary', PERFORMANCE: 'success', REGRESSION: 'warning', SECURITY: 'danger' }
  return colors[type] || 'info'
}

const getTypeName = (type) => {
  const names = { FUNCTIONAL: '功能测试', PERFORMANCE: '性能测试', REGRESSION: '回归测试', SECURITY: '安全测试' }
  return names[type] || type
}

const getPriorityColor = (priority) => {
  const colors = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
  return colors[priority] || 'info'
}

const getStatusColor = (status) => {
  const colors = { PENDING: 'info', PASS: 'success', FAIL: 'danger', BLOCKED: 'warning' }
  return colors[status] || 'info'
}

const getStatusName = (status) => {
  const names = { PENDING: '待执行', PASS: '通过', FAIL: '失败', BLOCKED: '阻塞' }
  return names[status] || status
}

onMounted(() => {
  loadTestCases()
})
</script>

<style scoped>
.testcase-container {
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