<template>
  <router-view v-slot="{ Component, route }">
    <transition name="fade" mode="out-in" appear>
      <component
        :is="Component"
        v-if="route.meta.ignoreCache || !useKeepAlive"
        :key="route.fullPath"
      />
      <keep-alive v-else :include="cacheList">
        <component :is="Component" :key="route.fullPath" />
      </keep-alive>
    </transition>
  </router-view>
</template>

<script lang="ts" setup>
  import { computed } from 'vue';
  import { useTabBarStore, useAppStore } from '@/store';

  const appStore = useAppStore();
  const tabBarStore = useTabBarStore();

  const useKeepAlive = computed(() => appStore.tabBar);
  const cacheList = computed(() => tabBarStore.getCacheList);
</script>

<style scoped lang="less"></style>
