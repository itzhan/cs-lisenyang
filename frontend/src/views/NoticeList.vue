<template>
  <div class="container">
    <div class="page-header">
      <h1>公告中心</h1>
      <p>翔泳社最新动态与通知</p>
    </div>

    <a-spin :loading="loading" style="width: 100%">
      <div class="notice-list" v-if="notices.length">
        <div class="notice-card" v-for="n in notices" :key="n.id" @click="toggleExpand(n.id)">
          <div class="notice-header">
            <div class="notice-badge">
              <icon-notification :style="{ color: 'var(--color-primary)' }" />
            </div>
            <div class="notice-main">
              <div class="notice-title">{{ n.title }}</div>
              <div class="notice-time">{{ n.createTime }}</div>
            </div>
            <icon-down
              :class="['expand-icon', { expanded: expandedId === n.id }]"
            />
          </div>
          <transition name="fade">
            <div class="notice-body" v-if="expandedId === n.id">
              <div class="notice-content">{{ n.content || '暂无详细内容' }}</div>
            </div>
          </transition>
        </div>
      </div>
      <div class="empty-state" v-else-if="!loading">
        <a-empty description="暂无公告" />
      </div>
    </a-spin>

    <div class="pagination-wrap" v-if="total > pageSize">
      <a-pagination
        :total="total"
        :current="page"
        :page-size="pageSize"
        show-total
        @change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { IconNotification, IconDown } from '@arco-design/web-vue/es/icon'
import { getNotices } from '../api/public'

const notices = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const expandedId = ref(null)

async function fetchData() {
  loading.value = true
  try {
    const res = await getNotices({ page: page.value, size: pageSize.value })
    notices.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    notices.value = []
  } finally {
    loading.value = false
  }
}

function toggleExpand(id) {
  expandedId.value = expandedId.value === id ? null : id
}

function handlePageChange(p) {
  page.value = p
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.notice-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-width: 800px;
  margin: 0 auto;
}

.notice-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  cursor: pointer;
  transition: box-shadow 0.25s;
  overflow: hidden;
}

.notice-card:hover {
  box-shadow: var(--shadow-md);
}

.notice-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
}

.notice-badge {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: rgba(24, 144, 255, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.notice-main {
  flex: 1;
}

.notice-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 4px;
}

.notice-time {
  font-size: 12px;
  color: var(--color-text-tertiary);
}

.expand-icon {
  transition: transform 0.3s;
  color: var(--color-text-tertiary);
}

.expand-icon.expanded {
  transform: rotate(180deg);
}

.notice-body {
  padding: 0 24px 20px;
}

.notice-content {
  font-size: 14px;
  line-height: 1.8;
  color: var(--color-text-secondary);
  padding: 16px;
  background: var(--color-bg);
  border-radius: var(--radius-sm);
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}
</style>
