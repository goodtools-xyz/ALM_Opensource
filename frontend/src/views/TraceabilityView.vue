<template>
  <div class="traceability-container">
    <div class="trace-header">
      <h2>双向追溯中心</h2>
      <div class="trace-actions">
        <el-button type="primary" @click="showCreateRelation = true">创建追溯关系</el-button>
        <el-button @click="loadTraceMatrix">刷新</el-button>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="trace-tabs">
      <el-tab-pane label="追溯查询" name="query">
        <div class="query-section">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>搜索项目</span>
                  </div>
                </template>
                <el-input v-model="searchKeyword" placeholder="输入关键词搜索" clearable @clear="loadSearchResults">
                  <template #append>
                    <el-button @click="loadSearchResults">搜索</el-button>
                  </template>
                </el-input>
                <el-radio-group v-model="searchType" class="type-filter">
                  <el-radio label="">全部</el-radio>
                  <el-radio label="REQ">需求</el-radio>
                  <el-radio label="DESIGN">设计</el-radio>
                  <el-radio label="TEST">测试</el-radio>
                  <el-radio label="DEFECT">缺陷</el-radio>
                </el-radio-group>
                <el-divider />
                <div class="search-results">
                  <div v-for="item in searchResults" :key="item.id" class="result-item" @click="selectItem(item)">
                    <el-tag :type="getTypeColor(item.type)">{{ getTypeName(item.type) }}</el-tag>
                    <span class="item-name">{{ item.name }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="16">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>追溯链路</span>
                    <el-tag v-if="selectedItem">{{ getTypeName(selectedItem.type) }}: {{ selectedItem.name }}</el-tag>
                  </div>
                </template>
                <div v-if="selectedItem" class="trace-content">
                  <el-tabs v-model="traceTab">
                    <el-tab-pane label="上游追溯" name="upstream">
                      <el-table :data="upstreamItems" stripe>
                        <el-table-column prop="id" label="ID" width="120" />
                        <el-table-column prop="type" label="类型" width="100">
                          <template #default="{ row }">
                            <el-tag :type="getTypeColor(row.type)" size="small">{{ getTypeName(row.type) }}</el-tag>
                          </template>
                        </el-table-column>
                        <el-table-column prop="relationType" label="关系" />
                        <el-table-column label="操作" width="100">
                          <template #default="{ row }">
                            <el-button type="primary" link @click="viewItemDetail(row)">查看</el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                      <el-empty v-if="upstreamItems.length === 0" description="无上游追溯数据" />
                    </el-tab-pane>
                    <el-tab-pane label="下游追溯" name="downstream">
                      <el-table :data="downstreamItems" stripe>
                        <el-table-column prop="id" label="ID" width="120" />
                        <el-table-column prop="type" label="类型" width="100">
                          <template #default="{ row }">
                            <el-tag :type="getTypeColor(row.type)" size="small">{{ getTypeName(row.type) }}</el-tag>
                          </template>
                        </el-table-column>
                        <el-table-column prop="relationType" label="关系" />
                        <el-table-column label="操作" width="100">
                          <template #default="{ row }">
                            <el-button type="primary" link @click="viewItemDetail(row)">查看</el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                      <el-empty v-if="downstreamItems.length === 0" description="无下游追溯数据" />
                    </el-tab-pane>
                    <el-tab-pane label="影响分析" name="impact">
                      <el-descriptions :column="2" border>
                        <el-descriptions-item label="项目ID">{{ impactData.itemId }}</el-descriptions-item>
                        <el-descriptions-item label="影响项数量">{{ impactData.impactCount }}</el-descriptions-item>
                      </el-descriptions>
                      <el-divider content-position="left">受影响项目</el-divider>
                      <div class="impact-list">
                        <el-tag v-for="item in impactData.impactedItems" :key="item" class="impact-tag">{{ item }}</el-tag>
                      </div>
                    </el-tab-pane>
                  </el-tabs>
                </div>
                <el-empty v-else description="请先选择一个项目" />
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>

      <el-tab-pane label="追溯矩阵" name="matrix">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>追溯矩阵视图</span>
              <el-button type="success" @click="exportMatrix">导出矩阵</el-button>
            </div>
          </template>
          <el-table :data="traceMatrix" stripe>
            <el-table-column prop="reqId" label="需求ID" width="120" />
            <el-table-column prop="reqName" label="需求名称" />
            <el-table-column label="设计" width="150">
              <template #default="{ row }">
                <span v-if="row.designs && row.designs.length > 0">
                  {{ row.designs.length }} 项
                </span>
                <span v-else class="text-muted">-</span>
              </template>
            </el-table-column>
            <el-table-column label="测试" width="150">
              <template #default="{ row }">
                <span v-if="row.tests && row.tests.length > 0">
                  {{ row.tests.length }} 项
                </span>
                <span v-else class="text-muted">-</span>
              </template>
            </el-table-column>
            <el-table-column label="缺陷" width="150">
              <template #default="{ row }">
                <span v-if="row.defects && row.defects.length > 0">
                  {{ row.defects.length }} 项
                </span>
                <span v-else class="text-muted">-</span>
              </template>
            </el-table-column>
            <el-table-column label="追溯率" width="120">
              <template #default="{ row }">
                <el-progress :percentage="calculateTraceRate(row)" />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="showCreateRelation" title="创建追溯关系" width="500px">
      <el-form :model="relationForm" label-width="100px">
        <el-form-item label="源类型">
          <el-select v-model="relationForm.sourceType" placeholder="选择源类型">
            <el-option label="需求 (REQ)" value="REQ" />
            <el-option label="设计 (DESIGN)" value="DESIGN" />
            <el-option label="测试 (TEST)" value="TEST" />
            <el-option label="缺陷 (DEFECT)" value="DEFECT" />
          </el-select>
        </el-form-item>
        <el-form-item label="源ID">
          <el-input v-model="relationForm.sourceId" placeholder="输入源项目ID" />
        </el-form-item>
        <el-form-item label="目标类型">
          <el-select v-model="relationForm.targetType" placeholder="选择目标类型">
            <el-option label="需求 (REQ)" value="REQ" />
            <el-option label="设计 (DESIGN)" value="DESIGN" />
            <el-option label="测试 (TEST)" value="TEST" />
            <el-option label="缺陷 (DEFECT)" value="DEFECT" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标ID">
          <el-input v-model="relationForm.targetId" placeholder="输入目标项目ID" />
        </el-form-item>
        <el-form-item label="关系类型">
          <el-select v-model="relationForm.relationType" placeholder="选择关系类型">
            <el-option label="实现" value="IMPLEMENTS" />
            <el-option label="验证" value="VERIFIES" />
            <el-option label="追踪" value="TRACES" />
            <el-option label="解决" value="RESOLVES" />
            <el-option label="满足" value="SATISFIES" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateRelation = false">取消</el-button>
        <el-button type="primary" @click="handleCreateRelation">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { traceabilityAPI } from '../api'
import { ElMessage } from 'element-plus'

const activeTab = ref('query')
const traceTab = ref('upstream')
const showCreateRelation = ref(false)

const searchKeyword = ref('')
const searchType = ref('')
const searchResults = ref([])
const selectedItem = ref(null)
const upstreamItems = ref([])
const downstreamItems = ref([])
const impactData = ref({ itemId: '', impactCount: 0, impactedItems: [] })
const traceMatrix = ref([])

const relationForm = reactive({
  sourceType: '',
  sourceId: '',
  targetType: '',
  targetId: '',
  relationType: ''
})

const loadSearchResults = async () => {
  try {
    const response = await traceabilityAPI.searchItems(searchKeyword.value, searchType.value)
    searchResults.value = response.data || []
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败')
  }
}

const selectItem = async (item) => {
  selectedItem.value = item
  await loadTraceData(item)
}

const loadTraceData = async (item) => {
  try {
    let response
    if (item.type === 'REQ') {
      response = await traceabilityAPI.getRequirementTrace(item.id)
      upstreamItems.value = response.data.upstream || []
      downstreamItems.value = response.data.downstream || []
    } else if (item.type === 'DESIGN') {
      response = await traceabilityAPI.getDesignTrace(item.id)
      upstreamItems.value = response.data.upstream || []
      downstreamItems.value = response.data.downstream || []
    } else if (item.type === 'TEST') {
      response = await traceabilityAPI.getTestTrace(item.id)
      upstreamItems.value = response.data.upstream || []
      downstreamItems.value = response.data.downstream || []
    }

    const impactResponse = await traceabilityAPI.getImpactAnalysis(item.id)
    impactData.value = impactResponse.data || { itemId: item.id, impactCount: 0, impactedItems: [] }
  } catch (error) {
    console.error('加载追溯数据失败:', error)
    ElMessage.error('加载追溯数据失败')
  }
}

const loadTraceMatrix = async () => {
  try {
    const response = await traceabilityAPI.getTraceMatrix('')
    traceMatrix.value = response.data || []
  } catch (error) {
    console.error('加载追溯矩阵失败:', error)
    ElMessage.error('加载追溯矩阵失败')
  }
}

const handleCreateRelation = async () => {
  try {
    await traceabilityAPI.createRelation(relationForm)
    ElMessage.success('创建追溯关系成功')
    showCreateRelation.value = false
    Object.keys(relationForm).forEach(key => relationForm[key] = '')
    if (selectedItem.value) {
      await loadTraceData(selectedItem.value)
    }
    await loadTraceMatrix()
  } catch (error) {
    console.error('创建追溯关系失败:', error)
    ElMessage.error('创建追溯关系失败')
  }
}

const viewItemDetail = (item) => {
  selectItem({ id: item.id, type: item.type, name: item.id })
}

const exportMatrix = () => {
  ElMessage.info('导出功能开发中')
}

const getTypeColor = (type) => {
  const colors = { REQ: 'primary', DESIGN: 'success', TEST: 'warning', DEFECT: 'danger' }
  return colors[type] || 'info'
}

const getTypeName = (type) => {
  const names = { REQ: '需求', DESIGN: '设计', TEST: '测试', DEFECT: '缺陷' }
  return names[type] || type
}

const calculateTraceRate = (row) => {
  const total = (row.designs?.length || 0) + (row.tests?.length || 0) + (row.defects?.length || 0)
  if (total === 0) return 0
  return Math.round((total / (total + 1)) * 100)
}

onMounted(() => {
  loadSearchResults()
  loadTraceMatrix()
})
</script>

<style scoped>
.traceability-container {
  padding: 20px;
}

.trace-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.trace-header h2 {
  margin: 0;
  font-size: 24px;
}

.trace-actions {
  display: flex;
  gap: 10px;
}

.query-section {
  min-height: 500px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.type-filter {
  margin-top: 15px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.search-results {
  max-height: 300px;
  overflow-y: auto;
}

.result-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px;
  cursor: pointer;
  border-radius: 4px;
}

.result-item:hover {
  background-color: #f5f7fa;
}

.item-name {
  font-size: 14px;
}

.trace-content {
  min-height: 400px;
}

.impact-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.impact-tag {
  margin: 5px;
}

.text-muted {
  color: #909399;
}

:deep(.el-tabs__content) {
  padding: 20px 0;
}
</style>