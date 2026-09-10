import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'portal',
      component: () => import('../views/portal/Home.vue'),
      meta: { title: '汽车租赁系统' }
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/portal/UserReservations.vue'),
      meta: { title: '我的预约', requiresAuth: true }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
      meta: { title: '登录' }
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/admin/Layout.vue'),
      meta: { title: '后台管理', requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('../views/admin/Dashboard.vue'),
          meta: { title: '仪表盘' }
        },
        {
          path: 'cars',
          name: 'cars',
          component: () => import('../views/admin/CarManage.vue'),
          meta: { title: '汽车管理' }
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('../views/admin/UserManage.vue'),
          meta: { title: '用户管理' }
        },
        {
          path: 'reservations',
          name: 'reservations',
          component: () => import('../views/admin/ReservationManage.vue'),
          meta: { title: '预约管理' }
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userInfo = localStorage.getItem('userInfo')

  // 需要登录的页面
  if (to.meta.requiresAuth) {
    if (!userInfo) {
      next('/login')
      return
    }

    // 需要管理员权限的页面
    if (to.meta.requiresAdmin) {
      const user = JSON.parse(userInfo)
      if (user.role !== 1) {
        alert('您没有权限访问该页面')
        next('/')
        return
      }
    }
  }

  next()
})

export default router
