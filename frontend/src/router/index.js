import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
  },
  {
    path: '/courses',
    name: 'CourseList',
    component: () => import('../views/CourseList.vue'),
  },
  {
    path: '/courses/:id',
    name: 'CourseDetail',
    component: () => import('../views/CourseDetail.vue'),
  },
  {
    path: '/coaches',
    name: 'CoachList',
    component: () => import('../views/CoachList.vue'),
  },
  {
    path: '/notices',
    name: 'NoticeList',
    component: () => import('../views/NoticeList.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { hideForAuth: true },
  },
  {
    path: '/user/profile',
    name: 'Profile',
    component: () => import('../views/user/Profile.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/user/reservations',
    name: 'Reservations',
    component: () => import('../views/user/Reservations.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/user/orders',
    name: 'Orders',
    component: () => import('../views/user/Orders.vue'),
    meta: { requiresAuth: true },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  auth.load()
  if (to.meta.requiresAuth && !auth.token) {
    return { name: 'Login', query: { redirect: to.fullPath } }
  }
  if (to.meta.hideForAuth && auth.token) {
    return '/'
  }
  return true
})

export default router
