import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layout/Layout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      {
        path: 'resources',
        name: 'Resources',
        component: () => import('../views/Resources.vue')
      },
      {
        path: 'ai-chat',
        name: 'AiChat',
        component: () => import('../views/AiChat.vue')
      },
      {
        path: 'ai-practice',
        name: 'AiPractice',
        component: () => import('../views/AiPractice.vue')
      },
      {
        path: 'wrong-questions',
        name: 'WrongQuestions',
        component: () => import('../views/WrongQuestions.vue')
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('../views/Statistics.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
