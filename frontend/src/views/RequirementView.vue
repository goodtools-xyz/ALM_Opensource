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
        <el-button type="info" @click="showImportHistory">导入历史</el-button>
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

    <el-dialog v-model="showDocumentPreviewModal" title="文档预览" width="900px" :fullscreen="true">
      <div v-if="documentPreviewData" class="document-preview-container">
        <div class="preview-tabs">
          <el-tabs v-model="previewActiveTab" type="border-card">
            <el-tab-pane label="原始文档" name="document">
              <div class="document-content">
                <div v-html="documentPreviewData.documentContent"></div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="条目化需求" name="requirements">
              <div v-if="documentPreviewData.requirements && documentPreviewData.requirements.length > 0">
                <el-table :data="documentPreviewData.requirements" stripe>
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
                  <el-table-column prop="description" label="描述" show-overflow-tooltip />
                </el-table>
              </div>
              <div v-else>
                <p style="text-align: center; color: #999; padding: 40px;">暂无条目化需求数据</p>
              </div>
            </el-tab-pane>
            <el-tab-pane label="文档信息" name="info">
              <el-descriptions :column="2" border v-if="documentPreviewData.importRecord">
                <el-descriptions-item label="导入记录ID">{{ documentPreviewData.importRecord.importId }}</el-descriptions-item>
                <el-descriptions-item label="文件名">{{ documentPreviewData.importRecord.fileName }}</el-descriptions-item>
                <el-descriptions-item label="文件类型">{{ documentPreviewData.importRecord.fileType }}</el-descriptions-item>
                <el-descriptions-item label="文件大小">{{ formatFileSize(documentPreviewData.importRecord.fileSize) }}</el-descriptions-item>
                <el-descriptions-item label="导入总数">{{ documentPreviewData.importRecord.importCount }}</el-descriptions-item>
                <el-descriptions-item label="成功数">{{ documentPreviewData.importRecord.successCount }}</el-descriptions-item>
                <el-descriptions-item label="失败数">{{ documentPreviewData.importRecord.failCount }}</el-descriptions-item>
                <el-descriptions-item label="导入状态">{{ getImportStatusName(documentPreviewData.importRecord.status) }}</el-descriptions-item>
                <el-descriptions-item label="导入时间">{{ documentPreviewData.importRecord.createdAt }}</el-descriptions-item>
              </el-descriptions>
            </el-tab-pane>
          </el-tabs>
        </div>
        <div class="preview-actions">
          <el-button type="primary" @click="downloadOriginalDocument">
            <el-icon class="el-icon-download"></el-icon>
            下载原始文档
          </el-button>
          <el-button @click="showDocumentPreviewModal = false">关闭预览</el-button>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="showImportHistoryModal" title="导入历史记录" width="800px">
      <div class="import-history-container">
        <el-table :data="importRecords" stripe>
          <el-table-column prop="importId" label="导入ID" width="120" />
          <el-table-column prop="fileName" label="文件名" />
          <el-table-column prop="fileType" label="文件类型" width="120" />
          <el-table-column prop="fileSize" label="文件大小" width="100">
            <template #default="{ row }">{{ formatFileSize(row.fileSize) }}</template>
          </el-table-column>
          <el-table-column prop="importCount" label="总条数" width="80" />
          <el-table-column prop="successCount" label="成功" width="80" />
          <el-table-column prop="failCount" label="失败" width="80" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getImportStatusColor(row.status)" size="small">{{ getImportStatusName(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="导入时间" width="180" />
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="previewDocumentByImportId(row.importId)">预览</el-button>
              <el-button type="success" size="small" @click="viewRequirementsByImportId(row.importId)">查看需求</el-button>
              <el-button type="info" size="small" @click="downloadDocumentByImportId(row.importId)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="showImportHistoryModal = false">关闭</el-button>
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
const showDocumentPreviewModal = ref(false)
const showImportHistoryModal = ref(false)
const editingItem = ref(null)
const selectedRequirement = ref(null)
const importFileList = ref([])
const importPreviewData = ref([])
const currentImportFile = ref(null)
const importRecords = ref([])
const documentPreviewData = ref(null)
const previewActiveTab = ref('document')
const currentPreviewImportId = ref(null)

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

const showImportHistory = async () => {
  await loadImportRecords()
  showImportHistoryModal.value = true
}

const loadImportRecords = async () => {
  try {
    const response = await requirementAPI.getImportRecords()
    if (response.data.success) {
      importRecords.value = response.data.data || []
    }
  } catch (error) {
    console.error('加载导入记录失败:', error)
    ElMessage.error('加载导入记录失败')
  }
}

const previewDocumentByImportId = async (importId) => {
  try {
    const response = await requirementAPI.previewDocument(importId)
    if (response.data.success) {
      documentPreviewData.value = response.data
      currentPreviewImportId.value = importId
      previewActiveTab.value = 'document'
      showDocumentPreviewModal.value = true
    } else {
      ElMessage.error(response.data.message || '预览失败')
    }
  } catch (error) {
    console.error('预览文档失败:', error)
    ElMessage.error('预览文档失败: ' + (error.response?.data?.message || error.message))
  }
}

const viewRequirementsByImportId = async (importId) => {
  try {
    const response = await requirementAPI.getRequirementsByImportId(importId)
    if (response.data.success) {
      documentPreviewData.value = response.data
      currentPreviewImportId.value = importId
      previewActiveTab.value = 'requirements'
      showDocumentPreviewModal.value = true
    } else {
      ElMessage.error(response.data.message || '获取需求失败')
    }
  } catch (error) {
    console.error('获取需求失败:', error)
    ElMessage.error('获取需求失败')
  }
}

const downloadDocumentByImportId = (importId) => {
  const url = requirementAPI.downloadDocument(importId)
  window.open(url, '_blank')
}

const downloadOriginalDocument = () => {
  if (currentPreviewImportId.value) {
    downloadDocumentByImportId(currentPreviewImportId.value)
  }
}

const formatFileSize = (size) => {
  if (!size) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  let unitIndex = 0
  let fileSize = size
  while (fileSize >= 1024 && unitIndex < units.length - 1) {
    fileSize /= 1024
    unitIndex++
  }
  return fileSize.toFixed(2) + ' ' + units[unitIndex]
}

const getImportStatusColor = (status) => {
  const colors = { COMPLETED: 'success', PARTIAL: 'warning', FAILED: 'danger' }
  return colors[status] || 'info'
}

const getImportStatusName = (status) => {
  const names = { COMPLETED: '已完成', PARTIAL: '部分完成', FAILED: '失败' }
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

.document-preview-container {
  height: calc(100vh - 180px);
  display: flex;
  flex-direction: column;
}

.preview-tabs {
  flex: 1;
  overflow: auto;
}

.document-content {
  padding: 20px;
  background: white;
  min-height: 100%;
}

.document-content h1, .document-content h2, .document-content h3,
.document-content h4, .document-content h5, .document-content h6 {
  margin-top: 1em;
  margin-bottom: 0.5em;
  font-weight: bold;
}

.document-content h1 { font-size: 24px; }
.document-content h2 { font-size: 20px; }
.document-content h3 { font-size: 18px; }
.document-content h4 { font-size: 16px; }
.document-content h5 { font-size: 14px; }
.document-content h6 { font-size: 13px; }

.document-content p {
  margin: 0.5em 0;
  line-height: 1.6;
}

.document-content table {
  width: 100%;
  margin: 0.5em 0;
}

.document-content table td, .document-content table th {
  border: 1px solid #ddd;
  padding: 8px;
}

.preview-actions {
  padding: 15px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.import-history-container {
  max-height: 500px;
  overflow: auto;
}
</style>