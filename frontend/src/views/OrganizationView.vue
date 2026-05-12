
<template>
  <div class="org-view">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="组织架构" name="orgs">
        <div class="tab-content">
          <el-button type="primary" @click="showOrgModal = true">创建组织</el-button>
          <el-table :data="organizations" border style="width: 100%;">
            <el-table-column prop="orgId" label="组织ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="code" label="编码" width="100" />
            <el-table-column prop="type" label="类型" width="100" />
            <el-table-column prop="parentOrgId" label="上级组织" width="120" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="editOrg(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteOrg(scope.row.orgId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="员工管理" name="employees">
        <div class="tab-content">
          <el-select v-model="selectedOrg" placeholder="选择组织">
            <el-option label="全部" value="" />
            <el-option v-for="o in organizations" :key="o.orgId" :label="o.name" :value="o.orgId" />
          </el-select>
          <el-button type="primary" @click="showEmpModal = true">添加员工</el-button>
          <el-table :data="employees" border style="width: 100%;">
            <el-table-column prop="empId" label="员工ID" width="120" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="phone" label="电话" width="120" />
            <el-table-column prop="jobTitle" label="职位" width="120" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="editEmp(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteEmp(scope.row.empId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="角色管理" name="roles">
        <div class="tab-content">
          <el-button type="primary" @click="showRoleModal = true">创建角色</el-button>
          <el-table :data="roles" border style="width: 100%;">
            <el-table-column prop="roleId" label="角色ID" width="120" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="code" label="编码" width="100" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="editRole(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteRole(scope.row.roleId)">删除</el-button>
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
import { organizationAPI } from '../api'

const activeTab = ref('orgs')
const organizations = ref([])
const employees = ref([])
const roles = ref([])
const selectedOrg = ref('')
const showOrgModal = ref(false)
const showEmpModal = ref(false)
const showRoleModal = ref(false)

const loadOrgs = async () => {
  const response = await organizationAPI.getOrganizations()
  organizations.value = response.data
}

const loadEmployees = async () => {
  if (selectedOrg.value) {
    const response = await organizationAPI.getEmployeesByOrg(selectedOrg.value)
    employees.value = response.data
  } else {
    const response = await organizationAPI.getEmployees()
    employees.value = response.data
  }
}

const loadRoles = async () => {
  const response = await organizationAPI.getRoles()
  roles.value = response.data
}

const deleteOrg = async (id) => {
  await organizationAPI.deleteOrganization(id)
  loadOrgs()
}

const deleteEmp = async (id) => {
  await organizationAPI.deleteEmployee(id)
  loadEmployees()
}

const deleteRole = async (id) => {
  await organizationAPI.deleteRole(id)
  loadRoles()
}

const editOrg = (row) => {
  showOrgModal.value = true
}

const editEmp = (row) => {
  showEmpModal.value = true
}

const editRole = (row) => {
  showRoleModal.value = true
}

watch(selectedOrg, () => {
  loadEmployees()
})

onMounted(() => {
  loadOrgs()
  loadEmployees()
  loadRoles()
})
</script>

<style scoped>
.org-view {
  padding: 10px;
  height: 100%;
}

.tab-content {
  margin-top: 10px;
}
</style>
