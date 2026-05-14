<template>
  <div class="project-library-view">
    <div class="page-header">
      <h2>项目资料库</h2>
      <div class="header-actions">
        <el-button type="primary" @click="openProjectModal()">创建项目</el-button>
        <el-button type="success" @click="openFolderModal()">创建文件夹</el-button>
      </div>
    </div>

    <div class="main-content">
      <el-card shadow="hover" class="folder-tree-card">
        <div class="folder-tree">
          <el-tree
            :data="folderTree"
            :props="defaultProps"
            node-key="folderId"
            default-expand-all
            :expand-on-click-node="false"
            @node-click="handleFolderClick"
            :highlight-current="true"
          >
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <span>
                  <el-icon :size="18"><component :is="getFolderIcon(node)" /></el-icon>
                  {{ node.label }}
                </span>
                <span class="tree-actions">
                  <el-button link type="primary" size="small" @click="editFolder(data)">编辑</el-button>
                  <el-button link type="danger" size="small" @click="deleteFolder(data.folderId)">删除</el-button>
                  <el-button link type="success" size="small" @click="uploadFile(data)">上传文件</el-button>
                </span>
              </span>
            </template>
          </el-tree>
        </div>
      </el-card>

      <el-card shadow="hover" class="file-list-card">
        <div class="file-header">
          <h3>{{ currentFolderName || '选择文件夹查看文件' }}</h3>
          <el-button v-if="currentFolderId" type="primary" size="small" @click="uploadFile({folderId: currentFolderId, name: currentFolderName})">上传文件</el-button>
        </div>
        
        <div v-if="currentFolderId" class="file-list">
          <el-table :data="files" border style="width: 100%;">
            <el-table-column prop="name" label="文件名" min-width="200">
              <template #default="scope">
                <span class="file-name">
                  <el-icon :size="18"><Files /></el-icon>
                  {{ scope.row.name }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="fileSize" label="大小" width="120">
              <template #default="scope">
                {{ formatFileSize(scope.row.fileSize) }}
              </template>
            </el-table-column>
            <el-table-column prop="fileType" label="类型" width="120" />
            <el-table-column prop="version" label="版本" width="80" />
            <el-table-column prop="controlled" label="受控" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.controlled === 'Y' ? 'success' : 'info'">
                  {{ scope.row.controlled === 'Y' ? '是' : '否' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdBy" label="创建人" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="180">
              <template #default="scope">
                <el-button size="small" @click="viewFile(scope.row)">查看</el-button>
                <el-button v-if="scope.row.status !== 'ARCHIVED'" size="small" @click="archiveFile(scope.row.fileId)">归档</el-button>
                <el-button size="small" type="danger" @click="deleteFile(scope.row.fileId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div v-if="files.length === 0" class="empty-state">
            <el-icon :size="48" class="empty-icon"><Folder /></el-icon>
            <p>该文件夹暂无文件</p>
            <el-button type="primary" @click="uploadFile({folderId: currentFolderId, name: currentFolderName})">上传文件</el-button>
          </div>
        </div>
        
        <div v-else class="empty-hint">
          <el-icon :size="48" class="empty-icon"><Folder /></el-icon>
          <p>请从左侧选择一个文件夹查看其中的文件</p>
        </div>
      </el-card>
    </div>

    <!-- 创建项目弹窗 -->
    <el-dialog v-model="showProjectModal" title="创建项目" width="400px">
      <el-form :model="projectForm" label-width="100px">
        <el-form-item label="项目名称" required>
          <el-input v-model="projectForm.name" />
        </el-form-item>
        <el-form-item label="项目编号">
          <el-input v-model="projectForm.code" placeholder="自动生成" />
        </el-form-item>
        <el-form-item label="项目描述">
          <el-textarea v-model="projectForm.description" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showProjectModal = false">取消</el-button>
        <el-button type="primary" @click="saveProject()">创建项目</el-button>
      </template>
    </el-dialog>

    <!-- 文件夹弹窗 -->
    <el-dialog v-model="showFolderModal" :title="folderForm.folderId ? '编辑文件夹' : '创建文件夹'" width="400px">
      <el-form :model="folderForm" label-width="100px">
        <el-form-item label="文件夹名称" required>
          <el-input v-model="folderForm.name" />
        </el-form-item>
        <el-form-item label="上级文件夹">
          <el-tree-select
            v-model="folderForm.parentFolderId"
            :data="folderTree"
            :props="defaultProps"
            check-strictly
            placeholder="选择上级文件夹（可选）"
          />
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="folderForm.path" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showFolderModal = false">取消</el-button>
        <el-button type="primary" @click="saveFolder()">保存</el-button>
      </template>
    </el-dialog>

    <!-- 文件上传弹窗 -->
    <el-dialog v-model="showUploadModal" title="上传文件" width="500px">
      <el-form :model="uploadForm" label-width="100px">
        <el-form-item label="选择文件" required>
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :file-list="uploadFiles"
            drag
            multiple
          >
            <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
            <div class="el-upload__text">
              拖拽文件到此处或 <em>点击上传</em>
            </div>
            <div class="el-upload__tip">
              支持上传多个文件
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="目标文件夹">
          <el-input v-model="uploadForm.folderName" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleUploadCancel">取消</el-button>
        <el-button type="primary" @click="handleUpload()" :loading="uploading">上传</el-button>
      </template>
    </el-dialog>

    <!-- 文件预览弹窗 -->
    <FilePreviewModal
      :visible.sync="showPreviewModal"
      :file-id="previewFileId"
      @close="handlePreviewClose"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, markRaw } from 'vue'
import { 
  Folder, 
  Briefcase, 
  Files, 
  Cpu, 
  Connection, 
  Checked, 
  Box,
  Notebook,
  Edit,
  CircleCheck,
  TrendCharts,
  DataLine,
  PieChart,
  User,
  CopyDocument,
  Warning,
  UploadFilled,
  EditPen
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { storageAPI, projectAPI } from '../api'
import FilePreviewModal from '../components/FilePreviewModal.vue'

const folderTree = ref([])
const showFolderModal = ref(false)
const showProjectModal = ref(false)
const showUploadModal = ref(false)
const files = ref([])
const uploadRef = ref(null)
const uploading = ref(false)
const uploadFiles = ref([])
const currentFolderId = ref('')
const currentFolderName = ref('')
const showPreviewModal = ref(false)
const previewFileId = ref('')

const defaultProps = {
  children: 'children',
  label: 'name'
}

const folderForm = reactive({
  folderId: '',
  name: '',
  parentFolderId: '',
  path: ''
})

const projectForm = reactive({
  projectId: '',
  name: '',
  code: '',
  description: ''
})

const uploadForm = reactive({
  folderId: '',
  folderName: ''
})

const level1Icons = {
  '项目管理': Briefcase,
  '客户需求管理': Files,
  '产品设计和开发': Cpu,
  '过程设计和开发': Connection,
  '项目质量': Checked,
  '供应商管理': User,
  '物流管理': Box
}

const level2Icons = {
  '系统需求': Notebook,
  '系统设计': DataLine,
  '软件需求': Notebook,
  '软件系统设计': Edit,
  '软件详细设计': Edit,
  '软件单元测试': CircleCheck,
  '软件集成测试': CircleCheck,
  '软件合格性测试': CircleCheck,
  '系统集成测试': CircleCheck,
  '系统合格性测试': CircleCheck,
  '软件发布': CopyDocument,
  '硬件设计': Cpu,
  '结构设计': DataLine,
  '光学设计': DataLine,
  '机器学习': PieChart,
  '外包供应商管理': User,
  '配置管理': Briefcase,
  '研发质量管理': TrendCharts
}

const getFolderIcon = (node) => {
  const label = node.label
  const level = getNodeLevel(node)
  
  if (level === 1) {
    return markRaw(Briefcase)
  }
  if (level === 2 && level1Icons[label]) {
    return markRaw(level1Icons[label])
  }
  if (level >= 3 && level2Icons[label]) {
    return markRaw(level2Icons[label])
  }
  
  return markRaw(Folder)
}

const getNodeLevel = (node) => {
  let level = 1
  let parent = node.parent
  while (parent) {
    level++
    parent = parent.parent
  }
  return level
}

const loadFolders = async () => {
  try {
    const response = await storageAPI.getFolders()
    folderTree.value = buildFolderTree(response.data || [])
  } catch (error) {
    console.error('加载文件夹失败:', error)
    ElMessage.error('加载文件夹失败')
  }
}

const buildFolderTree = (folders) => {
  const folderMap = new Map()
  const tree = []

  folders.forEach(folder => {
    folderMap.set(folder.folderId, { ...folder, children: [] })
  })

  folders.forEach(folder => {
    const node = folderMap.get(folder.folderId)
    if (!folder.parentFolderId) {
      tree.push(node)
    } else {
      const parent = folderMap.get(folder.parentFolderId)
      if (parent) {
        parent.children.push(node)
      }
    }
  })

  return tree
}

const openProjectModal = () => {
  Object.keys(projectForm).forEach(key => {
    projectForm[key] = ''
  })
  showProjectModal.value = true
}

const saveProject = async () => {
  if (!projectForm.name) {
    ElMessage.warning('请输入项目名称')
    return
  }
  
  try {
    await projectAPI.create(projectForm)
    ElMessage.success('创建项目成功')
    showProjectModal.value = false
    loadFolders()
  } catch (error) {
    console.error('创建项目失败:', error)
    ElMessage.error('创建项目失败')
  }
}

const openFolderModal = (folder = null) => {
  if (folder) {
    Object.assign(folderForm, folder)
  } else {
    Object.keys(folderForm).forEach(key => {
      folderForm[key] = ''
    })
  }
  showFolderModal.value = true
}

const editFolder = (folder) => {
  openFolderModal(folder)
}

const saveFolder = async () => {
  try {
    if (folderForm.folderId) {
      await storageAPI.updateFolder(folderForm.folderId, folderForm)
      ElMessage.success('更新文件夹成功')
    } else {
      await storageAPI.createFolder(folderForm)
      ElMessage.success('创建文件夹成功')
    }
    showFolderModal.value = false
    loadFolders()
  } catch (error) {
    console.error('保存文件夹失败:', error)
    ElMessage.error('保存文件夹失败')
  }
}

const deleteFolder = async (folderId) => {
  try {
    await ElMessageBox.confirm('确定删除该文件夹吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await storageAPI.deleteFolder(folderId)
    ElMessage.success('删除成功')
    loadFolders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除文件夹失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleFolderClick = async (data) => {
  currentFolderId.value = data.folderId
  currentFolderName.value = data.name
  await loadFilesByFolder(data.folderId)
}

const loadFilesByFolder = async (folderId) => {
  try {
    const response = await storageAPI.getFilesByFolder(folderId)
    files.value = response.data || []
  } catch (error) {
    console.error('加载文件失败:', error)
    ElMessage.error('加载文件失败')
  }
}

const uploadFile = (folder) => {
  uploadForm.folderId = folder.folderId
  uploadForm.folderName = folder.name
  uploadFiles.value = []
  showUploadModal.value = true
}

const handleFileChange = (file, fileList) => {
  uploadFiles.value = fileList
}

const handleFileRemove = (file, fileList) => {
  uploadFiles.value = fileList
}

const handleUploadCancel = () => {
  showUploadModal.value = false
  uploadFiles.value = []
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}

const handleUpload = async () => {
  if (uploadFiles.value.length === 0) {
    ElMessage.warning('请选择文件')
    return
  }
  
  uploading.value = true
  
  try {
    const formData = new FormData()
    uploadFiles.value.forEach(file => {
      formData.append('files', file.raw)
    })
    formData.append('folderId', uploadForm.folderId)
    formData.append('createdBy', 'system')
    
    await storageAPI.uploadFiles(formData)
    ElMessage.success(`成功上传 ${uploadFiles.value.length} 个文件`)
    handleUploadCancel()
    await loadFilesByFolder(uploadForm.folderId)
    uploading.value = false
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败')
    uploading.value = false
  }
}

const formatFileSize = (bytes) => {
  if (!bytes || bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const viewFile = (file) => {
  previewFileId.value = file.fileId
  showPreviewModal.value = true
}

const handlePreviewClose = () => {
  showPreviewModal.value = false
  previewFileId.value = ''
}

const archiveFile = async (fileId) => {
  try {
    await storageAPI.archiveFile(fileId)
    ElMessage.success('归档成功')
    await loadFilesByFolder(currentFolderId.value)
  } catch (error) {
    console.error('归档失败:', error)
    ElMessage.error('归档失败')
  }
}

const deleteFile = async (fileId) => {
  try {
    await ElMessageBox.confirm('确定删除该文件吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await storageAPI.deleteFile(fileId)
    ElMessage.success('删除成功')
    await loadFilesByFolder(currentFolderId.value)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除文件失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadFolders()
})
</script>

<style scoped>
.project-library-view {
  padding: 20px;
  height: calc(100vh - 100px);
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-shrink: 0;
}

.page-header h2 {
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.main-content {
  display: flex;
  gap: 20px;
  flex: 1;
  overflow: hidden;
}

.folder-tree-card {
  width: 350px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.folder-tree {
  flex: 1;
  overflow-y: auto;
}

.file-list-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  min-width: 0;
}

.file-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.file-header h3 {
  margin: 0;
  font-size: 16px;
}

.file-list {
  flex: 1;
  overflow-y: auto;
}

.custom-tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.tree-actions {
  display: flex;
  gap: 5px;
  opacity: 0;
  transition: opacity 0.3s;
}

.custom-tree-node:hover .tree-actions {
  opacity: 1;
}

.file-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.empty-state, .empty-hint {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 60px 20px;
  color: #999;
}

.empty-icon {
  margin-bottom: 16px;
  color: #ccc;
}

.empty-state p, .empty-hint p {
  margin: 0 0 16px 0;
}
</style>
