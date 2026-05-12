
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

    <!-- 组织弹窗 -->
    <el-dialog v-model="showOrgModal" :title="orgForm.orgId ? '编辑组织' : '创建组织'" width="400px">
      <el-form :model="orgForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="orgForm.name" />
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="orgForm.code" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="orgForm.type">
            <el-option label="公司" value="COMPANY" />
            <el-option label="部门" value="DEPARTMENT" />
            <el-option label="团队" value="TEAM" />
          </el-select>
        </el-form-item>
        <el-form-item label="上级组织">
          <el-select v-model="orgForm.parentOrgId">
            <el-option label="无" value="" />
            <el-option v-for="o in organizations" :key="o.orgId" :label="o.name" :value="o.orgId" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="orgForm.status">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showOrgModal = false">取消</el-button>
        <el-button type="primary" @click="saveOrg()">保存</el-button>
      </template>
    </el-dialog>

    <!-- 员工弹窗 -->
    <el-dialog v-model="showEmpModal" :title="empForm.empId ? '编辑员工' : '添加员工'" width="450px">
      <el-form :model="empForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="empForm.name" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="empForm.email" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="empForm.phone" />
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="empForm.jobTitle" />
        </el-form-item>
        <el-form-item label="所属组织">
          <el-select v-model="empForm.orgId">
            <el-option v-for="o in organizations" :key="o.orgId" :label="o.name" :value="o.orgId" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="empForm.status">
            <el-option label="在职" value="ACTIVE" />
            <el-option label="离职" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEmpModal = false">取消</el-button>
        <el-button type="primary" @click="saveEmp()">保存</el-button>
      </template>
    </el-dialog>

    <!-- 角色弹窗 -->
    <el-dialog v-model="showRoleModal" :title="roleForm.roleId ? '编辑角色' : '创建角色'" width="400px">
      <el-form :model="roleForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="roleForm.name" />
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="roleForm.code" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="roleForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="roleForm.status">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRoleModal = false">取消</el-button>
        <el-button type="primary" @click="saveRole()">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from 'vue'
import { organizationAPI } from '../api'

const activeTab = ref('orgs')
const organizations = ref([])
const employees = ref([])
const roles = ref([])
const selectedOrg = ref('')
const showOrgModal = ref(false)
const showEmpModal = ref(false)
const showRoleModal = ref(false)

const orgForm = reactive({
  orgId: null,
  name: '',
  code: '',
  type: 'DEPARTMENT',
  parentOrgId: '',
  status: 'ACTIVE'
})

const empForm = reactive({
  empId: null,
  name: '',
  email: '',
  phone: '',
  jobTitle: '',
  orgId: '',
  status: 'ACTIVE'
})

const roleForm = reactive({
  roleId: null,
  name: '',
  code: '',
  description: '',
  status: 'ACTIVE'
})

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
  orgForm.orgId = row.orgId
  orgForm.name = row.name
  orgForm.code = row.code
  orgForm.type = row.type
  orgForm.parentOrgId = row.parentOrgId
  orgForm.status = row.status
  showOrgModal.value = true
}

const saveOrg = async () => {
  if (orgForm.orgId) {
    await organizationAPI.updateOrganization(orgForm.orgId, orgForm)
  } else {
    await organizationAPI.createOrganization(orgForm)
  }
  showOrgModal.value = false
  loadOrgs()
}

const editEmp = (row) => {
  empForm.empId = row.empId
  empForm.name = row.name
  empForm.email = row.email
  empForm.phone = row.phone
  empForm.jobTitle = row.jobTitle
  empForm.orgId = row.orgId
  empForm.status = row.status
  showEmpModal.value = true
}

const saveEmp = async () => {
  if (empForm.empId) {
    await organizationAPI.updateEmployee(empForm.empId, empForm)
  } else {
    await organizationAPI.createEmployee(empForm)
  }
  showEmpModal.value = false
  loadEmployees()
}

const editRole = (row) => {
  roleForm.roleId = row.roleId
  roleForm.name = row.name
  roleForm.code = row.code
  roleForm.description = row.description
  roleForm.status = row.status
  showRoleModal.value = true
}

const saveRole = async () => {
  if (roleForm.roleId) {
    await organizationAPI.updateRole(roleForm.roleId, roleForm)
  } else {
    await organizationAPI.createRole(roleForm)
  }
  showRoleModal.value = false
  loadRoles()
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
