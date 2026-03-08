<template>
  <div class="container">
    <div class="page-header">
      <h1>个人中心</h1>
      <p>查看和编辑您的个人信息</p>
    </div>

    <div class="profile-layout">
      <div class="profile-sidebar">
        <div class="avatar-section">
          <a-avatar :size="80" :style="{ backgroundColor: '#1890FF' }">
            {{ profile.username?.charAt(0)?.toUpperCase() || 'U' }}
          </a-avatar>
          <div class="user-info">
            <div class="user-name">{{ profile.realName || profile.username || '用户' }}</div>
            <div class="user-role">普通用户</div>
          </div>
        </div>

        <div class="sidebar-menu">
          <router-link to="/user/profile" class="menu-item active">
            <icon-user /> 个人信息
          </router-link>
          <router-link to="/user/reservations" class="menu-item">
            <icon-calendar /> 我的预约
          </router-link>
          <router-link to="/user/orders" class="menu-item">
            <icon-file /> 我的订单
          </router-link>
        </div>
      </div>

      <div class="profile-main">
        <div class="section-card">
          <h3 class="card-heading">基本信息</h3>
          <a-spin :loading="loading">
            <a-form
              ref="formRef"
              :model="formData"
              layout="vertical"
              @submit-success="handleSave"
            >
              <div class="form-grid">
                <a-form-item field="realName" label="真实姓名">
                  <a-input v-model="formData.realName" placeholder="请输入真实姓名" allow-clear />
                </a-form-item>
                <a-form-item field="gender" label="性别">
                  <a-select v-model="formData.gender" placeholder="请选择性别" allow-clear>
                    <a-option value="男">男</a-option>
                    <a-option value="女">女</a-option>
                  </a-select>
                </a-form-item>
                <a-form-item field="phone" label="手机号">
                  <a-input v-model="formData.phone" placeholder="请输入手机号" allow-clear />
                </a-form-item>
                <a-form-item field="email" label="邮箱">
                  <a-input v-model="formData.email" placeholder="请输入邮箱" allow-clear />
                </a-form-item>
              </div>
              <a-form-item>
                <a-button type="primary" html-type="submit" :loading="saving">保存修改</a-button>
              </a-form-item>
            </a-form>
          </a-spin>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Message } from '@arco-design/web-vue'
import { IconUser, IconCalendar, IconFile } from '@arco-design/web-vue/es/icon'
import { getProfile, updateProfile } from '../../api/user'

const profile = ref({})
const formData = reactive({ realName: '', phone: '', email: '', gender: '' })
const loading = ref(false)
const saving = ref(false)

async function fetchProfile() {
  loading.value = true
  try {
    const res = await getProfile()
    profile.value = res.data || {}
    Object.assign(formData, {
      realName: res.data?.realName || '',
      phone: res.data?.phone || '',
      email: res.data?.email || '',
      gender: res.data?.gender || '',
    })
  } finally {
    loading.value = false
  }
}

async function handleSave() {
  saving.value = true
  try {
    await updateProfile({
      realName: formData.realName,
      phone: formData.phone,
      email: formData.email,
      gender: formData.gender,
    })
    Message.success('保存成功')
    fetchProfile()
  } catch {
    // handled by interceptor
  } finally {
    saving.value = false
  }
}

onMounted(fetchProfile)
</script>

<style scoped>
.profile-layout {
  display: flex;
  gap: 24px;
  padding-bottom: 40px;
}

.profile-sidebar {
  width: 240px;
  flex-shrink: 0;
}

.avatar-section {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 24px;
  text-align: center;
  box-shadow: var(--shadow-sm);
  margin-bottom: 16px;
}

.user-info {
  margin-top: 12px;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
}

.user-role {
  font-size: 12px;
  color: var(--color-text-tertiary);
  margin-top: 2px;
}

.sidebar-menu {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 8px;
  box-shadow: var(--shadow-sm);
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border-radius: var(--radius-sm);
  font-size: 14px;
  color: var(--color-text-secondary);
  transition: all 0.2s;
}

.menu-item:hover {
  background: var(--color-bg);
  color: var(--color-primary);
}

.menu-item.active,
.menu-item.router-link-active {
  background: rgba(24, 144, 255, 0.08);
  color: var(--color-primary);
  font-weight: 500;
}

.profile-main {
  flex: 1;
  min-width: 0;
}

.section-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 24px 32px;
  box-shadow: var(--shadow-sm);
}

.card-heading {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--color-border-light);
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 24px;
}

@media (max-width: 768px) {
  .profile-layout {
    flex-direction: column;
  }
  .profile-sidebar {
    width: 100%;
  }
  .sidebar-menu {
    display: flex;
    gap: 4px;
    overflow-x: auto;
  }
  .menu-item {
    white-space: nowrap;
  }
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
