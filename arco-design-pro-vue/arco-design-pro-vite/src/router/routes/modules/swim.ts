import { DEFAULT_LAYOUT } from '../base';
import type { AppRouteRecordRaw } from '../types';

const SWIM: AppRouteRecordRaw = {
  path: '/swim',
  name: 'swim',
  component: DEFAULT_LAYOUT,
  redirect: '/swim/dashboard',
  meta: {
    locale: 'menu.swim',
    requiresAuth: true,
    icon: 'icon-dashboard',
    order: 1,
  },
  children: [
    {
      path: 'dashboard',
      name: 'SwimDashboard',
      component: () => import('@/views/swim/dashboard/index.vue'),
      meta: {
        locale: 'menu.swim.dashboard',
        requiresAuth: true,
        roles: ['ADMIN', 'COACH'],
      },
    },
    {
      path: 'users',
      name: 'SwimUsers',
      component: () => import('@/views/swim/users/index.vue'),
      meta: {
        locale: 'menu.swim.users',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'coaches',
      name: 'SwimCoaches',
      component: () => import('@/views/swim/coaches/index.vue'),
      meta: {
        locale: 'menu.swim.coaches',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'courses',
      name: 'SwimCourses',
      component: () => import('@/views/swim/courses/index.vue'),
      meta: {
        locale: 'menu.swim.courses',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'schedules',
      name: 'SwimSchedules',
      component: () => import('@/views/swim/schedules/index.vue'),
      meta: {
        locale: 'menu.swim.schedules',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'reservations',
      name: 'SwimReservations',
      component: () => import('@/views/swim/reservations/index.vue'),
      meta: {
        locale: 'menu.swim.reservations',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'orders',
      name: 'SwimOrders',
      component: () => import('@/views/swim/orders/index.vue'),
      meta: {
        locale: 'menu.swim.orders',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'evaluations',
      name: 'SwimEvaluations',
      component: () => import('@/views/swim/evaluations/index.vue'),
      meta: {
        locale: 'menu.swim.evaluations',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'feedbacks',
      name: 'SwimFeedbacks',
      component: () => import('@/views/swim/feedbacks/index.vue'),
      meta: {
        locale: 'menu.swim.feedbacks',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'params',
      name: 'SwimParams',
      component: () => import('@/views/swim/params/index.vue'),
      meta: {
        locale: 'menu.swim.params',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'notices',
      name: 'SwimNotices',
      component: () => import('@/views/swim/notices/index.vue'),
      meta: {
        locale: 'menu.swim.notices',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'files',
      name: 'SwimFiles',
      component: () => import('@/views/swim/files/index.vue'),
      meta: {
        locale: 'menu.swim.files',
        requiresAuth: true,
        roles: ['ADMIN'],
      },
    },
    {
      path: 'coach/schedules',
      name: 'CoachSchedules',
      component: () => import('@/views/swim/coach/schedules.vue'),
      meta: {
        locale: 'menu.swim.coachSchedules',
        requiresAuth: true,
        roles: ['COACH'],
      },
    },
    {
      path: 'coach/reservations',
      name: 'CoachReservations',
      component: () => import('@/views/swim/coach/reservations.vue'),
      meta: {
        locale: 'menu.swim.coachReservations',
        requiresAuth: true,
        roles: ['COACH'],
      },
    },
  ],
};

export default SWIM;
