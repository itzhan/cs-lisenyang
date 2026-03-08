<template>
  <div class="container course-detail">
    <div class="page-header">
      <a-breadcrumb>
        <a-breadcrumb-item><router-link to="/courses">课程中心</router-link></a-breadcrumb-item>
        <a-breadcrumb-item>课程详情</a-breadcrumb-item>
      </a-breadcrumb>
    </div>

    <a-spin :loading="loading" style="width: 100%">
      <template v-if="course">
        <!-- 课程信息 -->
        <div class="detail-card">
          <div class="detail-header">
            <div class="detail-cover">
              <span>🏊‍♂️</span>
            </div>
            <div class="detail-info">
              <h1 class="detail-title">{{ course.courseName }}</h1>
              <div class="detail-meta">
                <a-tag color="arcoblue" v-if="course.level">{{ course.level }}</a-tag>
                <span class="meta-item" v-if="course.duration">⏱ {{ course.duration }}分钟/课时</span>
                <span class="meta-item" v-if="course.capacity">👥 容量 {{ course.capacity }}人</span>
              </div>
              <div class="detail-price">
                <span class="price-label">课程价格</span>
                <span class="price">¥{{ course.price }}</span>
              </div>
              <div class="detail-desc">{{ course.description || '暂无课程描述' }}</div>
            </div>
          </div>
        </div>

        <!-- 排课时间表 -->
        <div class="schedule-section">
          <h2 class="sub-title">排课时间表</h2>
          <a-spin :loading="scheduleLoading">
            <div class="schedule-list" v-if="schedules.length">
              <div class="schedule-item" v-for="s in schedules" :key="s.id">
                <div class="schedule-info">
                  <div class="schedule-time">
                    <icon-calendar /> {{ s.scheduleDate }}
                  </div>
                  <div class="schedule-period">
                    {{ s.startTime }} - {{ s.endTime }}
                  </div>
                  <div class="schedule-coach" v-if="s.coachName">
                    教练：{{ s.coachName }}
                  </div>
                  <div class="schedule-remain">
                    剩余名额：<span :class="s.remainCapacity > 0 ? 'text-success' : 'text-danger'">{{ s.remainCapacity ?? '-' }}</span>
                  </div>
                </div>
                <a-button
                  type="primary"
                  size="small"
                  :disabled="!s.remainCapacity || s.remainCapacity <= 0"
                  @click="handleReserve(s)"
                >
                  {{ s.remainCapacity > 0 ? '立即预约' : '已满' }}
                </a-button>
              </div>
            </div>
            <a-empty v-else-if="!scheduleLoading" description="暂无排课信息" />
          </a-spin>
        </div>

        <!-- 评价列表（占位，实际需后端支持） -->
        <div class="eval-section">
          <h2 class="sub-title">学员评价</h2>
          <a-empty description="暂无评价" />
        </div>
      </template>
    </a-spin>

    <!-- 预约确认 -->
    <a-modal
      v-model:visible="reserveModalVisible"
      title="确认预约"
      @ok="confirmReserve"
      :ok-loading="reserveLoading"
      ok-text="确认预约"
      cancel-text="取消"
    >
      <p>确认预约以下课程？</p>
      <a-descriptions :column="1" bordered size="small" style="margin-top: 12px">
        <a-descriptions-item label="课程">{{ course?.courseName }}</a-descriptions-item>
        <a-descriptions-item label="日期">{{ selectedSchedule?.scheduleDate }}</a-descriptions-item>
        <a-descriptions-item label="时间">{{ selectedSchedule?.startTime }} - {{ selectedSchedule?.endTime }}</a-descriptions-item>
        <a-descriptions-item label="教练">{{ selectedSchedule?.coachName || '-' }}</a-descriptions-item>
      </a-descriptions>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Message } from '@arco-design/web-vue'
import { IconCalendar } from '@arco-design/web-vue/es/icon'
import { getCourses, getSchedules } from '../api/public'
import { createReservation } from '../api/user'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const course = ref(null)
const loading = ref(true)
const schedules = ref([])
const scheduleLoading = ref(false)

const reserveModalVisible = ref(false)
const selectedSchedule = ref(null)
const reserveLoading = ref(false)

async function fetchCourse() {
  loading.value = true
  try {
    const res = await getCourses({ page: 1, size: 100 })
    const list = res.data?.records || []
    course.value = list.find((c) => String(c.id) === String(route.params.id)) || null
  } finally {
    loading.value = false
  }
}

async function fetchSchedules() {
  scheduleLoading.value = true
  try {
    const res = await getSchedules({ page: 1, size: 100 })
    const all = res.data?.records || []
    schedules.value = all.filter((s) => String(s.courseId) === String(route.params.id))
  } finally {
    scheduleLoading.value = false
  }
}

function handleReserve(schedule) {
  if (!authStore.token) {
    Message.warning('请先登录后再预约')
    router.push({ name: 'Login', query: { redirect: route.fullPath } })
    return
  }
  selectedSchedule.value = schedule
  reserveModalVisible.value = true
}

async function confirmReserve() {
  reserveLoading.value = true
  try {
    await createReservation({ scheduleId: selectedSchedule.value.id })
    Message.success('预约成功！')
    reserveModalVisible.value = false
    fetchSchedules()
  } catch {
    // error handled by interceptor
  } finally {
    reserveLoading.value = false
  }
}

onMounted(() => {
  fetchCourse()
  fetchSchedules()
})
</script>

<style scoped>
.detail-card {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 32px;
  box-shadow: var(--shadow-sm);
  margin-bottom: 24px;
}

.detail-header {
  display: flex;
  gap: 32px;
}

.detail-cover {
  width: 240px;
  height: 180px;
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, #e8f4ff 0%, #d6e8fa 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 64px;
  flex-shrink: 0;
}

.detail-info {
  flex: 1;
}

.detail-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 12px;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.meta-item {
  font-size: 13px;
  color: var(--color-text-secondary);
}

.detail-price {
  margin-bottom: 16px;
}

.price-label {
  font-size: 13px;
  color: var(--color-text-tertiary);
  margin-right: 8px;
}

.detail-desc {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.8;
}

.sub-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  padding-left: 12px;
  border-left: 3px solid var(--color-primary);
}

.schedule-section,
.eval-section {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 24px 32px;
  box-shadow: var(--shadow-sm);
  margin-bottom: 24px;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.schedule-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  background: var(--color-bg);
  border-radius: var(--radius-sm);
}

.schedule-info {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.schedule-time {
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.schedule-period,
.schedule-coach,
.schedule-remain {
  font-size: 13px;
  color: var(--color-text-secondary);
}

.text-success {
  color: var(--color-success);
  font-weight: 600;
}

.text-danger {
  color: var(--color-danger);
  font-weight: 600;
}

@media (max-width: 768px) {
  .detail-header {
    flex-direction: column;
  }
  .detail-cover {
    width: 100%;
    height: 160px;
  }
  .schedule-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>
