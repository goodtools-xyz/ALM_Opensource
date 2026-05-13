<template>
  <div class="design-container">
    <div class="page-header">
      <h2>设计管理</h2>
      <div class="header-actions">
        <el-input v-model="searchKeyword" placeholder="搜索设计" clearable class="search-input">
          <template #append>
            <el-button @click="loadDesigns">搜索</el-button>
          </template>
        </el-input>
        <el-button type="primary" @click="showCreateModal = true">新建设计</el-button>
      </div>
    </div>

    <el-card shadow="hover">
      <el-table :data="designs" stripe>
        <el-table-column prop="designId" label="设计ID" width="120" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeColor(row.type)" size="small">{{ getTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusColor(row.status)" size="small">{{ getStatusName(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="relatedReqId" label="关联需求" width="120" />
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="designer" label="设计人员" width="120" />
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewDesign(row)">查看</el-button>
            <el-button type="success" link @click="editDesign(row)">编辑</el-button>
            <el-button type="danger" link @click="deleteDesign(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showCreateModal" :title="editingItem ? '编辑设计' : '新建设计'" width="600px">
      <el-form :model="designForm" label-width="100px">
        <el-form-item label="设计ID">
          <el-input v-model="designForm.designId" :disabled="!!editingItem" />
        </el-form-item>
        <el-form-item label="标题" required>
          <el-input v-model="designForm.title" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="designForm.type">
            <el-option label="架构设计" value="ARCHITECTURE" />
            <el-option label="详细设计" value="DETAILED" />
            <el-option label="界面设计" value="UI" />
            <el-option label="数据库设计" value="DATABASE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="designForm.status">
            <el-option label="待评审" value="PENDING" />
            <el-option label="已批准" value="APPROVED" />
            <el-option label="已实现" value="IMPLEMENTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联需求">
          <el-input v-model="designForm.relatedReqId" placeholder="如 REQ-001" />
        </el-form-item>
        <el-form-item label="模块">
          <el-input v-model="designForm.module" />
        </el-form-item>
        <el-form-item label="设计人员">
          <el-input v-model="designForm.designer" />
        </el-form-item>
        <el-form-item label="版本">
          <el-input v-model="designForm.version" />
        </el-form-item>
        <el-form-item label="文件路径">
          <el-input v-model="designForm.filePath" />
        </el-form-item>
        <el-form-item label="描述">
          <el-textarea v-model="designForm.description" rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateModal = false">取消</el-button>
        <el-button type="primary" @click="saveDesign">{{ editingItem ? '保存修改' : '创建设计' }}</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showDetailModal" title="设计详情" width="600px">
      <el-descriptions :column="2" border v-if="selectedDesign">
        <el-descriptions-item label="设计ID">{{ selectedDesign.designId }}</el-descriptions-item>
        <el-descriptions-item label="标题">{{ selectedDesign.title }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ getTypeName(selectedDesign.type) }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusName(selectedDesign.status) }}</el-descriptions-item>
        <el-descriptions-item label="关联需求">{{ selectedDesign.relatedReqId }}</el-descriptions-item>
        <el-descriptions-item label="模块">{{ selectedDesign.module }}</el-descriptions-item>
        <el-descriptions-item label="设计人员">{{ selectedDesign.designer }}</el-descriptions-item>
        <el-descriptions-item label="版本">{{ selectedDesign.version }}</el-descriptions-item>
        <el-descriptions-item label="文件路径">{{ selectedDesign.filePath }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedDesign.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ selectedDesign.description }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="showDetailModal = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { designAPI } from '../api'
import { ElMessage } from 'element-plus'

const searchKeyword = ref('')
const designs = ref([])
const showCreateModal = ref(false)
const showDetailModal = ref(false)
const editingItem = ref(null)
const selectedDesign = ref(null)

const designForm = reactive({
  designId: '',
  title: '',
  description: '',
  type: 'DETAILED',
  status: 'PENDING',
  version: '1.0',
  module: '',
  relatedReqId: '',
  designer: '',
  filePath: ''
})

const loadDesigns = async () => {
  try {
    const params = {}
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    const response = await designAPI.getAll(params)
    designs.value = response.data || []
  } catch (error) {
    console.error('加载设计失败:', error)
    ElMessage.error('加载设计失败')
  }
}

const createDesign = async () => {
  try {
    await designAPI.create(designForm)
    ElMessage.success('创建设计成功')
    resetForm()
    showCreateModal.value = false
    loadDesigns()
  } catch (error) {
    console.error('创建设计失败:', error)
    ElMessage.error('创建设计失败')
  }
}

const updateDesign = async () => {
  try {
    await designAPI.update(editingItem.value.id, designForm)
    ElMessage.success('更新设计成功')
    resetForm()
    showCreateModal.value = false
    loadDesigns()
  } catch (error) {
    console.error('更新设计失败:', error)
    ElMessage.error('更新设计失败')
  }
}

const saveDesign = () => {
  if (!designForm.title) {
    ElMessage.warning('请输入设计标题')
    return
  }
  if (editingItem.value) {
    updateDesign()
  } else {
    createDesign()
  }
}

const deleteDesign = async (item) => {
  try {
    await designAPI.delete(item.id)
    ElMessage.success('删除设计成功')
    loadDesigns()
  } catch (error) {
    console.error('删除设计失败:', error)
    ElMessage.error('删除设计失败')
  }
}

const viewDesign = (item) => {
  selectedDesign.value = item
  showDetailModal.value = true
}

const editDesign = (item) => {
  editingItem.value = item
  Object.assign(designForm, item)
  showCreateModal.value = true
}

const resetForm = () => {
  editingItem.value = null
  Object.keys(designForm).forEach(key => {
    designForm[key] = ''
  })
  designForm.type = 'DETAILED'
  designForm.status = 'PENDING'
  designForm.version = '1.0'
}

const getTypeColor = (type) => {
  const colors = { ARCHITECTURE: 'danger', DETAILED: 'primary', UI: 'warning', DATABASE: 'success' }
  return colors[type] || 'info'
}

const getTypeName = (type) => {
  const names = { ARCHITECTURE: '架构设计', DETAILED: '详细设计', UI: '界面设计', DATABASE: '数据库设计' }
  return names[type] || type
}

const getStatusColor = (status) => {
  const colors = { PENDING: 'info', APPROVED: 'success', IMPLEMENTED: 'primary' }
  return colors[status] || 'info'
}

const getStatusName = (status) => {
  const names = { PENDING: '待评审', APPROVED: '已批准', IMPLEMENTED: '已实现' }
  return names[status] || status
}

onMounted(() => {
  loadDesigns()
})
</script>

<style scoped>
.design-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-input {
  width: 300px;
}
</style>