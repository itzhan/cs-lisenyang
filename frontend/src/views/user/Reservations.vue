<template>
  <div class="container">
    <div class="page-header">
      <h1>我的预约</h1>
      <p>管理您的课程预约记录</p>
    </div>

    <div class="profile-layout">
      <div class="profile-sidebar">
        <div class="sidebar-menu">
          <router-link to="/user/profile" class="menu-item">
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
          <a-spin :loading="loading" style="width: 100%">
            <div class="reservation-list" v-if="reservations.length">
              <div class="reservation-item" v-for="r in reservations" :key="r.id">
                <div class="reservation-info">
                  <div class="reservation-course">{{ r.courseName || '课程' }}</div>
                  <div class="reservation-meta">
                    <span v-if="r.scheduleDate">📅 {{ r.scheduleDate }}</span>
                    <span v-if="r.startTime">⏰ {{ r.startTime }} - {{ r.endTime }}</span>
                    <span v-if="r.coachName">👨‍🏫 {{ r.coachName }}</span>
                  </div>
                  <div class="reservation-time">预约时间：{{ r.createTime }}</div>
                </div>
                <div class="reservation-actions">
                  <a-tag :color="statusColor(r.status)">{{ statusText(r.status) }}</a-tag>
                  <a-button
                    v-if="canCancel(r.status)"
                    type="text"
                    status="danger"
                    size="small"
                    @click="handleCancel(r)"
                  >
                    取消预约
                  </a-button>
                  <a-button
                    v-if="canEvaluate(r.status)"
                    type="text"
                    size="small"
                    @click="openEvalModal(r)"
                  >
                    评价
                  </a-button>
                </div>
              </div>
            </div>
            <a-empty v-else-if="!loading" description="暂无预约记录" />
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
      </div>
    </div>

    <!-- 取消预约 -->
    <a-modal
      v-model:visible="cancelModalVisible"
      title="取消预约"
      @ok="confirmCancel"
      :ok-loading="cancelLoading"
      ok-text="确认取消"
      cancel-text="返回"
    >
      <a-form layout="vertical">
        <a-form-item label="取消原因">
          <a-textarea
            v-model="cancelReason"
            placeholder="请输入取消原因（可选）"
            :max-length="200"
            show-word-limit
            :auto-size="{ minRows: 3 }"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 评价弹窗 -->
    <a-modal
      v-model:visible="evalModalVisible"
      title="提交评价"
      @ok="confirmEval"
      :ok-loading="evalLoading"
      ok-text="提交评价"
      cancel-text="取消"
    >
      <a-form layout="vertical">
        <a-form-item label="评分">
          <a-rate v-model="evalForm.score" allow-half />
        </a-form-item>
        <a-form-item label="评价内容">
          <a-textarea
            v-model="evalForm.content"
            placeholder="分享您的学习体验..."
            :max-length="500"
            show-word-limit
            :auto-size="{ minRows: 3 }"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Message } from '@arco-design/web-vue'
import { IconUser, IconCalendar, IconFile } from '@arco-design/web-vue/es/icon'
import { getReservations, cancelReservation, submitEvaluation } from '../../api/user'

const reservations = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const cancelModalVisible = ref(false)
const cancelReason = ref('')
const cancelLoading = ref(false)
const selectedReservation = ref(null)

const evalModalVisible = ref(false)
const evalLoading = ref(false)
const evalForm = reactive({ score: 5, content: '' })
const evalReservation = ref(null)

function statusText(status) {
  const map = { 0: '待确认', 1: '已确认', 2: '已取消', 3: '已完成' }
  return map[status] ?? status ?? '未知'
}

function statusColor(status) {
  const map = { 0: 'orangered', 1: 'arcoblue', 2: 'gray', 3: 'green' }
  return map[status] ?? 'gray'
}

function canCancel(status) {
  return status === 0 || status === 1
}

function canEvaluate(status) {
  return status === 3
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getReservations({ page: page.value, size: pageSize.value })
    reservations.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    reservations.value = []
  } finally {
    loading.value = false
  }
}

function handlePageChange(p) {
  page.value = p
  fetchData()
}

function handleCancel(r) {
  selectedReservation.value = r
  cancelReason.value = ''
  cancelModalVisible.value = true
}

async function confirmCancel() {
  cancelLoading.value = true
  try {
    await cancelReservation(selectedReservation.value.id, cancelReason.value || undefined)
    Message.success('已取消预约')
    cancelModalVisible.value = false
    fetchData()
  } catch {
    // handled by interceptor
  } finally {
    cancelLoading.value = false
  }
}

function openEvalModal(r) {
  evalReservation.value = r
  evalForm.score = 5
  evalForm.content = ''
  evalModalVisible.value = true
}

async function confirmEval() {
  if (!evalForm.content) {
    Message.warning('请输入评价内容')
    return
  }
  evalLoading.value = true
  try {
    await submitEvaluation({
      courseId: evalReservation.value.courseId,
      reservationId: evalReservation.value.id,
      score: evalForm.score,
      content: evalForm.content,
    })
    Message.success('评价已提交')
    evalModalVisible.value = false
    fetchData()
  } catch {
    // handled by interceptor
  } finally {
    evalLoading.value = false
  }
}

onMounted(fetchData)
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
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.reservation-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reservation-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: var(--color-bg);
  border-radius: var(--radius-sm);
  gap: 16px;
}

.reservation-course {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 6px;
}

.reservation-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-bottom: 4px;
  flex-wrap: wrap;
}

.reservation-time {
  font-size: 12px;
  color: var(--color-text-tertiary);
}

.reservation-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  padding-top: 20px;
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
  .reservation-item {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
