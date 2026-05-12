
<template>
  <div class="pcb-list">
    <div class="toolbar">
      <el-select v-model="selectedProject" placeholder="选择项目">
        <el-option label="全部" value="" />
        <el-option v-for="p in projects" :key="p.projectId" :label="p.name" :value="p.projectId" />
      </el-select>
      <el-button type="primary" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        上传PCB设计
      </el-button>
    </div>
    
    <el-table :data="pcbs" border>
      <el-table-column prop="pcbId" label="PCB ID" width="100" />
      <el-table-column prop="projectId" label="项目ID" width="120" />
      <el-table-column prop="name" label="文件名称" />
      <el-table-column prop="filePath" label="文件路径" />
      <el-table-column prop="layers" label="层数" width="80" />
      <el-table-column prop="size" label="尺寸" width="100" />
      <el-table-column prop="version" label="版本" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdBy" label="创建人" width="100" />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="previewPcb(scope.row)">预览</el-button>
          <el-button size="small" @click="editPcb(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deletePcb(scope.row.pcbId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑弹窗 -->
    <el-dialog :title="editPcbData ? '编辑PCB' : '上传PCB设计'" :visible.sync="showAddModal">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="项目">
          <el-select v-model="formData.projectId">
            <el-option v-for="p in projects" :key="p.projectId" :label="p.name" :value="p.projectId" />
          </el-select>
        </el-form-item>
        <el-form-item label="文件名称">
          <el-input v-model="formData.name" />
        </el-form-item>
        <el-form-item label="文件路径">
          <el-input v-model="formData.filePath" />
        </el-form-item>
        <el-form-item label="层数">
          <el-input type="number" v-model="formData.layers" />
        </el-form-item>
        <el-form-item label="尺寸">
          <el-input v-model="formData.size" />
        </el-form-item>
        <el-form-item label="版本">
          <el-input v-model="formData.version" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status">
            <el-option label="DRAFT" value="DRAFT" />
            <el-option label="REVIEW" value="REVIEW" />
            <el-option label="APPROVED" value="APPROVED" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建人">
          <el-input v-model="formData.createdBy" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="savePcb">保存</el-button>
      </template>
    </el-dialog>

    <!-- 预览弹窗 -->
    <el-dialog title="PCB预览" :visible.sync="showPreviewModal" width="800px">
      <div class="preview-content">
        <p>文件名称: {{ previewData?.name }}</p>
        <p>文件路径: {{ previewData?.filePath }}</p>
        <p>层数: {{ previewData?.layers }}</p>
        <p>尺寸: {{ previewData?.size }}</p>
        <p>版本: {{ previewData?.version }}</p>
        <div class="preview-placeholder">
          <el-icon size="100"><component :is="icons.Layers" /></el-icon>
          <p>PCB预览区域</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, markRaw, onMounted, watch } from 'vue'
import { Plus, Grid } from '@element-plus/icons-vue'
import { hardwareAPI } from '../api'

const icons = { Plus: markRaw(Plus), Layers: markRaw(Grid) }

const projects = ref([])
const pcbs = ref([])
const selectedProject = ref('')
const showAddModal = ref(false)
const showPreviewModal = ref(false)
const editPcbData = ref(null)
const previewData = ref(null)

const formData = reactive({
  projectId: '',
  name: '',
  filePath: '',
  layers: 2,
  size: '',
  version: '1.0',
  status: 'DRAFT',
  createdBy: ''
})

const loadProjects = async () => {
  try {
    const response = await hardwareAPI.getProjects()
    projects.value = response.data
  } catch (error) {
    console.error('加载项目失败:', error)
  }
}

const loadPcbs = async () => {
  try {
    if (selectedProject.value) {
      const response = await hardwareAPI.getPcbs(selectedProject.value)
      pcbs.value = response.data
    } else {
      pcbs.value = []
    }
  } catch (error) {
    console.error('加载PCB失败:', error)
  }
}

const getStatusTagType = (status) => {
  switch (status) {
    case 'DRAFT': return 'info'
    case 'REVIEW': return 'warning'
    case 'APPROVED': return 'success'
    default: return 'default'
  }
}

const previewPcb = (pcb) => {
  previewData.value = pcb
  showPreviewModal.value = true
}

const editPcb = (pcb) => {
  editPcbData.value = pcb
  formData.projectId = pcb.projectId
  formData.name = pcb.name
  formData.filePath = pcb.filePath
  formData.layers = pcb.layers || 2
  formData.size = pcb.size || ''
  formData.version = pcb.version || '1.0'
  formData.status = pcb.status
  formData.createdBy = pcb.createdBy || ''
  showAddModal.value = true
}

const deletePcb = async (id) => {
  try {
    await hardwareAPI.deletePcb(id)
    loadPcbs()
  } catch (error) {
    console.error('删除PCB失败:', error)
  }
}

const savePcb = async () => {
  try {
    if (editPcbData.value) {
      await hardwareAPI.updatePcb(editPcbData.value.pcbId, formData)
    } else {
      await hardwareAPI.createPcb(formData)
    }
    showAddModal.value = false
    editPcbData.value = null
    formData.projectId = ''
    formData.name = ''
    formData.filePath = ''
    formData.layers = 2
    formData.size = ''
    formData.version = '1.0'
    formData.status = 'DRAFT'
    formData.createdBy = ''
    loadPcbs()
  } catch (error) {
    console.error('保存PCB失败:', error)
  }
}

watch(selectedProject, () => {
  loadPcbs()
})

onMounted(() => {
  loadProjects()
})
</script>

<style scoped>
.pcb-list {
  padding: 10px;
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.preview-content {
  padding: 20px;
}

.preview-placeholder {
  margin-top: 20px;
  padding: 40px;
  text-align: center;
  background-color: #f5f5f5;
  border-radius: 8px;
}
</style>
