<template>
  <Teleport to="body">
    <div v-if="visible" class="modal-overlay" @click.self="handleClose">
      <div class="modal-container">
        <div class="modal-header">
          <h3>{{ fileInfo?.fileName || '文件预览' }}</h3>
          <button class="close-btn" @click="handleClose">×</button>
        </div>
        <div class="modal-body">
          <div v-if="!loading">
            <!-- Word文档预览 -->
            <div v-if="isWordFile" class="preview-container">
              <div ref="wordPreviewRef" class="word-preview"></div>
            </div>
            
            <!-- Excel文档预览 -->
            <div v-else-if="isExcelFile" class="preview-container">
              <div class="excel-preview">
                <table v-if="excelData" class="excel-table">
                  <thead>
                    <tr>
                      <th v-for="(col, index) in excelData.headers" :key="index">{{ col }}</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(row, rowIndex) in excelData.rows" :key="rowIndex">
                      <td v-for="(cell, cellIndex) in row" :key="cellIndex">{{ cell }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            
            <!-- PDF文件预览 -->
            <div v-else-if="isPdfFile && fileInfo?.downloadUrl" class="preview-container">
              <iframe
                :src="`${window.location.origin}${fileInfo.downloadUrl}`"
                frameborder="0"
                width="100%"
                height="600px"
                class="preview-iframe"
              ></iframe>
            </div>
            
            <!-- 其他文件类型 -->
            <div v-else class="preview-container">
              <div class="no-preview">
                <p class="no-preview-icon">📄</p>
                <p>该文件类型不支持在线预览</p>
                <button class="download-btn" @click="downloadFile">下载文件</button>
              </div>
            </div>
            
            <!-- 文件属性面板 -->
            <div class="properties-panel">
              <h4>文件属性</h4>
              <div class="property-item">
                <span class="property-label">文件名:</span>
                <span class="property-value">{{ fileInfo?.fileName }}</span>
              </div>
              <div class="property-item">
                <span class="property-label">文件类型:</span>
                <span class="property-value">{{ fileInfo?.extension?.toUpperCase() }}</span>
              </div>
              <div class="property-item">
                <span class="property-label">文件大小:</span>
                <span class="property-value">{{ formatFileSize(currentFile?.fileSize) }}</span>
              </div>
              <div class="property-item">
                <span class="property-label">创建时间:</span>
                <span class="property-value">{{ formatDateTime(currentFile?.createdAt) }}</span>
              </div>
              <div class="property-item">
                <span class="property-label">创建人:</span>
                <span class="property-value">{{ currentFile?.createdBy || '-' }}</span>
              </div>
              <div class="property-item">
                <span class="property-label">文件版本:</span>
                <span class="property-value">{{ currentFile?.version || '1.0' }}</span>
              </div>
            </div>
          </div>
          <div v-else>
            <div class="loading-container">
              <div class="loading-spinner"></div>
              <p>加载中...</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, computed, watch, onMounted, nextTick } from 'vue'
import { storageAPI } from '../api'

const props = defineProps({
  visible: Boolean,
  fileId: String
})

const emit = defineEmits(['close', 'update:visible'])

const loading = ref(false)
const fileInfo = ref(null)
const currentFile = ref(null)
const wordPreviewRef = ref(null)
const excelData = ref(null)

const extension = computed(() => fileInfo.value?.extension || '')

const isWordFile = computed(() => {
  return ['doc', 'docx'].includes(extension.value)
})

const isExcelFile = computed(() => {
  return ['xls', 'xlsx'].includes(extension.value)
})

const isPdfFile = computed(() => extension.value === 'pdf')

const formatFileSize = (size) => {
  if (!size) return '-'
  if (size < 1024) return size + ' B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' KB'
  if (size < 1024 * 1024 * 1024) return (size / (1024 * 1024)).toFixed(1) + ' MB'
  return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB'
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const loadWordFile = async (url) => {
  try {
    const { default: renderAsync } = await import('docx-preview')
    const response = await fetch(url)
    const arrayBuffer = await response.arrayBuffer()
    await nextTick()
    renderAsync(arrayBuffer, wordPreviewRef.value, null, {
      className: 'docx-preview',
      style: { 'max-width': '100%' }
    })
  } catch (error) {
    console.error('加载Word文件失败:', error)
  }
}

const loadExcelFile = async (url) => {
  try {
    const { read, utils } = await import('xlsx')
    const response = await fetch(url)
    const arrayBuffer = await response.arrayBuffer()
    const workbook = read(arrayBuffer, { type: 'array' })
    const firstSheet = workbook.Sheets[workbook.SheetNames[0]]
    const jsonData = utils.sheet_to_json(firstSheet, { header: 1 })
    
    if (jsonData.length > 0) {
      excelData.value = {
        headers: jsonData[0],
        rows: jsonData.slice(1)
      }
    }
  } catch (error) {
    console.error('加载Excel文件失败:', error)
  }
}

const loadFileInfo = async () => {
  if (!props.fileId) return
  
  loading.value = true
  excelData.value = null
  
  try {
    const [infoRes, fileRes] = await Promise.all([
      storageAPI.getPreviewUrl(props.fileId),
      storageAPI.getFile(props.fileId)
    ])
    
    fileInfo.value = infoRes.data
    currentFile.value = fileRes.data
    
    // 根据文件类型加载预览
    if (fileInfo.value.downloadUrl) {
      const fileUrl = window.location.origin + fileInfo.value.downloadUrl
      
      if (isWordFile.value) {
        await loadWordFile(fileUrl)
      } else if (isExcelFile.value) {
        await loadExcelFile(fileUrl)
      }
    }
  } catch (error) {
    console.error('加载文件信息失败:', error)
  } finally {
    loading.value = false
  }
}

const downloadFile = () => {
  if (fileInfo.value?.downloadUrl) {
    const link = document.createElement('a')
    link.href = fileInfo.value.downloadUrl
    link.download = fileInfo.value.fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }
}

const handleClose = () => {
  emit('close')
  emit('update:visible', false)
}

onMounted(() => {})

watch(() => props.visible, (newVal) => {
  if (newVal) {
    loadFileInfo()
  }
})

watch(() => props.fileId, () => {
  if (props.visible) {
    loadFileInfo()
  }
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-container {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 1200px;
  max-height: 90vh;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #eee;
  background-color: #f8f9fa;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  line-height: 1;
}

.close-btn:hover {
  color: #333;
}

.modal-body {
  padding: 20px;
  max-height: calc(90vh - 80px);
  overflow-y: auto;
}

.preview-container {
  margin-bottom: 20px;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
  background-color: white;
}

.preview-iframe {
  border: none;
}

.word-preview {
  padding: 20px;
  min-height: 400px;
}

.word-preview img {
  max-width: 100%;
  height: auto;
}

.excel-preview {
  max-height: 500px;
  overflow: auto;
  padding: 10px;
}

.excel-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.excel-table th,
.excel-table td {
  border: 1px solid #ddd;
  padding: 8px 12px;
  text-align: left;
}

.excel-table th {
  background-color: #f5f7fa;
  font-weight: 600;
}

.excel-table tr:nth-child(even) {
  background-color: #fafafa;
}

.no-preview {
  text-align: center;
  padding: 40px;
  color: #999;
}

.no-preview-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.properties-panel {
  background-color: #fafafa;
  border-radius: 4px;
  padding: 16px;
}

.properties-panel h4 {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #333;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
}

.property-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px dashed #eee;
}

.property-label {
  font-size: 13px;
  color: #666;
}

.property-value {
  font-size: 13px;
  color: #333;
  text-align: right;
  max-width: 60%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.download-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-top: 10px;
}

.download-btn:hover {
  background-color: #66b1ff;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px;
  gap: 16px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
