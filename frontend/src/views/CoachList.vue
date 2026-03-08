<template>
  <div class="container">
    <div class="page-header">
      <h1>教练团队</h1>
      <p>认识我们经验丰富的专业游泳教练</p>
    </div>

    <a-spin :loading="loading" style="width: 100%">
      <div class="coach-grid" v-if="coaches.length">
        <div class="coach-card" v-for="coach in coaches" :key="coach.id">
          <div class="coach-avatar-wrap">
            <a-avatar :size="80" :style="{ backgroundColor: avatarColors[coach.id % avatarColors.length] }">
              {{ coach.name?.charAt(0) || '教' }}
            </a-avatar>
          </div>
          <div class="coach-info">
            <div class="coach-name">{{ coach.name }}</div>
            <a-tag color="arcoblue" size="small">{{ coach.specialty || '游泳教练' }}</a-tag>
            <div class="coach-detail" v-if="coach.experience">
              <span>{{ coach.experience }}年教学经验</span>
            </div>
            <div class="coach-bio" v-if="coach.introduction">{{ coach.introduction }}</div>
            <div class="coach-bio" v-else>专业游泳教练，持有相关资格证书，擅长教学引导。</div>
          </div>
        </div>
      </div>
      <div class="empty-state" v-else-if="!loading">
        <a-empty description="暂无教练数据" />
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
import { getCoaches } from '../api/public'

const coaches = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)

const avatarColors = ['#1890FF', '#00B42A', '#E6A23C', '#F53F3F', '#722ED1', '#0FC6C2']

async function fetchData() {
  loading.value = true
  try {
    const res = await getCoaches({ page: page.value, size: pageSize.value })
    coaches.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    coaches.value = []
  } finally {
    loading.value = false
  }
}

function handlePageChange(p) {
  page.value = p
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.coach-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.coach-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 24px;
  box-shadow: var(--shadow-sm);
  display: flex;
  gap: 20px;
  transition: transform 0.25s, box-shadow 0.25s;
}

.coach-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.coach-avatar-wrap {
  flex-shrink: 0;
}

.coach-info {
  flex: 1;
}

.coach-name {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
}

.coach-detail {
  margin-top: 8px;
  font-size: 13px;
  color: var(--color-text-secondary);
}

.coach-bio {
  margin-top: 8px;
  font-size: 13px;
  color: var(--color-text-tertiary);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}

@media (max-width: 768px) {
  .coach-grid {
    grid-template-columns: 1fr;
  }
}
</style>
