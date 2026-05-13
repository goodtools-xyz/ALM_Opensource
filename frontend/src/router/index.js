import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import HardwareView from '../views/HardwareView.vue'
import StructureView from '../views/StructureView.vue'
import KnowledgeView from '../views/KnowledgeView.vue'
import ReportView from '../views/ReportView.vue'
import ApprovalView from '../views/ApprovalView.vue'
import OrganizationView from '../views/OrganizationView.vue'
import StorageView from '../views/StorageView.vue'
import TemplateView from '../views/TemplateView.vue'
import TraceabilityView from '../views/TraceabilityView.vue'
import RequirementView from '../views/RequirementView.vue'
import DesignView from '../views/DesignView.vue'
import TestCaseView from '../views/TestCaseView.vue'
import DefectView from '../views/DefectView.vue'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/hardware',
    name: 'hardware',
    component: HardwareView,
    meta: { requiresAuth: true }
  },
  {
    path: '/structure',
    name: 'structure',
    component: StructureView,
    meta: { requiresAuth: true }
  },
  {
    path: '/knowledge',
    name: 'knowledge',
    component: KnowledgeView,
    meta: { requiresAuth: true }
  },
  {
    path: '/report',
    name: 'report',
    component: ReportView,
    meta: { requiresAuth: true, adminOnly: true }
  },
  {
    path: '/approval',
    name: 'approval',
    component: ApprovalView,
    meta: { requiresAuth: true }
  },
  {
    path: '/organization',
    name: 'organization',
    component: OrganizationView,
    meta: { requiresAuth: true, adminOnly: true }
  },
  {
    path: '/storage',
    name: 'storage',
    component: StorageView,
    meta: { requiresAuth: true }
  },
  {
    path: '/template',
    name: 'template',
    component: TemplateView,
    meta: { requiresAuth: true, adminOnly: true }
  },
  {
    path: '/traceability',
    name: 'traceability',
    component: TraceabilityView,
    meta: { requiresAuth: true }
  },
  {
    path: '/requirement',
    name: 'requirement',
    component: RequirementView,
    meta: { requiresAuth: true }
  },
  {
    path: '/design',
    name: 'design',
    component: DesignView,
    meta: { requiresAuth: true }
  },
  {
    path: '/testcase',
    name: 'testcase',
    component: TestCaseView,
    meta: { requiresAuth: true }
  },
  {
    path: '/defect',
    name: 'defect',
    component: DefectView,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userInfo = localStorage.getItem('userInfo')
  const isLoggedIn = !!userInfo

  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
    return
  }

  if (to.path === '/login' && isLoggedIn) {
    next('/hardware')
    return
  }

  if (to.meta.adminOnly && isLoggedIn) {
    const user = JSON.parse(userInfo)
    if (user.role !== 'admin') {
      alert('您没有权限访问此页面')
      next('/hardware')
      return
    }
  }

  next()
})

export default router