<template>
  <el-dialog
    :title="fileInfo?.fileName || '文件预览'"
    :visible.sync="visible"
    width="90%"
    :fullscreen="isFullscreen"
    @close="handleClose"
  >
    <div v-if="!loading" class="preview-container">
      <div class="preview-header">
        <div class="file-info">
          <el-icon :size="24"><Files /></el-icon>
          <span class="file-name">{{ fileInfo?.fileName }}</span>
        </div>
        <div class="preview-actions">
          <el-button size="small" @click="toggleFullscreen">
            <el-icon><ZoomOut v-if="isFullscreen" /><ZoomIn v-else /></el-icon>
          </el-button>
          <el-button size="small" type="primary" @click="downloadFile">
            <el-icon><Download /></el-icon>
            下载
          </el-button>
        </div>
      </div>

      <div class="preview-body">
        <div class="properties-panel" :class="{ 'collapsed': !showProperties }">
          <div class="panel-header" @click="showProperties = !showProperties">
            <span class="panel-title">文件属性</span>
            <el-icon><ArrowUp v-if="showProperties" /><ArrowDown v-else /></el-icon>
          </div>
          <div v-if="showProperties" class="properties-content">
            <div class="property-item">
              <span class="property-label">文件名称</span>
              <span class="property-value">{{ fileInfo?.fileName }}</span>
            </div>
            <div class="property-item">
              <span class="property-label">文件类型</span>
              <span class="property-value">{{ fileTypeText }}</span>
            </div>
            <div class="property-item">
              <span class="property-label">文件大小</span>
              <span class="property-value">{{ formatFileSize(currentFile?.fileSize) }}</span>
            </div>
            <div class="property-item">
              <span class="property-label">创建时间</span>
              <span class="property-value">{{ formatDateTime(currentFile?.createdAt) }}</span>
            </div>
            <div class="property-item">
              <span class="property-label">创建人</span>
              <span class="property-value">{{ currentFile?.createdBy || '-' }}</span>
            </div>
            <div class="property-item">
              <span class="property-label">文件版本</span>
              <span class="property-value">{{ currentFile?.version || '1.0' }}</span>
            </div>
            <div class="property-item">
              <span class="property-label">受控状态</span>
              <span class="property-value">
                <el-tag :type="currentFile?.controlled ? 'success' : 'info'">
                  {{ currentFile?.controlled ? '受控' : '非受控' }}
                </el-tag>
              </span>
            </div>
            <div class="property-item">
              <span class="property-label">归档状态</span>
              <span class="property-value">
                <el-tag :type="currentFile?.archived ? 'warning' : 'success'">
                  {{ currentFile?.archived ? '已归档' : '正常' }}
                </el-tag>
              </span>
            </div>
          </div>
        </div>

        <div class="preview-panel">
          <div v-if="previewUrl && canPreviewOnline" class="preview-content">
            <div v-if="isOfficeFile" class="office-preview">
              <iframe
                :src="previewUrl"
                frameborder="0"
                width="100%"
                height="100%"
                class="preview-iframe"
              ></iframe>
            </div>
            <div v-else-if="isPdfFile" class="pdf-preview">
              <embed
                :src="previewUrl"
                type="application/pdf"
                width="100%"
                height="100%"
                class="preview-embed"
              />
            </div>
            <div v-else-if="isTextFile" class="text-preview">
              <pre class="text-content">{{ textContent }}</pre>
            </div>
          </div>
          <div v-else class="no-preview">
            <el-icon :size="64" class="no-preview-icon"><EditPen /></el-icon>
            <p class="no-preview-text">该文件类型不支持在线预览</p>
            <el-button type="primary" @click="downloadFile">
              <el-icon><Download /></el-icon>
              下载文件
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="loading-container">
      <el-spinner size="large" />
      <p>加载中...</p>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import {
  Files,
  Download,
  ZoomIn,
  ZoomOut,
  ArrowUp,
  ArrowDown,
  EditPen
} from '@element-plus/icons-vue'
import { storageAPI } from '../api'

const props = defineProps({
  visible: Boolean,
  fileId: String
})

const emit = defineEmits(['close', 'update:visible'])

const loading = ref(false)
const showProperties = ref(true)
const isFullscreen = ref(false)
const fileInfo = ref(null)
const currentFile = ref(null)
const textContent = ref('')

const previewUrl = computed(() => fileInfo.value?.previewUrl)
const canPreviewOnline = computed(() => fileInfo.value?.canPreviewOnline)
const extension = computed(() => fileInfo.value?.extension || '')

const isOfficeFile = computed(() => {
  return ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx'].includes(extension.value)
})

const isPdfFile = computed(() => extension.value === 'pdf')

const isTextFile = computed(() => {
  return ['txt', 'md', 'json', 'html', 'htm'].includes(extension.value)
})

const fileTypeText = computed(() => {
  const types = {
    'doc': 'Microsoft Word 文档',
    'docx': 'Microsoft Word 文档',
    'xls': 'Microsoft Excel 表格',
    'xlsx': 'Microsoft Excel 表格',
    'ppt': 'Microsoft PowerPoint 演示',
    'pptx': 'Microsoft PowerPoint 演示',
    'pdf': 'PDF 文档',
    'txt': '文本文档',
    'md': 'Markdown 文档',
    'json': 'JSON 文件',
    'html': 'HTML 文件'
  }
  return types[extension.value] || `${extension.value.toUpperCase()} 文件`
})

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

const loadFileInfo = async () => {
  if (!props.fileId) return
  
  loading.value = true
  try {
    const [infoRes, fileRes] = await Promise.all([
      storageAPI.getPreviewUrl(props.fileId),
      storageAPI.getFile(props.fileId)
    ])
    fileInfo.value = infoRes.data
    currentFile.value = fileRes.data
    
    if (isTextFile.value && previewUrl.value) {
      const textRes = await fetch(previewUrl.value)
      textContent.value = await textRes.text()
    }
  } catch (error) {
    console.error('加载文件信息失败:', error)
  } finally {
    loading.value = false
  }
}

const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value
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
}

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
.preview-container {
  display: flex;
  flex-direction: column;
  height: 70vh;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.preview-actions {
  display: flex;
  gap: 8px;
}

.preview-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.properties-panel {
  width: 280px;
  border-right: 1px solid #e4e7ed;
  background: #fafafa;
  transition: width 0.3s;
}

.properties-panel.collapsed {
  width: 40px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  border-bottom: 1px solid #e4e7ed;
}

.panel-title {
  font-weight: 600;
  font-size: 14px;
}

.properties-panel.collapsed .panel-title {
  display: none;
}

.properties-content {
  padding: 12px;
}

.property-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px dashed #e4e7ed;
}

.property-label {
  font-size: 13px;
  color: #606266;
}

.property-value {
  font-size: 13px;
  color: #303133;
  text-align: right;
  max-width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.preview-panel {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.preview-content {
  width: 100%;
  height: 100%;
}

.office-preview {
  width: 100%;
  height: 100%;
}

.preview-iframe {
  width: 100%;
  height: 100%;
}

.pdf-preview {
  width: 100%;
  height: 100%;
}

.preview-embed {
  width: 100%;
  height: 100%;
}

.text-preview {
  width: 100%;
  height: 100%;
  padding: 16px;
  overflow: auto;
}

.text-content {
  white-space: pre-wrap;
  word-break: break-all;
  font-family: monospace;
  font-size: 14px;
  color: #303133;
  margin: 0;
}

.no-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  color: #909399;
}

.no-preview-icon {
  color: #c0c4cc;
}

.no-preview-text {
  font-size: 14px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px;
  gap: 16px;
}
</style>