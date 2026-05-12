
<template>
  <div class="drawing-list">
    <div class="toolbar">
      <el-select v-model="selectedProject" placeholder="选择项目">
        <el-option label="全部" value="" />
        <el-option v-for="p in projects" :key="p.projectId" :label="p.name" :value="p.projectId" />
      </el-select>
      <el-button type="primary" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        上传2D图纸
      </el-button>
    </div>
    
    <el-table :data="drawings" border>
      <el-table-column prop="drawingId" label="图纸ID" width="100" />
      <el-table-column prop="projectId" label="项目ID" width="120" />
      <el-table-column prop="name" label="文件名称" />
      <el-table-column prop="filePath" label="文件路径" />
      <el-table-column prop="fileFormat" label="文件格式" width="120" />
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
          <el-button size="small" @click="previewDrawing(scope.row)">预览</el-button>
          <el-button size="small" @click="editDrawing(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteDrawing(scope.row.drawingId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑弹窗 -->
    <el-dialog :title="editDrawingData ? '编辑2D图纸' : '上传2D图纸'" :visible.sync="showAddModal">
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
        <el-form-item label="文件格式">
          <el-select v-model="formData.fileFormat">
            <el-option label="DWG" value="DWG" />
            <el-option label="DXF" value="DXF" />
            <el-option label="PDF" value="PDF" />
          </el-select>
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
        <el-button type="primary" @click="saveDrawing">保存</el-button>
      </template>
    </el-dialog>

    <!-- 预览弹窗 -->
    <el-dialog title="2D图纸预览" :visible.sync="showPreviewModal" width="800px">
      <div class="preview-content">
        <p>文件名称: {{ previewData?.name }}</p>
        <p>文件路径: {{ previewData?.filePath }}</p>
        <p>文件格式: {{ previewData?.fileFormat }}</p>
        <p>版本: {{ previewData?.version }}</p>
        <div class="preview-placeholder">
          <el-icon size="100"><component :is="icons.FileText" /></el-icon>
          <p>2D图纸预览区域</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, markRaw, onMounted, watch } from 'vue'
import { Plus, Files } from '@element-plus/icons-vue'
import { structureAPI } from '../api'

const icons = { Plus: markRaw(Plus), FileText: markRaw(Files) }

const projects = ref([])
const drawings = ref([])
const selectedProject = ref('')
const showAddModal = ref(false)
const showPreviewModal = ref(false)
const editDrawingData = ref(null)
const previewData = ref(null)

const formData = reactive({
  projectId: '',
  name: '',
  filePath: '',
  fileFormat: 'DWG',
  version: '1.0',
  status: 'DRAFT',
  createdBy: ''
})

const loadProjects = async () => {
  try {
    const response = await structureAPI.getProjects()
    projects.value = response.data
  } catch (error) {
    console.error('加载项目失败:', error)
  }
}

const loadDrawings = async () => {
  try {
    if (selectedProject.value) {
      const response = await structureAPI.get2dDrawings(selectedProject.value)
      drawings.value = response.data
    } else {
      drawings.value = []
    }
  } catch (error) {
    console.error('加载2D图纸失败:', error)
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

const previewDrawing = (drawing) => {
  previewData.value = drawing
  showPreviewModal.value = true
}

const editDrawing = (drawing) => {
  editDrawingData.value = drawing
  formData.projectId = drawing.projectId
  formData.name = drawing.name
  formData.filePath = drawing.filePath
  formData.fileFormat = drawing.fileFormat || 'DWG'
  formData.version = drawing.version || '1.0'
  formData.status = drawing.status
  formData.createdBy = drawing.createdBy || ''
  showAddModal.value = true
}

const deleteDrawing = async (id) => {
  try {
    await structureAPI.delete2dDrawing(id)
    loadDrawings()
  } catch (error) {
    console.error('删除2D图纸失败:', error)
  }
}

const saveDrawing = async () => {
  try {
    if (editDrawingData.value) {
      await structureAPI.update2dDrawing(editDrawingData.value.drawingId, formData)
    } else {
      await structureAPI.create2dDrawing(formData)
    }
    showAddModal.value = false
    editDrawingData.value = null
    formData.projectId = ''
    formData.name = ''
    formData.filePath = ''
    formData.fileFormat = 'DWG'
    formData.version = '1.0'
    formData.status = 'DRAFT'
    formData.createdBy = ''
    loadDrawings()
  } catch (error) {
    console.error('保存2D图纸失败:', error)
  }
}

watch(selectedProject, () => {
  loadDrawings()
})

onMounted(() => {
  loadProjects()
})
</script>

<style scoped>
.drawing-list {
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
