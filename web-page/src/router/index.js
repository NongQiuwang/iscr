import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'portal',
      component: () => import('../views/portal/Home.vue'),
      meta: { title: 'Car Rental System' }
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/portal/UserReservations.vue'),
      meta: { title: 'My Reservations', requiresAuth: true }
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/portal/About.vue'),
      meta: { title: 'About Us' }
    },
    {
      path: '/cars/:id',
      name: 'car-detail',
      component: () => import('../views/portal/CarDetail.vue'),
      meta: { title: 'Vehicle Details' }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/auth/Login.vue'),
      meta: { title: 'Login' }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/auth/Register.vue'),
      meta: { title: 'Register' }
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../layouts/AdminLayout.vue'),
      redirect: '/admin/dashboard',
      meta: { title: 'Admin Panel', requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('../views/admin/Dashboard.vue'),
          meta: { title: 'Dashboard' }
        },
        {
          path: 'cars',
          name: 'cars',
          component: () => import('../views/admin/CarManage.vue'),
          meta: { title: 'Car Management' }
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('../views/admin/UserManage.vue'),
          meta: { title: 'User Management' }
        },
        {
          path: 'reservations',
          name: 'reservations',
          component: () => import('../views/admin/ReservationManage.vue'),
          meta: { title: 'Reservation Management' }
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFound.vue'),
      meta: { title: 'Page Not Found' }
    }
  ]
})

// Route guard
router.beforeEach((to, from, next) => {
  const userInfo = localStorage.getItem('userInfo')

  // Pages that require login
  if (to.meta.requiresAuth) {
    if (!userInfo) {
      next({ path: '/login', query: { redirect: to.fullPath } })
      return
    }

    // Pages that require admin access
    if (to.meta.requiresAdmin) {
      try {
        const user = JSON.parse(userInfo)
        if (user.role !== 1) {
          alert('You do not have permission to access this page')
          next('/')
          return
        }
      } catch (e) {
        localStorage.removeItem('userInfo')
        next({ path: '/login', query: { redirect: to.fullPath } })
        return
      }
    }
  }

  next()
})

export default router
