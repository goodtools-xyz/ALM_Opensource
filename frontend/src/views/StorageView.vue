
<template>
  <div class="storage-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="文件夹管理" name="folders">
        <div class="tab-content">
          <el-button type="primary" @click="showFolderModal = true">创建文件夹</el-button>
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
          <el-button type="primary" @click="showFileModal = true">上传文件</el-button>
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
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { storageAPI } from '../api'

const activeTab = ref('folders')
const folders = ref([])
const files = ref([])
const selectedFolder = ref('')
const showFolderModal = ref(false)
const showFileModal = ref(false)

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

const editFolder = (row) => {
  showFolderModal.value = true
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
