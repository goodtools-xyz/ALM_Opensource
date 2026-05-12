
<template>
  <div class="template-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="任务模板" name="templates">
        <div class="tab-content">
          <el-button type="primary" @click="openTemplateModal()">创建模板</el-button>
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
          <el-button type="primary" @click="openFieldModal()">添加字段</el-button>
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

    <!-- 模板弹窗 -->
    <el-dialog v-model="showTemplateModal" :title="templateForm.templateId ? '编辑模板' : '创建模板'" width="450px">
      <el-form :model="templateForm" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="templateForm.name" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="templateForm.category">
            <el-option label="硬件" value="HARDWARE" />
            <el-option label="结构" value="STRUCTURE" />
            <el-option label="文档" value="DOCUMENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目类型" prop="projectType">
          <el-select v-model="templateForm.projectType">
            <el-option label="新项目" value="NEW_PROJECT" />
            <el-option label="迭代" value="ITERATION" />
            <el-option label="变更" value="CHANGE" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
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

    <!-- 字段弹窗 -->
    <el-dialog v-model="showFieldModal" :title="fieldForm.fieldId ? '编辑字段' : '添加字段'" width="450px">
      <el-form :model="fieldForm" label-width="80px">
        <el-form-item label="所属模板" prop="templateId">
          <el-select v-model="fieldForm.templateId">
            <el-option v-for="t in templates" :key="t.templateId" :label="t.name" :value="t.templateId" />
          </el-select>
        </el-form-item>
        <el-form-item label="字段名" prop="fieldName">
          <el-input v-model="fieldForm.fieldName" />
        </el-form-item>
        <el-form-item label="显示名称" prop="fieldLabel">
          <el-input v-model="fieldForm.fieldLabel" />
        </el-form-item>
        <el-form-item label="类型" prop="fieldType">
          <el-select v-model="fieldForm.fieldType">
            <el-option label="文本" value="TEXT" />
            <el-option label="数字" value="NUMBER" />
            <el-option label="日期" value="DATE" />
            <el-option label="选择" value="SELECT" />
            <el-option label="多行文本" value="TEXTAREA" />
          </el-select>
        </el-form-item>
        <el-form-item label="必填">
          <el-switch v-model="fieldForm.required" />
        </el-form-item>
        <el-form-item label="默认值" prop="defaultValue">
          <el-input v-model="fieldForm.defaultValue" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showFieldModal = false">取消</el-button>
        <el-button type="primary" @click="saveField()">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from 'vue'
import { templateAPI } from '../api'

const activeTab = ref('templates')
const templates = ref([])
const fields = ref([])
const selectedTemplate = ref('')
const showTemplateModal = ref(false)
const showFieldModal = ref(false)

const templateForm = reactive({
  templateId: null,
  name: '',
  category: 'HARDWARE',
  projectType: 'NEW_PROJECT',
  description: '',
  status: 'ACTIVE'
})

const fieldForm = reactive({
  fieldId: null,
  templateId: '',
  fieldName: '',
  fieldLabel: '',
  fieldType: 'TEXT',
  required: false,
  defaultValue: ''
})

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

const openTemplateModal = () => {
  templateForm.templateId = null
  templateForm.name = ''
  templateForm.category = 'HARDWARE'
  templateForm.projectType = 'NEW_PROJECT'
  templateForm.description = ''
  templateForm.status = 'ACTIVE'
  showTemplateModal.value = true
}

const editTemplate = (row) => {
  templateForm.templateId = row.templateId
  templateForm.name = row.name
  templateForm.category = row.category
  templateForm.projectType = row.projectType
  templateForm.description = row.description
  templateForm.status = row.status
  showTemplateModal.value = true
}

const saveTemplate = async () => {
  if (templateForm.templateId) {
    await templateAPI.updateTemplate(templateForm.templateId, templateForm)
  } else {
    await templateAPI.createTemplate(templateForm)
  }
  showTemplateModal.value = false
  loadTemplates()
}

const openFieldModal = () => {
  fieldForm.fieldId = null
  fieldForm.templateId = selectedTemplate.value || ''
  fieldForm.fieldName = ''
  fieldForm.fieldLabel = ''
  fieldForm.fieldType = 'TEXT'
  fieldForm.required = false
  fieldForm.defaultValue = ''
  showFieldModal.value = true
}

const editField = (row) => {
  fieldForm.fieldId = row.fieldId
  fieldForm.templateId = row.templateId
  fieldForm.fieldName = row.fieldName
  fieldForm.fieldLabel = row.fieldLabel
  fieldForm.fieldType = row.fieldType
  fieldForm.required = row.required
  fieldForm.defaultValue = row.defaultValue
  showFieldModal.value = true
}

const saveField = async () => {
  if (!fieldForm.templateId) {
    alert('请先选择所属模板')
    return
  }
  if (!fieldForm.fieldName) {
    alert('请输入字段名')
    return
  }
  try {
    if (fieldForm.fieldId) {
      await templateAPI.updateField(fieldForm.fieldId, fieldForm)
    } else {
      await templateAPI.createField(fieldForm)
    }
    showFieldModal.value = false
    loadFields()
  } catch (error) {
    console.error('保存字段失败:', error)
    alert('保存字段失败，请检查网络连接')
  }
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
