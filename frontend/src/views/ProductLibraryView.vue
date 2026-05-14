<template>
  <div class="product-library-view">
    <div class="page-header">
      <h2>产品库</h2>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, markRaw } from 'vue'
import { 
  Folder, 
  Box,
  Edit,
  CircleCheck,
  TrendCharts,
  DataLine,
  PieChart,
  User,
  CopyDocument,
  Warning,
  Briefcase,
  Files,
  Cpu,
  Connection,
  Checked,
  Notebook
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

const productIcons = {
  '产品设计': Edit,
  '产品规格': DataLine,
  '产品文档': Notebook,
  '产品测试': CircleCheck,
  '产品发布': CopyDocument,
  '版本管理': Connection,
  '产品需求': Files,
  '竞品分析': PieChart,
  '市场调研': TrendCharts,
  '产品路线图': Checked,
  '用户手册': Briefcase,
  '技术规格': Cpu
}

const getFolderIcon = (node) => {
  const label = node.label
  
  if (productIcons[label]) {
    return markRaw(productIcons[label])
  }
  
  return markRaw(Folder)
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
.product-library-view {
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