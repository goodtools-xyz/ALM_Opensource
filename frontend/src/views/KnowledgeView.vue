
<template>
  <div class="knowledge-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="空间管理" name="spaces">
        <div class="tab-content">
          <el-button type="primary" @click="openSpaceModal()">创建空间</el-button>
          <el-table :data="spaces" border style="width: 100%;">
            <el-table-column prop="spaceId" label="空间ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="spaceKey" label="标识" width="100" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="editSpace(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteSpace(scope.row.spaceId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="页面管理" name="pages">
        <div class="tab-content">
          <el-select v-model="selectedSpace" placeholder="选择空间">
            <el-option label="全部" value="" />
            <el-option v-for="s in spaces" :key="s.spaceId" :label="s.name" :value="s.spaceId" />
          </el-select>
          <el-button type="primary" @click="openPageModal()">创建页面</el-button>
          <el-table :data="pages" border style="width: 100%;">
            <el-table-column prop="pageId" label="页面ID" width="120" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="contentType" label="类型" width="100" />
            <el-table-column prop="version" label="版本" width="80" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="viewPage(scope.row)">查看</el-button>
                <el-button size="small" @click="editPage(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deletePage(scope.row.pageId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 空间弹窗 -->
    <el-dialog :title="spaceForm.spaceId ? '编辑空间' : '创建空间'" :visible.sync="showSpaceModal" width="400px">
      <el-form :model="spaceForm" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="spaceForm.name" />
        </el-form-item>
        <el-form-item label="标识" prop="spaceKey">
          <el-input v-model="spaceForm.spaceKey" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="spaceForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="spaceForm.status">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showSpaceModal = false">取消</el-button>
        <el-button type="primary" @click="saveSpace()">保存</el-button>
      </div>
    </el-dialog>

    <!-- 页面弹窗 -->
    <el-dialog :title="pageForm.pageId ? '编辑页面' : '创建页面'" :visible.sync="showPageModal" width="500px">
      <el-form :model="pageForm" label-width="80px">
        <el-form-item label="所属空间" prop="spaceId">
          <el-select v-model="pageForm.spaceId">
            <el-option v-for="s in spaces" :key="s.spaceId" :label="s.name" :value="s.spaceId" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="pageForm.title" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="pageForm.contentType">
            <el-option label="文档" value="DOCUMENT" />
            <el-option label="Markdown" value="MARKDOWN" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="pageForm.content" type="textarea" :rows="5" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="pageForm.status">
            <el-option label="发布" value="PUBLISHED" />
            <el-option label="草稿" value="DRAFT" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showPageModal = false">取消</el-button>
        <el-button type="primary" @click="savePage()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from 'vue'
import { knowledgeAPI } from '../api'

const activeTab = ref('spaces')
const spaces = ref([])
const pages = ref([])
const selectedSpace = ref('')
const showSpaceModal = ref(false)
const showPageModal = ref(false)

const spaceForm = reactive({
  spaceId: null,
  name: '',
  spaceKey: '',
  description: '',
  status: 'ACTIVE'
})

const pageForm = reactive({
  pageId: null,
  spaceId: '',
  title: '',
  contentType: 'DOCUMENT',
  content: '',
  status: 'DRAFT'
})

const loadSpaces = async () => {
  const response = await knowledgeAPI.getSpaces()
  spaces.value = response.data
}

const loadPages = async () => {
  if (selectedSpace.value) {
    const response = await knowledgeAPI.getPages(selectedSpace.value)
    pages.value = response.data
  } else {
    pages.value = []
  }
}

const deleteSpace = async (id) => {
  await knowledgeAPI.deleteSpace(id)
  loadSpaces()
}

const deletePage = async (id) => {
  await knowledgeAPI.deletePage(id)
  loadPages()
}

const openSpaceModal = () => {
  spaceForm.spaceId = null
  spaceForm.name = ''
  spaceForm.spaceKey = ''
  spaceForm.description = ''
  spaceForm.status = 'ACTIVE'
  showSpaceModal.value = true
}

const editSpace = (row) => {
  spaceForm.spaceId = row.spaceId
  spaceForm.name = row.name
  spaceForm.spaceKey = row.spaceKey
  spaceForm.description = row.description
  spaceForm.status = row.status
  showSpaceModal.value = true
}

const saveSpace = async () => {
  if (spaceForm.spaceId) {
    await knowledgeAPI.updateSpace(spaceForm.spaceId, spaceForm)
  } else {
    await knowledgeAPI.createSpace(spaceForm)
  }
  showSpaceModal.value = false
  loadSpaces()
}

const openPageModal = () => {
  pageForm.pageId = null
  pageForm.spaceId = selectedSpace.value || ''
  pageForm.title = ''
  pageForm.contentType = 'DOCUMENT'
  pageForm.content = ''
  pageForm.status = 'DRAFT'
  showPageModal.value = true
}

const editPage = (row) => {
  pageForm.pageId = row.pageId
  pageForm.spaceId = row.spaceId
  pageForm.title = row.title
  pageForm.contentType = row.contentType
  pageForm.content = row.content
  pageForm.status = row.status
  showPageModal.value = true
}

const viewPage = (row) => {
  pageForm.pageId = row.pageId
  pageForm.spaceId = row.spaceId
  pageForm.title = row.title
  pageForm.contentType = row.contentType
  pageForm.content = row.content
  pageForm.status = row.status
  showPageModal.value = true
}

const savePage = async () => {
  if (pageForm.pageId) {
    await knowledgeAPI.updatePage(pageForm.pageId, pageForm)
  } else {
    await knowledgeAPI.createPage(pageForm)
  }
  showPageModal.value = false
  loadPages()
}

watch(selectedSpace, () => {
  loadPages()
})

onMounted(() => {
  loadSpaces()
})
</script>

<style scoped>
.knowledge-view {
  padding: 10px;
  height: 100%;
}

.tab-content {
  margin-top: 10px;
}
</style>
