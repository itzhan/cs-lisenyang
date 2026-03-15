<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-left">
        <div class="login-brand">
          <Waves class="brand-icon" :size="48" :stroke-width="2" />
          <h2>翔泳社</h2>
          <p>专业游泳培训与管理平台</p>
        </div>
        <div class="login-features">
          <div class="feature-item">
            <BookOpen class="feature-icon" :size="20" />
            <span>丰富的课程选择</span>
          </div>
          <div class="feature-item">
            <GraduationCap class="feature-icon" :size="20" />
            <span>专业教练团队</span>
          </div>
          <div class="feature-item">
            <CalendarDays class="feature-icon" :size="20" />
            <span>便捷在线预约</span>
          </div>
        </div>
      </div>

      <div class="login-right">
        <div class="form-wrapper">
          <div class="form-tabs">
            <div
              class="tab-item"
              :class="{ active: mode === 'login' }"
              @click="mode = 'login'"
            >
              登录
            </div>
            <div
              class="tab-item"
              :class="{ active: mode === 'register' }"
              @click="mode = 'register'"
            >
              注册
            </div>
          </div>

          <!-- 登录表单 -->
          <a-form
            v-if="mode === 'login'"
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            layout="vertical"
            @submit-success="handleLogin"
          >
            <a-form-item field="username" label="用户名">
              <a-input v-model="loginForm.username" placeholder="请输入用户名" size="large" allow-clear />
            </a-form-item>
            <a-form-item field="password" label="密码">
              <a-input-password v-model="loginForm.password" placeholder="请输入密码" size="large" allow-clear />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" html-type="submit" long size="large" :loading="submitting">
                登录
              </a-button>
            </a-form-item>
          </a-form>

          <!-- 注册表单 -->
          <a-form
            v-else
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            layout="vertical"
            @submit-success="handleRegister"
          >
            <a-form-item field="username" label="用户名">
              <a-input v-model="registerForm.username" placeholder="请输入用户名" size="large" allow-clear />
            </a-form-item>
            <a-form-item field="password" label="密码">
              <a-input-password v-model="registerForm.password" placeholder="请输入密码" size="large" allow-clear />
            </a-form-item>
            <a-form-item field="realName" label="真实姓名">
              <a-input v-model="registerForm.realName" placeholder="请输入真实姓名" size="large" allow-clear />
            </a-form-item>
            <a-form-item field="phone" label="手机号">
              <a-input v-model="registerForm.phone" placeholder="请输入手机号" size="large" allow-clear />
            </a-form-item>
            <a-form-item field="email" label="邮箱">
              <a-input v-model="registerForm.email" placeholder="请输入邮箱" size="large" allow-clear />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" html-type="submit" long size="large" :loading="submitting">
                注册
              </a-button>
            </a-form-item>
          </a-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Message } from '@arco-design/web-vue'
import { login, register } from '../api/user'
import { useAuthStore } from '../stores/auth'
import { Waves, BookOpen, GraduationCap, CalendarDays } from 'lucide-vue-next'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const mode = ref('login')
const submitting = ref(false)

const loginForm = reactive({ username: '', password: '' })
const registerForm = reactive({ username: '', password: '', realName: '', phone: '', email: '' })

const loginRules = {
  username: [{ required: true, message: '请输入用户名' }],
  password: [{ required: true, message: '请输入密码' }],
}

const registerRules = {
  username: [{ required: true, message: '请输入用户名' }],
  password: [{ required: true, message: '请输入密码' }, { minLength: 6, message: '密码至少6位' }],
  realName: [{ required: true, message: '请输入真实姓名' }],
  phone: [{ required: true, message: '请输入手机号' }],
  email: [{ required: true, message: '请输入邮箱' }],
}

async function handleLogin() {
  submitting.value = true
  try {
    const res = await login({ username: loginForm.username, password: loginForm.password })
    const { token, username, role } = res.data
    authStore.setAuth(token, username, role)
    Message.success('登录成功')
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } catch {
    // handled by interceptor
  } finally {
    submitting.value = false
  }
}

async function handleRegister() {
  submitting.value = true
  try {
    await register({
      username: registerForm.username,
      password: registerForm.password,
      realName: registerForm.realName,
      phone: registerForm.phone,
      email: registerForm.email,
    })
    Message.success('注册成功，请登录')
    mode.value = 'login'
    loginForm.username = registerForm.username
    loginForm.password = ''
  } catch {
    // handled by interceptor
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - var(--header-height) - 80px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8edf3 100%);
}

.login-container {
  display: flex;
  width: 100%;
  max-width: 900px;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #1890FF 0%, #096dd9 100%);
  color: #fff;
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-brand {
  margin-bottom: 48px;
}

.brand-icon {
  color: rgba(255, 255, 255, 0.95);
  display: block;
  margin-bottom: 16px;
}

.login-brand h2 {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.login-brand p {
  opacity: 0.85;
  font-size: 15px;
}

.login-features {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 15px;
  opacity: 0.9;
}

.feature-icon {
  opacity: 0.9;
  flex-shrink: 0;
}

.login-right {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  align-items: center;
}

.form-wrapper {
  width: 100%;
}

.form-tabs {
  display: flex;
  gap: 24px;
  margin-bottom: 32px;
}

.tab-item {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text-tertiary);
  cursor: pointer;
  padding-bottom: 8px;
  border-bottom: 2px solid transparent;
  transition: all 0.25s;
}

.tab-item.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  .login-left {
    padding: 32px 24px;
  }
  .login-right {
    padding: 32px 24px;
  }
  .login-features {
    display: none;
  }
}
</style>
