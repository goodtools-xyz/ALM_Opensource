
import { createRouter, createWebHistory } from 'vue-router'
import HardwareView from '../views/HardwareView.vue'
import StructureView from '../views/StructureView.vue'
import KnowledgeView from '../views/KnowledgeView.vue'
import ReportView from '../views/ReportView.vue'
import ApprovalView from '../views/ApprovalView.vue'
import OrganizationView from '../views/OrganizationView.vue'
import StorageView from '../views/StorageView.vue'
import TemplateView from '../views/TemplateView.vue'

const routes = [
  {
    path: '/',
    redirect: '/hardware'
  },
  {
    path: '/hardware',
    name: 'hardware',
    component: HardwareView
  },
  {
    path: '/structure',
    name: 'structure',
    component: StructureView
  },
  {
    path: '/knowledge',
    name: 'knowledge',
    component: KnowledgeView
  },
  {
    path: '/report',
    name: 'report',
    component: ReportView
  },
  {
    path: '/approval',
    name: 'approval',
    component: ApprovalView
  },
  {
    path: '/organization',
    name: 'organization',
    component: OrganizationView
  },
  {
    path: '/storage',
    name: 'storage',
    component: StorageView
  },
  {
    path: '/template',
    name: 'template',
    component: TemplateView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
