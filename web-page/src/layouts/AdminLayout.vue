<template>
  <el-container class="admin-layout">
    <el-aside width="220px" class="sidebar">
      <div class="admin-brand">
        <span class="brand-text">ADMIN</span>
        <span class="brand-sub">PANEL</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="clean-menu"
        router
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataBoard /></el-icon>
          <span>Overview</span>
        </el-menu-item>
        <el-menu-item index="/admin/cars">
          <el-icon><Van /></el-icon>
          <span>VehicleAdmin</span>
        </el-menu-item>
        <el-menu-item index="/admin/reservations">
          <el-icon><List /></el-icon>
          <span>Reservations</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>Users</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="admin-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">Admin</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown trigger="click">
            <div class="admin-user">
              <el-avatar :size="32" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
              <span class="username">{{ userInfo?.username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">Sign Out</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="admin-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { DataBoard, Van, List, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userInfo = ref(null)

const activeMenu = computed(() => route.path)
const currentRouteTitle = computed(() => route.meta.title || 'Admin')

onMounted(() => {
  const savedUserInfo = localStorage.getItem('userInfo')
  if (savedUserInfo) {
    userInfo.value = JSON.parse(savedUserInfo)
  }
})

const handleLogout = () => {
  localStorage.removeItem('userInfo')
  ElMessage.success('Signed out of the admin panel')
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  background-color: #f5f7f9;
}

.sidebar {
  background-color: #fff;
  border-right: 1px solid #eef1f6;
  display: flex;
  flex-direction: column;
}

.admin-brand {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  border-bottom: 1px solid #f0f2f5;
}

.brand-text {
  font-weight: 800;
  color: #1a1a1a;
  letter-spacing: 1px;
}

.brand-sub {
  color: #999;
  font-size: 12px;
}

.clean-menu {
  border-right: none;
  padding: 16px 0;
}

.clean-menu :deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
  margin: 4px 12px;
  border-radius: 8px;
  color: #606266;
}

.clean-menu :deep(.el-menu-item.is-active) {
  background-color: #f0f4ff;
  color: #409eff;
  font-weight: 600;
}

.clean-menu :deep(.el-menu-item:hover) {
  background-color: #f5f7fa;
}

.admin-header {
  background-color: #fff;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  border-bottom: 1px solid #f0f2f5;
}

.admin-user {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.3s;
}

.admin-user:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.admin-main {
  padding: 24px;
  overflow-y: auto;
}

/* Page transition animation */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-15px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(15px);
}
</style>
