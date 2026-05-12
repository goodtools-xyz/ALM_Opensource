
<template>
  <div class="approval-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="审批流程" name="flows">
        <div class="tab-content">
          <el-button type="primary" @click="showFlowModal = true">创建流程</el-button>
          <el-table :data="flows" border style="width: 100%;">
            <el-table-column prop="flowId" label="流程ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="type" label="类型" width="100" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="manageSteps(scope.row)">步骤管理</el-button>
                <el-button size="small" type="danger" @click="deleteFlow(scope.row.flowId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="审批任务" name="tasks">
        <div class="tab-content">
          <el-select v-model="taskStatus" placeholder="状态筛选">
            <el-option label="全部" value="" />
            <el-option label="待审批" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
          <el-table :data="tasks" border style="width: 100%;">
            <el-table-column prop="taskId" label="任务ID" width="120" />
            <el-table-column prop="documentId" label="文档ID" width="120" />
            <el-table-column prop="documentType" label="文档类型" width="120" />
            <el-table-column prop="currentStep" label="当前步骤" width="120" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="applicant" label="申请人" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button v-if="scope.row.status === 'PENDING'" size="small" type="success" @click="approveTask(scope.row.taskId)">通过</el-button>
                <el-button v-if="scope.row.status === 'PENDING'" size="small" type="danger" @click="rejectTask(scope.row.taskId)">拒绝</el-button>
                <el-button size="small" @click="viewTask(scope.row)">详情</el-button>
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
import { approvalAPI } from '../api'

const activeTab = ref('flows')
const flows = ref([])
const tasks = ref([])
const taskStatus = ref('')
const showFlowModal = ref(false)

const loadFlows = async () => {
  const response = await approvalAPI.getFlows()
  flows.value = response.data
}

const loadTasks = async () => {
  if (taskStatus.value) {
    const response = await approvalAPI.getTasksByStatus(taskStatus.value)
    tasks.value = response.data
  } else {
    tasks.value = []
  }
}

const deleteFlow = async (id) => {
  await approvalAPI.deleteFlow(id)
  loadFlows()
}

const approveTask = async (id) => {
  await approvalAPI.approveTask(id, { approver: 'admin', comment: '同意' })
  loadTasks()
}

const rejectTask = async (id) => {
  await approvalAPI.rejectTask(id, { approver: 'admin', comment: '拒绝' })
  loadTasks()
}

const manageSteps = (row) => {
  showFlowModal.value = true
}

const viewTask = (row) => {
  showFlowModal.value = true
}

watch(taskStatus, () => {
  loadTasks()
})

onMounted(() => {
  loadFlows()
})
</script>

<style scoped>
.approval-view {
  padding: 10px;
  height: 100%;
}

.tab-content {
  margin-top: 10px;
}
</style>
