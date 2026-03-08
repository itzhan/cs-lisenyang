<template>
  <div class="container">
    <div class="page-header">
      <h1>课程中心</h1>
      <p>探索适合您的游泳课程，从入门到进阶全覆盖</p>
    </div>

    <div class="filter-bar">
      <a-input-search
        v-model="keyword"
        placeholder="搜索课程名称..."
        style="max-width: 360px"
        allow-clear
        @search="handleSearch"
        @clear="handleSearch"
      />
    </div>

    <a-spin :loading="loading" style="width: 100%">
      <div class="card-grid" v-if="courses.length">
        <div
          class="card-item course-card"
          v-for="c in courses"
          :key="c.id"
          @click="$router.push(`/courses/${c.id}`)"
        >
          <div class="card-cover">
            <span>🏊‍♂️</span>
          </div>
          <div class="card-body">
            <div class="card-title">{{ c.courseName }}</div>
            <div class="card-desc">{{ c.description || '暂无描述' }}</div>
          </div>
          <div class="card-footer">
            <span class="price">¥{{ c.price }}</span>
            <a-tag color="arcoblue" size="small">{{ c.level || '全级别' }}</a-tag>
          </div>
        </div>
      </div>
      <div class="empty-state" v-else-if="!loading">
        <a-empty description="暂无课程数据" />
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
import { getCourses } from '../api/public'

const courses = ref([])
const loading = ref(false)
const keyword = ref('')
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)

async function fetchData() {
  loading.value = true
  try {
    const res = await getCourses({ page: page.value, size: pageSize.value, keyword: keyword.value || undefined })
    courses.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    courses.value = []
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  page.value = 1
  fetchData()
}

function handlePageChange(p) {
  page.value = p
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.filter-bar {
  margin-bottom: 24px;
}

.course-card {
  cursor: pointer;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}
</style>
