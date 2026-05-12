
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

    <!-- 模板弹窗 -->
    <el-dialog v-model="showTemplateModal" :title="templateForm.templateId ? '编辑模板' : '创建模板'" width="400px">
      <el-form :model="templateForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="templateForm.name" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="templateForm.type">
            <el-option label="项目报表" value="PROJECT" />
            <el-option label="进度报表" value="PROGRESS" />
            <el-option label="资源报表" value="RESOURCE" />
            <el-option label="质量报表" value="QUALITY" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="templateForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="templateForm.status">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showTemplateModal = false">取消</el-button>
        <el-button type="primary" @click="saveTemplate()">保存</el-button>
      </template>
    </el-dialog>

    <!-- 看板弹窗 -->
    <el-dialog v-model="showKanbanModal" :title="kanbanForm.boardId ? '编辑看板' : '创建看板'" width="400px">
      <el-form :model="kanbanForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="kanbanForm.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="kanbanForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="关联项目">
          <el-input v-model="kanbanForm.projectId" />
        </el-form-item>
        <el-form-item label="共享">
          <el-switch v-model="kanbanForm.shared" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showKanbanModal = false">取消</el-button>
        <el-button type="primary" @click="saveKanban()">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { reportAPI } from '../api'

const activeTab = ref('templates')
const templates = ref([])
const kanbans = ref([])
const showTemplateModal = ref(false)
const showKanbanModal = ref(false)

const templateForm = reactive({
  templateId: null,
  name: '',
  type: 'PROJECT',
  description: '',
  status: 'ACTIVE'
})

const kanbanForm = reactive({
  boardId: null,
  name: '',
  description: '',
  projectId: '',
  shared: false
})

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
  templateForm.templateId = row.templateId
  templateForm.name = row.name
  templateForm.type = row.type
  templateForm.description = row.description
  templateForm.status = row.status
  showTemplateModal.value = true
}

const saveTemplate = async () => {
  if (templateForm.templateId) {
    await reportAPI.updateTemplate(templateForm.templateId, templateForm)
  } else {
    await reportAPI.createTemplate(templateForm)
  }
  showTemplateModal.value = false
  loadTemplates()
}

const viewKanban = (row) => {
  kanbanForm.boardId = row.boardId
  kanbanForm.name = row.name
  kanbanForm.description = row.description
  kanbanForm.projectId = row.projectId
  kanbanForm.shared = row.shared
  showKanbanModal.value = true
}

const saveKanban = async () => {
  if (kanbanForm.boardId) {
    await reportAPI.updateKanban(kanbanForm.boardId, kanbanForm)
  } else {
    await reportAPI.createKanban(kanbanForm)
  }
  showKanbanModal.value = false
  loadKanbans()
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
