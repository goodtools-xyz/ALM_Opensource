
<template>
  <div class="hardware-view">
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="硬件项目" name="projects">
        <HardwareProjectList />
      </el-tab-pane>
      <el-tab-pane label="原理图管理" name="schematics">
        <SchematicList />
      </el-tab-pane>
      <el-tab-pane label="PCB设计" name="pcbs">
        <PcbList />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import HardwareProjectList from '../components/HardwareProjectList.vue'
import SchematicList from '../components/SchematicList.vue'
import PcbList from '../components/PcbList.vue'

const activeTab = ref('projects')
</script>

<style scoped>
.hardware-view {
  padding: 20px;
}
</style>
