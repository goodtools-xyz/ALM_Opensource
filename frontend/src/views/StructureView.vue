
<template>
  <div class="structure-view">
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="结构项目" name="projects">
        <StructureProjectList />
      </el-tab-pane>
      <el-tab-pane label="3D模型" name="3dmodels">
        <Model3dList />
      </el-tab-pane>
      <el-tab-pane label="2D图纸" name="2ddrawings">
        <Drawing2dList />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import StructureProjectList from '../components/StructureProjectList.vue'
import Model3dList from '../components/Model3dList.vue'
import Drawing2dList from '../components/Drawing2dList.vue'

const activeTab = ref('projects')
</script>

<style scoped>
.structure-view {
  padding: 20px;
}
</style>
