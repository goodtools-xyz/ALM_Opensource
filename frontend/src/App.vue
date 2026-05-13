<template>
  <el-container style="height: 100vh;">
    <el-header class="header">
      <div class="logo">ALM系统</div>
      <div class="header-right">
        <span class="user-info">
          <el-icon><component :is="icons.User" /></el-icon>
          {{ currentUser?.username }} ({{ currentUser?.role === 'admin' ? '管理员' : '普通用户' }})
        </span>
        <el-button type="text" @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="220px" class="aside">
        <el-menu :default-active="activeMenu" mode="vertical" @select="handleMenuSelect">
          <el-sub-menu index="engineering">
            <template #title>
              <el-icon><component :is="icons.Wrench" /></el-icon>
              <span>工程管理</span>
            </template>
            <el-menu-item index="hardware">
              <el-icon><component :is="icons.Cpu" /></el-icon>
              <span>硬件电子管理</span>
            </el-menu-item>
            <el-menu-item index="structure">
              <el-icon><component :is="icons.Box" /></el-icon>
              <span>结构设计管理</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="rd-management">
            <template #title>
              <el-icon><component :is="icons.BookOpen" /></el-icon>
              <span>研发管理</span>
            </template>
            <el-menu-item index="requirement">
              <el-icon><component :is="icons.FileText" /></el-icon>
              <span>需求管理</span>
            </el-menu-item>
            <el-menu-item index="design">
              <el-icon><component :is="icons.Pencil" /></el-icon>
              <span>设计管理</span>
            </el-menu-item>
            <el-menu-item index="testcase">
              <el-icon><component :is="icons.CheckCircle" /></el-icon>
              <span>测试用例</span>
            </el-menu-item>
            <el-menu-item index="defect">
              <el-icon><component :is="icons.AlertCircle" /></el-icon>
              <span>缺陷管理</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="knowledge-group">
            <template #title>
              <el-icon><component :is="icons.BookOpen" /></el-icon>
              <span>知识管理</span>
            </template>
            <el-menu-item index="knowledge">
              <el-icon><component :is="icons.FileText" /></el-icon>
              <span>知识库</span>
            </el-menu-item>
            <el-menu-item index="storage">
              <el-icon><component :is="icons.FolderOpen" /></el-icon>
              <span>文档管理</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="workflow">
            <template #title>
              <el-icon><component :is="icons.FlowChart" /></el-icon>
              <span>工作流程</span>
            </template>
            <el-menu-item index="approval">
              <el-icon><component :is="icons.CheckSquare" /></el-icon>
              <span>审批流程</span>
            </el-menu-item>
            <el-menu-item index="traceability">
              <el-icon><component :is="icons.Connection" /></el-icon>
              <span>双向追溯</span>
            </el-menu-item>
            <el-menu-item index="template" v-if="isAdmin">
              <el-icon><component :is="icons.Template" /></el-icon>
              <span>任务模板</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="management" v-if="isAdmin">
            <template #title>
              <el-icon><component :is="icons.User" /></el-icon>
              <span>组织管理</span>
            </template>
            <el-menu-item index="organization">
              <el-icon><component :is="icons.Building" /></el-icon>
              <span>组织架构</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="analytics" v-if="isAdmin">
            <template #title>
              <el-icon><component :is="icons.BarChart" /></el-icon>
              <span>数据分析</span>
            </template>
            <el-menu-item index="report">
              <el-icon><component :is="icons.PieChart" /></el-icon>
              <span>数据报表</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, markRaw, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Cpu, Box, Notebook, Files, Folder, DataLine, Checked, CopyDocument, User, Briefcase, TrendCharts, PieChart, Edit, Connection, EditPen, CircleCheck, Warning } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const icons = {
  Cpu: markRaw(Cpu),
  Box: markRaw(Box),
  BookOpen: markRaw(Notebook),
  FileText: markRaw(Files),
  FolderOpen: markRaw(Folder),
  FlowChart: markRaw(DataLine),
  CheckSquare: markRaw(Checked),
  Template: markRaw(CopyDocument),
  User: markRaw(User),
  Building: markRaw(Briefcase),
  BarChart: markRaw(TrendCharts),
  PieChart: markRaw(PieChart),
  Wrench: markRaw(Edit),
  Connection: markRaw(Connection),
  Pencil: markRaw(EditPen),
  CheckCircle: markRaw(CircleCheck),
  AlertCircle: markRaw(Warning)
}

const activeMenu = ref('hardware')
const currentUser = ref(null)

const isAdmin = computed(() => {
  return currentUser.value?.role === 'admin'
})

const handleMenuSelect = (index) => {
  activeMenu.value = index
  router.push('/' + index)
}

const handleLogout = () => {
  localStorage.removeItem('userInfo')
  router.push('/login')
}

const loadUserInfo = () => {
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    try {
      currentUser.value = JSON.parse(userInfo)
    } catch (e) {
      console.error('解析用户信息失败:', e)
    }
  }
}

onMounted(() => {
  loadUserInfo()
  activeMenu.value = route.name || 'hardware'
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.header {
  background-color: #1f2937;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.aside {
  background-color: #374151;
}

.el-menu {
  border-right: none;
}

.main {
  background-color: #f3f4f6;
}
</style>