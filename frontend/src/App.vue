<template>
  <div id="app">
    <header class="app-header">
      <div class="container header-inner">
        <router-link to="/" class="logo">
          <Waves class="logo-icon" :size="26" :stroke-width="2.5" />
          <span class="logo-text">翔泳社</span>
        </router-link>

        <nav class="nav-links">
          <router-link to="/" exact-active-class="active">首页</router-link>
          <router-link to="/courses" active-class="active">课程</router-link>
          <router-link to="/coaches" active-class="active">教练</router-link>
          <router-link to="/notices" active-class="active">公告</router-link>
        </nav>

        <div class="header-actions">
          <template v-if="authStore.token">
            <a-dropdown trigger="hover">
              <a-button type="text" class="user-btn">
                <a-avatar :size="28" :style="{ backgroundColor: '#1890FF' }">
                  {{ authStore.username?.charAt(0)?.toUpperCase() }}
                </a-avatar>
                <span class="user-name">{{ authStore.username }}</span>
                <icon-down />
              </a-button>
              <template #content>
                <a-doption @click="$router.push('/user/profile')">个人中心</a-doption>
                <a-doption @click="$router.push('/user/reservations')">我的预约</a-doption>
                <a-doption @click="$router.push('/user/orders')">我的订单</a-doption>
                <a-divider style="margin: 4px 0" />
                <a-doption @click="handleLogout">
                  <span style="color: var(--color-danger)">退出登录</span>
                </a-doption>
              </template>
            </a-dropdown>
          </template>
          <template v-else>
            <a-button type="primary" @click="$router.push('/login')">登录 / 注册</a-button>
          </template>
        </div>

        <div class="mobile-menu-btn" @click="mobileMenuOpen = !mobileMenuOpen">
          <icon-menu />
        </div>
      </div>

      <transition name="fade">
        <div v-if="mobileMenuOpen" class="mobile-menu">
          <router-link to="/" @click="mobileMenuOpen = false">首页</router-link>
          <router-link to="/courses" @click="mobileMenuOpen = false">课程</router-link>
          <router-link to="/coaches" @click="mobileMenuOpen = false">教练</router-link>
          <router-link to="/notices" @click="mobileMenuOpen = false">公告</router-link>
          <template v-if="authStore.token">
            <router-link to="/user/profile" @click="mobileMenuOpen = false">个人中心</router-link>
            <router-link to="/user/reservations" @click="mobileMenuOpen = false">我的预约</router-link>
            <router-link to="/user/orders" @click="mobileMenuOpen = false">我的订单</router-link>
            <a @click="handleLogout(); mobileMenuOpen = false" style="color: var(--color-danger); cursor: pointer">退出登录</a>
          </template>
          <template v-else>
            <router-link to="/login" @click="mobileMenuOpen = false">登录 / 注册</router-link>
          </template>
        </div>
      </transition>
    </header>

    <main class="app-main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <footer class="app-footer">
      <div class="container footer-inner">
        <div class="footer-brand">
          <Waves class="logo-icon" :size="22" :stroke-width="2.5" />
          <span>翔泳社游泳馆管理系统</span>
        </div>
        <div class="footer-copy">© 2025 翔泳社. All rights reserved.</div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'
import { IconDown, IconMenu } from '@arco-design/web-vue/es/icon'
import { Waves } from 'lucide-vue-next'

const authStore = useAuthStore()
const router = useRouter()
const mobileMenuOpen = ref(false)

authStore.load()

function handleLogout() {
  authStore.logout()
  router.push('/')
}
</script>

<style scoped>
.app-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid var(--color-border-light);
  height: var(--header-height);
}

.header-inner {
  display: flex;
  align-items: center;
  height: var(--header-height);
  gap: 32px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 700;
  color: var(--color-text);
  flex-shrink: 0;
}

.logo:hover {
  color: var(--color-text);
}

.logo-icon {
  color: #0077B6;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
}

.nav-links a {
  padding: 6px 16px;
  border-radius: 6px;
  font-size: 15px;
  font-weight: 500;
  color: var(--color-text-secondary);
  transition: all 0.2s;
}

.nav-links a:hover {
  color: var(--color-primary);
  background: rgba(24, 144, 255, 0.06);
}

.nav-links a.active {
  color: var(--color-primary);
  background: rgba(24, 144, 255, 0.1);
}

.header-actions {
  flex-shrink: 0;
}

.user-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text);
}

.mobile-menu-btn {
  display: none;
  font-size: 22px;
  cursor: pointer;
  color: var(--color-text);
}

.mobile-menu {
  position: absolute;
  top: var(--header-height);
  left: 0;
  right: 0;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  display: flex;
  flex-direction: column;
  padding: 12px 24px;
  gap: 4px;
}

.mobile-menu a {
  padding: 10px 12px;
  border-radius: 6px;
  font-size: 15px;
  color: var(--color-text-secondary);
}

.mobile-menu a:hover {
  background: var(--color-bg);
}

.app-main {
  min-height: calc(100vh - var(--header-height) - 80px);
}

.app-footer {
  background: var(--color-surface);
  border-top: 1px solid var(--color-border-light);
  padding: 24px 0;
}

.footer-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.footer-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: var(--color-text-secondary);
}

.footer-copy {
  font-size: 13px;
  color: var(--color-text-tertiary);
}

@media (max-width: 768px) {
  .nav-links,
  .header-actions {
    display: none;
  }
  .mobile-menu-btn {
    display: block;
    margin-left: auto;
  }
  .footer-inner {
    flex-direction: column;
    gap: 8px;
    text-align: center;
  }
}
</style>
