<template>
  <div class="requirement-container">
    <div class="page-header">
      <h2>需求管理</h2>
      <div class="header-actions">
        <el-input v-model="searchKeyword" placeholder="搜索需求" clearable class="search-input">
          <template #append>
            <el-button @click="loadRequirements">搜索</el-button>
          </template>
        </el-input>
        <el-button type="primary" @click="showCreateModal = true">新建需求</el-button>
        <el-button type="success" @click="showImportModal = true">导入文档</el-button>
      </div>
    </div>

    <el-card shadow="hover">
      <el-table :data="requirements" stripe>
        <el-table-column prop="reqId" label="需求ID" width="120" />
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
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="owner" label="负责人" width="100" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewRequirement(row)">查看</el-button>
            <el-button type="success" link @click="editRequirement(row)">编辑</el-button>
            <el-button type="danger" link @click="deleteRequirement(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showCreateModal" :title="editingItem ? '编辑需求' : '新建需求'" width="600px">
      <el-form :model="requirementForm" label-width="100px">
        <el-form-item label="需求ID">
          <el-input v-model="requirementForm.reqId" :disabled="!!editingItem" />
        </el-form-item>
        <el-form-item label="标题" required>
          <el-input v-model="requirementForm.title" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="requirementForm.type">
            <el-option label="功能需求" value="FUNCTIONAL" />
            <el-option label="非功能需求" value="NON_FUNCTIONAL" />
            <el-option label="接口需求" value="INTERFACE" />
            <el-option label="数据需求" value="DATA" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="requirementForm.priority">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="requirementForm.status">
            <el-option label="待评审" value="PENDING" />
            <el-option label="已批准" value="APPROVED" />
            <el-option label="已实现" value="IMPLEMENTED" />
            <el-option label="已验证" value="VERIFIED" />
          </el-select>
        </el-form-item>
        <el-form-item label="模块">
          <el-input v-model="requirementForm.module" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="requirementForm.owner" />
        </el-form-item>
        <el-form-item label="来源">
          <el-input v-model="requirementForm.source" />
        </el-form-item>
        <el-form-item label="版本">
          <el-input v-model="requirementForm.version" />
        </el-form-item>
        <el-form-item label="描述">
          <el-textarea v-model="requirementForm.description" rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateModal = false">取消</el-button>
        <el-button type="primary" @click="saveRequirement">{{ editingItem ? '保存修改' : '创建需求' }}</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showDetailModal" title="需求详情" width="600px">
      <el-descriptions :column="2" border v-if="selectedRequirement">
        <el-descriptions-item label="需求ID">{{ selectedRequirement.reqId }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ selectedRequirement.title }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ getTypeName(selectedRequirement.type) }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ selectedRequirement.priority }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusName(selectedRequirement.status) }}</el-descriptions-item>
        <el-descriptions-item label="模块">{{ selectedRequirement.module }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ selectedRequirement.owner }}</el-descriptions-item>
        <el-descriptions-item label="来源">{{ selectedRequirement.source }}</el-descriptions-item>
        <el-descriptions-item label="版本">{{ selectedRequirement.version }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedRequirement.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ selectedRequirement.description }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="showDetailModal = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showImportModal" title="导入文档" width="800px">
      <div class="import-container">
        <div class="upload-area">
          <el-upload
            class="upload-demo"
            drag
            :auto-upload="false"
            :accept="'.docx,.xlsx'"
            :on-change="handleFileChange"
            :file-list="importFileList"
            :limit="1"
          >
            <el-icon class="el-icon-upload" style="font-size: 48px; color: #ccc;"></el-icon>
            <div class="el-upload__text">
              拖拽文件到此处上传，或<em>点击选择文件</em>
            </div>
            <div class="el-upload__tip" slot="tip">
              仅支持 .docx（Word）和 .xlsx（Excel）格式文件
            </div>
          </el-upload>
        </div>

        <div v-if="importPreviewData.length > 0" class="preview-area">
          <h4>解析结果预览（共 {{ importPreviewData.length }} 条需求）</h4>
          <el-table :data="importPreviewData" stripe size="small" max-height="300">
            <el-table-column prop="reqId" label="需求ID" width="120" />
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
            <el-table-column prop="description" label="描述" width="200" show-overflow-tooltip />
          </el-table>
        </div>
      </div>
      <template #footer>
        <el-button @click="closeImportModal">取消</el-button>
        <el-button type="primary" :disabled="importFileList.length === 0" @click="parseDocument">
          解析文档
          </el-button>
        <el-button type="success" :disabled="importPreviewData.length === 0" @click="importDocument">
          导入并保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { requirementAPI } from '../api'
import { ElMessage } from 'element-plus'

const searchKeyword = ref('')
const requirements = ref([])
const showCreateModal = ref(false)
const showDetailModal = ref(false)
const showImportModal = ref(false)
const editingItem = ref(null)
const selectedRequirement = ref(null)
const importFileList = ref([])
const importPreviewData = ref([])
const currentImportFile = ref(null)

const requirementForm = reactive({
  reqId: '',
  title: '',
  description: '',
  priority: 'MEDIUM',
  status: 'PENDING',
  type: 'FUNCTIONAL',
  source: '',
  version: '1.0',
  module: '',
  owner: ''
})

const loadRequirements = async () => {
  try {
    const params = {}
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    const response = await requirementAPI.getAll(params)
    requirements.value = response.data || []
  } catch (error) {
    console.error('加载需求失败:', error)
    ElMessage.error('加载需求失败')
  }
}

const createRequirement = async () => {
  try {
    await requirementAPI.create(requirementForm)
    ElMessage.success('创建需求成功')
    resetForm()
    showCreateModal.value = false
    loadRequirements()
  } catch (error) {
    console.error('创建需求失败:', error)
    ElMessage.error('创建需求失败')
  }
}

const updateRequirement = async () => {
  try {
    await requirementAPI.update(editingItem.value.id, requirementForm)
    ElMessage.success('更新需求成功')
    resetForm()
    showCreateModal.value = false
    loadRequirements()
  } catch (error) {
    console.error('更新需求失败:', error)
    ElMessage.error('更新需求失败')
  }
}

const saveRequirement = () => {
  if (!requirementForm.title) {
    ElMessage.warning('请输入需求标题')
    return
  }
  if (editingItem.value) {
    updateRequirement()
  } else {
    createRequirement()
  }
}

const deleteRequirement = async (item) => {
  try {
    await requirementAPI.delete(item.id)
    ElMessage.success('删除需求成功')
    loadRequirements()
  } catch (error) {
    console.error('删除需求失败:', error)
    ElMessage.error('删除需求失败')
  }
}

const viewRequirement = (item) => {
  selectedRequirement.value = item
  showDetailModal.value = true
}

const editRequirement = (item) => {
  editingItem.value = item
  Object.assign(requirementForm, item)
  showCreateModal.value = true
}

const resetForm = () => {
  editingItem.value = null
  Object.keys(requirementForm).forEach(key => {
    requirementForm[key] = ''
  })
  requirementForm.priority = 'MEDIUM'
  requirementForm.status = 'PENDING'
  requirementForm.type = 'FUNCTIONAL'
  requirementForm.version = '1.0'
}

const handleFileChange = (file) => {
  importFileList.value = [file]
  currentImportFile.value = file.raw
  importPreviewData.value = []
}

const closeImportModal = () => {
  showImportModal.value = false
  importFileList.value = []
  importPreviewData.value = []
  currentImportFile.value = null
}

const parseDocument = async () => {
  if (!currentImportFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }
  
  try {
    const response = await requirementAPI.importPreview(currentImportFile.value)
    if (response.data.success) {
      importPreviewData.value = response.data.data || []
      ElMessage.success(`解析成功，共识别 ${response.data.count} 条需求`)
    } else {
      ElMessage.error('解析失败')
    }
  } catch (error) {
    console.error('解析文档失败:', error)
    ElMessage.error('解析文档失败: ' + (error.response?.data?.message || error.message))
  }
}

const importDocument = async () => {
  if (!currentImportFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }
  
  try {
    const response = await requirementAPI.importSave(currentImportFile.value)
    if (response.data.success) {
      ElMessage.success(
        `导入完成！共 ${response.data.total} 条，成功 ${response.data.successCount} 条，失败 ${response.data.failCount} 条`
      )
      closeImportModal()
      loadRequirements()
    } else {
      ElMessage.error('导入失败')
    }
  } catch (error) {
    console.error('导入文档失败:', error)
    ElMessage.error('导入文档失败: ' + (error.response?.data?.message || error.message))
  }
}

const getTypeColor = (type) => {
  const colors = { FUNCTIONAL: 'primary', NON_FUNCTIONAL: 'success', INTERFACE: 'warning', DATA: 'info' }
  return colors[type] || 'info'
}

const getTypeName = (type) => {
  const names = { FUNCTIONAL: '功能需求', NON_FUNCTIONAL: '非功能需求', INTERFACE: '接口需求', DATA: '数据需求' }
  return names[type] || type
}

const getPriorityColor = (priority) => {
  const colors = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
  return colors[priority] || 'info'
}

const getStatusColor = (status) => {
  const colors = { PENDING: 'info', APPROVED: 'success', IMPLEMENTED: 'primary', VERIFIED: 'success' }
  return colors[status] || 'info'
}

const getStatusName = (status) => {
  const names = { PENDING: '待评审', APPROVED: '已批准', IMPLEMENTED: '已实现', VERIFIED: '已验证' }
  return names[status] || status
}

onMounted(() => {
  loadRequirements()
})
</script>

<style scoped>
.requirement-container {
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

.import-container {
  padding: 10px;
}

.upload-area {
  margin-bottom: 20px;
}

.preview-area {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.preview-area h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: bold;
}
</style>