<template>
  <div class="container">
    <div class="page-header">
      <h1>我的订单</h1>
      <p>查看您的消费记录</p>
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
            <div class="order-list" v-if="orders.length">
              <div class="order-item" v-for="o in orders" :key="o.id">
                <div class="order-header">
                  <span class="order-no">订单号：{{ o.orderNo || o.id }}</span>
                  <a-tag :color="orderStatusColor(o.status)">{{ orderStatusText(o.status) }}</a-tag>
                </div>
                <div class="order-body">
                  <div class="order-info">
                    <div class="order-course">{{ o.courseName || '课程' }}</div>
                    <div class="order-meta">
                      <span v-if="o.createTime">下单时间：{{ o.createTime }}</span>
                    </div>
                  </div>
                  <div class="order-amount">
                    <span class="amount-label">金额</span>
                    <span class="price">¥{{ o.amount || o.totalAmount || '0.00' }}</span>
                  </div>
                </div>
              </div>
            </div>
            <a-empty v-else-if="!loading" description="暂无订单记录" />
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { IconUser, IconCalendar, IconFile } from '@arco-design/web-vue/es/icon'
import { getOrders } from '../../api/user'

const orders = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

function orderStatusText(status) {
  const map = { 0: '待支付', 1: '已支付', 2: '已取消', 3: '已退款' }
  return map[status] ?? status ?? '未知'
}

function orderStatusColor(status) {
  const map = { 0: 'orangered', 1: 'green', 2: 'gray', 3: 'red' }
  return map[status] ?? 'gray'
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getOrders({ page: page.value, size: pageSize.value })
    orders.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    orders.value = []
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

.order-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-item {
  background: var(--color-bg);
  border-radius: var(--radius-sm);
  overflow: hidden;
}

.order-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px;
  background: rgba(24, 144, 255, 0.03);
  border-bottom: 1px solid var(--color-border-light);
}

.order-no {
  font-size: 12px;
  color: var(--color-text-tertiary);
}

.order-body {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
}

.order-course {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 4px;
}

.order-meta {
  font-size: 12px;
  color: var(--color-text-tertiary);
}

.order-amount {
  text-align: right;
}

.amount-label {
  display: block;
  font-size: 12px;
  color: var(--color-text-tertiary);
  margin-bottom: 2px;
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
  .order-body {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  .order-amount {
    text-align: left;
  }
}
</style>
