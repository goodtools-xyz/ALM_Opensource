
<template>
  <el-container style="height: 100vh;">
    <el-header class="header">
      <div class="logo">ALM系统</div>
      <div class="header-right">
        <span>应用生命周期管理</span>
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
          
          <el-sub-menu index="knowledge">
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
            <el-menu-item index="template">
              <el-icon><component :is="icons.Template" /></el-icon>
              <span>任务模板</span>
            </el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="management">
            <template #title>
              <el-icon><component :is="icons.User" /></el-icon>
              <span>组织管理</span>
            </template>
            <el-menu-item index="organization">
              <el-icon><component :is="icons.Building" /></el-icon>
              <span>组织架构</span>
            </el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="analytics">
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
import { ref, markRaw } from 'vue'
import { Cpu, Box, BookOpen, FileText, FolderOpen, FlowChart, CheckSquare, Template, User, Building, BarChart, PieChart, Wrench } from '@element-plus/icons-vue'

const icons = {
  Cpu: markRaw(Cpu),
  Box: markRaw(Box),
  BookOpen: markRaw(BookOpen),
  FileText: markRaw(FileText),
  FolderOpen: markRaw(FolderOpen),
  FlowChart: markRaw(FlowChart),
  CheckSquare: markRaw(CheckSquare),
  Template: markRaw(Template),
  User: markRaw(User),
  Building: markRaw(Building),
  BarChart: markRaw(BarChart),
  PieChart: markRaw(PieChart),
  Wrench: markRaw(Wrench)
}

const activeMenu = ref('hardware')

const handleMenuSelect = (index) => {
  activeMenu.value = index
}
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
