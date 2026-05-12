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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userInfo = localStorage.getItem('userInfo')
  const isLoggedIn = !!userInfo
  
  // 如果需要登录且未登录，跳转到登录页
  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
    return
  }
  
  // 如果已登录且访问登录页，跳转到首页
  if (to.path === '/login' && isLoggedIn) {
    next('/hardware')
    return
  }
  
  // 如果需要管理员权限但当前用户不是管理员
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