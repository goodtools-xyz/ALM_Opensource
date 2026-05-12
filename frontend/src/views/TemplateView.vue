
<template>
  <div class="template-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="任务模板" name="templates">
        <div class="tab-content">
          <el-button type="primary" @click="showTemplateModal = true">创建模板</el-button>
          <el-table :data="templates" border style="width: 100%;">
            <el-table-column prop="templateId" label="模板ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="category" label="分类" width="100" />
            <el-table-column prop="projectType" label="项目类型" width="120" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="manageFields(scope.row)">字段管理</el-button>
                <el-button size="small" @click="editTemplate(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteTemplate(scope.row.templateId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="模板字段" name="fields">
        <div class="tab-content">
          <el-select v-model="selectedTemplate" placeholder="选择模板">
            <el-option label="全部" value="" />
            <el-option v-for="t in templates" :key="t.templateId" :label="t.name" :value="t.templateId" />
          </el-select>
          <el-button type="primary" @click="showFieldModal = true">添加字段</el-button>
          <el-table :data="fields" border style="width: 100%;">
            <el-table-column prop="fieldId" label="字段ID" width="120" />
            <el-table-column prop="fieldName" label="字段名" />
            <el-table-column prop="fieldLabel" label="显示名称" />
            <el-table-column prop="fieldType" label="类型" width="100" />
            <el-table-column prop="required" label="必填" width="80" />
            <el-table-column prop="defaultValue" label="默认值" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="editField(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteField(scope.row.fieldId)">删除</el-button>
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
import { templateAPI } from '../api'

const activeTab = ref('templates')
const templates = ref([])
const fields = ref([])
const selectedTemplate = ref('')
const showTemplateModal = ref(false)
const showFieldModal = ref(false)

const loadTemplates = async () => {
  const response = await templateAPI.getTemplates()
  templates.value = response.data
}

const loadFields = async () => {
  if (selectedTemplate.value) {
    const response = await templateAPI.getFields(selectedTemplate.value)
    fields.value = response.data
  } else {
    fields.value = []
  }
}

const deleteTemplate = async (id) => {
  await templateAPI.deleteTemplate(id)
  loadTemplates()
}

const deleteField = async (id) => {
  await templateAPI.deleteField(id)
  loadFields()
}

const manageFields = (row) => {
  selectedTemplate.value = row.templateId
  activeTab.value = 'fields'
}

const editTemplate = (row) => {
  showTemplateModal.value = true
}

const editField = (row) => {
  showFieldModal.value = true
}

watch(selectedTemplate, () => {
  loadFields()
})

onMounted(() => {
  loadTemplates()
})
</script>

<style scoped>
.template-view {
  padding: 10px;
  height: 100%;
}

.tab-content {
  margin-top: 10px;
}
</style>
