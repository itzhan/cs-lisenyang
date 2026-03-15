<template>
  <div class="container">
    <div class="logo">
      <svg class="logo-svg" viewBox="0 0 24 24" width="32" height="32" fill="none" stroke="#0077B6" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M2 6c.6.5 1.2 1 2.5 1C7 7 7 5 9.5 5c2.6 0 2.4 2 5 2 2.5 0 2.5-2 5-2 1.3 0 1.9.5 2.5 1"/>
        <path d="M2 12c.6.5 1.2 1 2.5 1 2.5 0 2.5-2 5-2 2.6 0 2.4 2 5 2 2.5 0 2.5-2 5-2 1.3 0 1.9.5 2.5 1"/>
        <path d="M2 18c.6.5 1.2 1 2.5 1 2.5 0 2.5-2 5-2 2.6 0 2.4 2 5 2 2.5 0 2.5-2 5-2 1.3 0 1.9.5 2.5 1"/>
      </svg>
      <div class="logo-text">翔泳社管理系统</div>
    </div>
    <LoginBanner />
    <div class="content">
      <div class="content-inner">
        <LoginForm />
      </div>
      <div class="footer">
        <Footer />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import Footer from '@/components/footer/index.vue';
  import LoginBanner from './components/banner.vue';
  import LoginForm from './components/login-form.vue';
</script>

<style lang="less" scoped>
  .container {
    display: flex;
    height: 100vh;

    .banner {
      width: 550px;
      background: linear-gradient(163.85deg, #023e8a 0%, #0077B6 50%, #00B4D8 100%);
    }

    .content {
      position: relative;
      display: flex;
      flex: 1;
      align-items: center;
      justify-content: center;
      padding-bottom: 40px;
    }

    .footer {
      position: absolute;
      right: 0;
      bottom: 0;
      width: 100%;
    }
  }

  .logo {
    position: fixed;
    top: 24px;
    left: 22px;
    z-index: 1;
    display: inline-flex;
    align-items: center;

    &-svg {
      flex-shrink: 0;
    }

    &-text {
      margin-right: 4px;
      margin-left: 8px;
      color: var(--color-text-1);
      font-weight: 600;
      font-size: 18px;
    }
  }
</style>

<style lang="less" scoped>
  // responsive
  @media (max-width: @screen-lg) {
    .container {
      .banner {
        width: 25%;
      }
    }
  }
</style>
