
<template>
  <div class="project-list">
    <div class="toolbar">
      <el-button type="primary" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        新建项目
      </el-button>
    </div>
    
    <el-table :data="projects" border>
      <el-table-column prop="projectId" label="项目ID" width="120" />
      <el-table-column prop="name" label="项目名称" />
      <el-table-column prop="description" label="项目描述" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdBy" label="创建人" width="100" />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="small" @click="editProject(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteProject(scope.row.projectId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="showAddModal" :title="editProjectData ? '编辑项目' : '新建项目'">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="项目名称">
          <el-input v-model="formData.name" />
        </el-form-item>
        <el-form-item label="项目描述">
          <el-textarea v-model="formData.description" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status">
            <el-option label="PLANNING" value="PLANNING" />
            <el-option label="IN_PROGRESS" value="IN_PROGRESS" />
            <el-option label="COMPLETED" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建人">
          <el-input v-model="formData.createdBy" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveProject">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, markRaw, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { structureAPI } from '../api'

const icons = { Plus: markRaw(Plus) }

const projects = ref([])
const showAddModal = ref(false)
const editProjectData = ref(null)
const formData = reactive({
  name: '',
  description: '',
  status: 'PLANNING',
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

const getStatusTagType = (status) => {
  switch (status) {
    case 'PLANNING': return 'info'
    case 'IN_PROGRESS': return 'warning'
    case 'COMPLETED': return 'success'
    default: return 'default'
  }
}

const editProject = (project) => {
  editProjectData.value = project
  formData.name = project.name
  formData.description = project.description || ''
  formData.status = project.status
  formData.createdBy = project.createdBy || ''
  showAddModal.value = true
}

const deleteProject = async (id) => {
  try {
    await structureAPI.deleteProject(id)
    loadProjects()
  } catch (error) {
    console.error('删除项目失败:', error)
  }
}

const saveProject = async () => {
  try {
    if (editProjectData.value) {
      await structureAPI.updateProject(editProjectData.value.projectId, formData)
    } else {
      await structureAPI.createProject(formData)
    }
    showAddModal.value = false
    editProjectData.value = null
    formData.name = ''
    formData.description = ''
    formData.status = 'PLANNING'
    formData.createdBy = ''
    loadProjects()
  } catch (error) {
    console.error('保存项目失败:', error)
  }
}

onMounted(() => {
  loadProjects()
})
</script>

<style scoped>
.project-list {
  padding: 10px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>
