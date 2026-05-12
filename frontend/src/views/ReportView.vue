
<template>
  <div class="report-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="报表模板" name="templates">
        <div class="tab-content">
          <el-button type="primary" @click="showTemplateModal = true">创建模板</el-button>
          <el-table :data="templates" border style="width: 100%;">
            <el-table-column prop="templateId" label="模板ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="type" label="类型" width="100" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="generateReport(scope.row)">生成报表</el-button>
                <el-button size="small" @click="editTemplate(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteTemplate(scope.row.templateId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="个性化看板" name="kanban">
        <div class="tab-content">
          <el-button type="primary" @click="showKanbanModal = true">创建看板</el-button>
          <el-table :data="kanbans" border style="width: 100%;">
            <el-table-column prop="boardId" label="看板ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="projectId" label="关联项目" width="120" />
            <el-table-column prop="shared" label="共享" width="80" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="viewKanban(scope.row)">查看</el-button>
                <el-button size="small" type="danger" @click="deleteKanban(scope.row.boardId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { reportAPI } from '../api'

const activeTab = ref('templates')
const templates = ref([])
const kanbans = ref([])
const showTemplateModal = ref(false)
const showKanbanModal = ref(false)

const loadTemplates = async () => {
  const response = await reportAPI.getTemplates()
  templates.value = response.data
}

const loadKanbans = async () => {
  const response = await reportAPI.getKanbanBoards()
  kanbans.value = response.data
}

const deleteTemplate = async (id) => {
  await reportAPI.deleteTemplate(id)
  loadTemplates()
}

const deleteKanban = async (id) => {
  await reportAPI.deleteKanban(id)
  loadKanbans()
}

const generateReport = (row) => {
  reportAPI.generateReport(row.templateId, {})
}

const editTemplate = (row) => {
  showTemplateModal.value = true
}

const viewKanban = (row) => {
  showKanbanModal.value = true
}

onMounted(() => {
  loadTemplates()
  loadKanbans()
})
</script>

<style scoped>
.report-view {
  padding: 10px;
  height: 100%;
}

.tab-content {
  margin-top: 10px;
}
</style>
