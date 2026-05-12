
<template>
  <div class="approval-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="审批流程" name="flows">
        <div class="tab-content">
          <el-button type="primary" @click="openFlowModal()">创建流程</el-button>
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

    <!-- 流程弹窗 -->
    <el-dialog :title="flowForm.flowId ? '编辑流程' : '创建流程'" :visible.sync="showFlowModal" width="500px">
      <el-form :model="flowForm" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="flowForm.name" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="flowForm.type">
            <el-option label="立项审批" value="PROJECT_APPROVAL" />
            <el-option label="文档审批" value="DOCUMENT_APPROVAL" />
            <el-option label="采购审批" value="PURCHASE_APPROVAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="flowForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="flowForm.status">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showFlowModal = false">取消</el-button>
        <el-button type="primary" @click="saveFlow()">保存</el-button>
      </div>
    </el-dialog>

    <!-- 步骤管理弹窗 -->
    <el-dialog title="步骤管理" :visible.sync="showStepModal" width="500px">
      <el-form :model="stepForm" label-width="80px">
        <el-form-item label="流程ID" prop="flowId">
          <el-input v-model="stepForm.flowId" disabled />
        </el-form-item>
        <el-form-item label="步骤名称" prop="stepName">
          <el-input v-model="stepForm.stepName" />
        </el-form-item>
        <el-form-item label="审批人" prop="approver">
          <el-input v-model="stepForm.approver" />
        </el-form-item>
        <el-form-item label="步骤顺序" prop="stepOrder">
          <el-input v-model="stepForm.stepOrder" type="number" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="stepForm.stepType">
            <el-option label="审批" value="APPROVAL" />
            <el-option label="抄送" value="CC" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showStepModal = false">取消</el-button>
        <el-button type="primary" @click="saveStep()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from 'vue'
import { approvalAPI } from '../api'

const activeTab = ref('flows')
const flows = ref([])
const tasks = ref([])
const taskStatus = ref('')
const showFlowModal = ref(false)
const showStepModal = ref(false)

const flowForm = reactive({
  flowId: null,
  name: '',
  type: 'PROJECT_APPROVAL',
  description: '',
  status: 'ACTIVE'
})

const stepForm = reactive({
  stepId: null,
  flowId: '',
  stepName: '',
  approver: '',
  stepOrder: 1,
  stepType: 'APPROVAL'
})

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

const openFlowModal = () => {
  flowForm.flowId = null
  flowForm.name = ''
  flowForm.type = 'PROJECT_APPROVAL'
  flowForm.description = ''
  flowForm.status = 'ACTIVE'
  showFlowModal.value = true
}

const saveFlow = async () => {
  if (flowForm.flowId) {
    await approvalAPI.updateFlow(flowForm.flowId, flowForm)
  } else {
    await approvalAPI.createFlow(flowForm)
  }
  showFlowModal.value = false
  loadFlows()
}

const manageSteps = (row) => {
  stepForm.stepId = null
  stepForm.flowId = row.flowId
  stepForm.stepName = ''
  stepForm.approver = ''
  stepForm.stepOrder = 1
  stepForm.stepType = 'APPROVAL'
  showStepModal.value = true
}

const saveStep = async () => {
  if (stepForm.stepId) {
    await approvalAPI.updateStep(stepForm.stepId, stepForm)
  } else {
    await approvalAPI.createStep(stepForm)
  }
  showStepModal.value = false
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
