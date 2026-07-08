<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="aside">
      <div class="logo">
        <el-icon :size="24"><Reading /></el-icon>
        <span>408考研助手</span>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/home">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/resources">
          <el-icon><FolderOpened /></el-icon>
          <span>资源中心</span>
        </el-menu-item>
        <el-menu-item index="/ai-chat">
          <el-icon><ChatDotRound /></el-icon>
          <span>AI问答</span>
        </el-menu-item>
        <el-menu-item index="/ai-practice">
          <el-icon><EditPen /></el-icon>
          <span>AI练习</span>
        </el-menu-item>
        <el-menu-item index="/wrong-questions">
          <el-icon><WarningFilled /></el-icon>
          <span>错题本</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <el-icon><DataLine /></el-icon>
          <span>学习统计</span>
        </el-menu-item>
        <el-menu-item index="/profile">
          <el-icon><UserFilled /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-right">
          <span class="username">{{ userStore.userInfo?.nickname || '用户' }}</span>
          <el-button type="danger" size="small" @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #001529;
  overflow-y: auto;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 20px 0;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.header {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  font-size: 14px;
  color: #333;
}

.main-content {
  background: #f5f7fa;
}
</style>
