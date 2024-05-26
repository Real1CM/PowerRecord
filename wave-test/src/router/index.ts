import { createRouter, createWebHistory } from 'vue-router'
import LayoutView from '@/views/layout/LayoutView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login/LoginView.vue')
    },
    {
      path: '/',
      name: 'home',
      component: LayoutView,
      redirect: '/home',
      children: [
        {
          path: 'home',
          component: () => import( '@/views/home/HomeView.vue'),
        }
      ]
    },
    {
      path: '/users',
      name: 'users',
      component: LayoutView,
      children: [
        {
          path: 'page',
          component: () => import( '@/views/user/UsersView.vue'),
        }
      ]
    },
    {
      path: '/facility',
      name: 'facility',
      component: LayoutView,
      children: [
        {
          path: 'page',
          component: () => import( '@/views/facility/FacilityView.vue'),
        }
      ]
    },
    {
      path: '/wave',
      name: 'wavedata',
      component: LayoutView,
      children: [
        {
          path: 'compare',
          component: () => import( '@/views/wave/WaveDataView.vue'),
        }
      ]
    },
  ]
})

export default router
