<template>
  <div class="storage-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="文件夹管理" name="folders">
        <div class="tab-content">
          <el-button type="primary" @click="openFolderModal()">创建文件夹</el-button>
          <el-table :data="folders" border style="width: 100%;">
            <el-table-column prop="folderId" label="文件夹ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="parentFolderId" label="上级文件夹" width="120" />
            <el-table-column prop="path" label="路径" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="editFolder(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteFolder(scope.row.folderId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="文件管理" name="files">
        <div class="tab-content">
          <el-select v-model="selectedFolder" placeholder="选择文件夹">
            <el-option label="全部" value="" />
            <el-option v-for="f in folders" :key="f.folderId" :label="f.name" :value="f.folderId" />
          </el-select>
          <el-button type="primary" @click="openFileModal()">上传文件</el-button>
          <el-table :data="files" border style="width: 100%;">
            <el-table-column prop="fileId" label="文件ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="isoFileNumber" label="文件编号" width="120" />
            <el-table-column prop="controlled" label="受控" width="80" />
            <el-table-column prop="version" label="版本" width="80" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="viewAudit(scope.row.fileId)">查看审计</el-button>
                <el-button v-if="scope.row.status !== 'ARCHIVED'" size="small" @click="archiveFile(scope.row.fileId)">归档</el-button>
                <el-button size="small" type="danger" @click="deleteFile(scope.row.fileId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 文件夹弹窗 -->
    <el-dialog v-model="showFolderModal" :title="folderForm.folderId ? '编辑文件夹' : '创建文件夹'" width="400px">
      <el-form :model="folderForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="folderForm.name" />
        </el-form-item>
        <el-form-item label="上级文件夹">
          <el-select v-model="folderForm.parentFolderId">
            <el-option label="无" value="" />
            <el-option v-for="f in folders" :key="f.folderId" :label="f.name" :value="f.folderId" />
          </el-select>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="folderForm.path" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showFolderModal = false">取消</el-button>
        <el-button type="primary" @click="saveFolder()">保存</el-button>
      </template>
    </el-dialog>

    <!-- 文件上传弹窗 -->
    <el-dialog v-model="showFileModal" :title="fileForm.fileId ? '编辑文件' : '上传文件'" width="450px">
      <el-form :model="fileForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="fileForm.name" />
        </el-form-item>
        <el-form-item label="文件编号">
          <el-input v-model="fileForm.isoFileNumber" />
        </el-form-item>
        <el-form-item label="所属文件夹">
          <el-select v-model="fileForm.folderId">
            <el-option v-for="f in folders" :key="f.folderId" :label="f.name" :value="f.folderId" />
          </el-select>
        </el-form-item>
        <el-form-item label="受控">
          <el-switch v-model="fileForm.controlled" />
        </el-form-item>
        <el-form-item label="版本">
          <el-input v-model="fileForm.version" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="fileForm.status">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="归档" value="ARCHIVED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showFileModal = false">取消</el-button>
        <el-button type="primary" @click="saveFile()">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from 'vue'
import { storageAPI } from '../api'

const activeTab = ref('folders')
const folders = ref([])
const files = ref([])
const selectedFolder = ref('')
const showFolderModal = ref(false)
const showFileModal = ref(false)

const folderForm = reactive({
  folderId: null,
  name: '',
  parentFolderId: '',
  path: ''
})

const fileForm = reactive({
  fileId: null,
  name: '',
  isoFileNumber: '',
  folderId: '',
  controlled: false,
  version: '1.0',
  status: 'ACTIVE'
})

const loadFolders = async () => {
  const response = await storageAPI.getFolders()
  folders.value = response.data
}

const loadFiles = async () => {
  if (selectedFolder.value) {
    const response = await storageAPI.getFilesByFolder(selectedFolder.value)
    files.value = response.data
  } else {
    const response = await storageAPI.getFiles()
    files.value = response.data
  }
}

const deleteFolder = async (id) => {
  await storageAPI.deleteFolder(id)
  loadFolders()
}

const deleteFile = async (id) => {
  await storageAPI.deleteFile(id)
  loadFiles()
}

const archiveFile = async (id) => {
  await storageAPI.archiveFile(id)
  loadFiles()
}

const viewAudit = (fileId) => {
  storageAPI.getFileAudit(fileId)
}

const openFolderModal = () => {
  folderForm.folderId = null
  folderForm.name = ''
  folderForm.parentFolderId = ''
  folderForm.path = ''
  showFolderModal.value = true
}

const editFolder = (row) => {
  folderForm.folderId = row.folderId
  folderForm.name = row.name
  folderForm.parentFolderId = row.parentFolderId
  folderForm.path = row.path
  showFolderModal.value = true
}

const saveFolder = async () => {
  if (folderForm.folderId) {
    await storageAPI.updateFolder(folderForm.folderId, folderForm)
  } else {
    await storageAPI.createFolder(folderForm)
  }
  showFolderModal.value = false
  loadFolders()
}

const openFileModal = () => {
  fileForm.fileId = null
  fileForm.name = ''
  fileForm.isoFileNumber = ''
  fileForm.folderId = ''
  fileForm.controlled = false
  fileForm.version = '1.0'
  fileForm.status = 'ACTIVE'
  showFileModal.value = true
}

const saveFile = async () => {
  if (fileForm.fileId) {
    await storageAPI.updateFile(fileForm.fileId, fileForm)
  } else {
    await storageAPI.createFile(fileForm)
  }
  showFileModal.value = false
  loadFiles()
}

watch(selectedFolder, () => {
  loadFiles()
})

onMounted(() => {
  loadFolders()
  loadFiles()
})
</script>

<style scoped>
.storage-view {
  padding: 10px;
  height: 100%;
}

.tab-content {
  margin-top: 10px;
}
</style>