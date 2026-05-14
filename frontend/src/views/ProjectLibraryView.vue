<template>
  <div class="project-library-view">
    <div class="page-header">
      <h2>项目资料库</h2>
      <div class="header-actions">
        <el-button type="primary" @click="openFolderModal()">创建文件夹</el-button>
      </div>
    </div>

    <el-card shadow="hover">
      <div class="folder-tree">
        <el-tree
          :data="folderTree"
          :props="defaultProps"
          node-key="folderId"
          default-expand-all
          :expand-on-click-node="false"
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
            :limit="1"
            :on-change="handleFileChange"
            :on-exceed="handleExceed"
            drag
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              拖拽文件到此处或 <em>点击上传</em>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="目标文件夹">
          <el-input v-model="uploadForm.folderName" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showUploadModal = false">取消</el-button>
        <el-button type="primary" @click="handleUpload()" :loading="uploading">上传</el-button>
      </template>
    </el-dialog>
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
  Warning
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { storageAPI } from '../api'

const folderTree = ref([])
const showFolderModal = ref(false)
const files = ref([])

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
  
  if (level === 1 && level1Icons[label]) {
    return markRaw(level1Icons[label])
  }
  if (level === 2 && level2Icons[label]) {
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
    await ElMessageBox.confirm('确定要删除此文件夹吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await storageAPI.deleteFolder(folderId)
    ElMessage.success('删除文件夹成功')
    loadFolders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除文件夹失败:', error)
      ElMessage.error('删除文件夹失败')
    }
  }
}

const uploadFile = (folder) => {
  ElMessage.info('上传文件功能开发中...')
}

onMounted(() => {
  loadFolders()
})
</script>

<style scoped>
.project-library-view {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.folder-tree {
  padding: 20px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  gap: 10px;
}

.tree-actions {
  display: flex;
  gap: 5px;
}
</style>