
<template>
  <div class="knowledge-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="空间管理" name="spaces">
        <div class="tab-content">
          <el-button type="primary" @click="showSpaceModal = true">创建空间</el-button>
          <el-table :data="spaces" border style="width: 100%;">
            <el-table-column prop="spaceId" label="空间ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="key" label="标识" width="100" />
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
          <el-button type="primary" @click="showPageModal = true">创建页面</el-button>
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
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { knowledgeAPI } from '../api'

const activeTab = ref('spaces')
const spaces = ref([])
const pages = ref([])
const selectedSpace = ref('')
const showSpaceModal = ref(false)
const showPageModal = ref(false)

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

const editSpace = (row) => {
  showSpaceModal.value = true
}

const editPage = (row) => {
  showPageModal.value = true
}

const viewPage = (row) => {
  showPageModal.value = true
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
