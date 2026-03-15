<template>
  <div class="banner">
    <div class="banner-inner">
      <a-carousel class="carousel" animation-name="fade">
        <a-carousel-item v-for="item in carouselItem" :key="item.slogan">
          <div :key="item.slogan" class="carousel-item">
            <div class="carousel-title">{{ item.slogan }}</div>
            <div class="carousel-sub-title">{{ item.subSlogan }}</div>
            <img class="carousel-image" :src="item.image" />
          </div>
        </a-carousel-item>
      </a-carousel>
    </div>
    <!-- 水波纹装饰 -->
    <div class="wave-decoration">
      <svg viewBox="0 0 1440 120" preserveAspectRatio="none">
        <path d="M0,40 C360,100 1080,0 1440,60 L1440,120 L0,120 Z" fill="rgba(255,255,255,0.06)" />
      </svg>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { computed } from 'vue';
  import { useI18n } from 'vue-i18n';

  const { t } = useI18n();
  const carouselItem = computed(() => [
    {
      slogan: t('login.banner.slogan1'),
      subSlogan: t('login.banner.subSlogan1'),
      image: 'https://images.unsplash.com/photo-1519315901367-f34ff9154487?w=400&h=300&fit=crop&q=80',
    },
    {
      slogan: t('login.banner.slogan2'),
      subSlogan: t('login.banner.subSlogan2'),
      image: 'https://images.unsplash.com/photo-1560089000-7433a4ebbd64?w=400&h=300&fit=crop&q=80',
    },
    {
      slogan: t('login.banner.slogan3'),
      subSlogan: t('login.banner.subSlogan3'),
      image: 'https://images.unsplash.com/photo-1575429198097-0414ec08e8cd?w=400&h=300&fit=crop&q=80',
    },
  ]);
</script>

<style lang="less" scoped>
  .banner {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    overflow: hidden;

    &-inner {
      flex: 1;
      height: 100%;
    }
  }

  .carousel {
    height: 100%;

    &-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 100%;
    }

    &-title {
      color: #fff;
      font-weight: 600;
      font-size: 22px;
      line-height: 30px;
      text-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }

    &-sub-title {
      margin-top: 8px;
      color: rgba(255, 255, 255, 0.75);
      font-size: 14px;
      line-height: 22px;
    }

    &-image {
      width: 320px;
      height: 220px;
      margin-top: 30px;
      border-radius: 12px;
      object-fit: cover;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
    }
  }

  .wave-decoration {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    line-height: 0;
    pointer-events: none;

    svg {
      width: 100%;
      height: 60px;
    }
  }
</style>
