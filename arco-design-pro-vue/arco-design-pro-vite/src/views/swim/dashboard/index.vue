<template>
  <div class="dashboard-wrapper">
    <a-spin :loading="loading" style="width: 100%">
      <!-- 欢迎卡片 -->
      <a-card class="welcome-card" :bordered="false">
        <div class="welcome-content">
          <div class="welcome-text">
            <h2 class="welcome-title">
              欢迎回来，{{ userName }}
            </h2>
            <p class="welcome-desc">
              翔泳社游泳馆管理系统 · {{ todayStr }}
            </p>
          </div>
          <div class="welcome-illustration">
            <svg viewBox="0 0 120 80" width="120" height="80" fill="none">
              <ellipse cx="60" cy="70" rx="55" ry="8" fill="rgba(0,180,216,0.1)" />
              <path d="M20 50c8-3 16 3 24 0s16-6 24-3 16 6 24 3" stroke="#00B4D8" stroke-width="2.5" stroke-linecap="round" fill="none" />
              <path d="M15 58c10-4 20 4 30 0s20-8 30-4 20 8 30 4" stroke="#90E0EF" stroke-width="2" stroke-linecap="round" fill="none" />
              <circle cx="60" cy="32" r="14" fill="#0077B6" opacity="0.15" />
              <path d="M54 28c1-4 11-4 12 0" stroke="#0077B6" stroke-width="2" stroke-linecap="round" />
              <circle cx="60" cy="34" r="3" fill="#0077B6" opacity="0.3" />
            </svg>
          </div>
        </div>
      </a-card>

      <!-- 统计卡片 -->
      <a-grid :cols="3" :col-gap="16" :row-gap="16" style="margin-top: 16px">
        <a-grid-item>
          <a-card class="stat-card" :bordered="false">
            <div class="stat-card-inner">
              <div class="stat-icon-wrap" style="background: rgba(0,119,182,0.1)">
                <icon-bar-chart style="color: #0077B6; font-size: 24px" />
              </div>
              <div class="stat-info">
                <div class="stat-label">订单总数</div>
                <div class="stat-value">{{ stats.orderCount }}</div>
              </div>
            </div>
          </a-card>
        </a-grid-item>
        <a-grid-item>
          <a-card class="stat-card" :bordered="false">
            <div class="stat-card-inner">
              <div class="stat-icon-wrap" style="background: rgba(0,180,216,0.1)">
                <icon-check-circle style="color: #00B4D8; font-size: 24px" />
              </div>
              <div class="stat-info">
                <div class="stat-label">已收金额</div>
                <div class="stat-value">¥{{ formatMoney(stats.paidAmount) }}</div>
              </div>
            </div>
          </a-card>
        </a-grid-item>
        <a-grid-item>
          <a-card class="stat-card" :bordered="false">
            <div class="stat-card-inner">
              <div class="stat-icon-wrap" style="background: rgba(144,224,239,0.2)">
                <icon-undo style="color: #0096C7; font-size: 24px" />
              </div>
              <div class="stat-info">
                <div class="stat-label">退款金额</div>
                <div class="stat-value">¥{{ formatMoney(stats.refundAmount) }}</div>
              </div>
            </div>
          </a-card>
        </a-grid-item>
      </a-grid>

      <!-- 图表区域 -->
      <a-grid :cols="24" :col-gap="16" :row-gap="16" style="margin-top: 16px">
        <a-grid-item :span="{ xs: 24, sm: 24, md: 24, lg: 16, xl: 16 }">
          <a-card class="general-card" title="近7日订单与预约趋势" :bordered="false">
            <Chart height="320px" :options="trendOption" />
          </a-card>
        </a-grid-item>
        <a-grid-item :span="{ xs: 24, sm: 24, md: 24, lg: 8, xl: 8 }">
          <a-card class="general-card" title="支付状态分布" :bordered="false">
            <Chart height="320px" :options="payStatusOption" />
          </a-card>
        </a-grid-item>

        <a-grid-item :span="24">
          <a-card class="general-card" title="预约状态分布" :bordered="false">
            <Chart height="320px" :options="reservationStatusOption" />
          </a-card>
        </a-grid-item>
      </a-grid>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
  import { computed, onMounted, reactive, ref } from 'vue';
  import dayjs from 'dayjs';
  import Chart from '@/components/chart/index.vue';
  import useChartOption from '@/hooks/chart-option';
  import { useUserStore } from '@/store';
  import {
    getStatsOverview,
    getDashboardStats,
    TrendPoint,
    StatusPoint,
  } from '@/api/swim';

  const userStore = useUserStore();
  const userName = computed(() => userStore.name || '管理员');
  const todayStr = dayjs().format('YYYY年MM月DD日 dddd');

  const loading = ref(false);
  const stats = reactive({
    orderCount: 0,
    paidAmount: 0,
    refundAmount: 0,
  });

  const formatMoney = (val: number) => {
    if (!val) return '0.00';
    return Number(val).toLocaleString('zh-CN', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
  };

  const orderTrend = ref<TrendPoint[]>([]);
  const reservationTrend = ref<TrendPoint[]>([]);
  const payStatus = ref<StatusPoint[]>([]);
  const reservationStatus = ref<StatusPoint[]>([]);

  const payStatusLabels = [
    { status: 0, name: '待支付' },
    { status: 1, name: '已支付' },
    { status: 2, name: '已退款' },
  ];
  const reservationStatusLabels = [
    { status: 1, name: '已预约' },
    { status: 2, name: '已取消' },
    { status: 3, name: '已到课' },
    { status: 4, name: '爽约' },
  ];

  const buildStatusData = (
    raw: StatusPoint[],
    labels: { status: number; name: string }[]
  ) => {
    const map = new Map<number, number>();
    raw.forEach((item) => {
      map.set(item.status, item.value);
    });
    return labels.map((item) => ({
      name: item.name,
      value: map.get(item.status) || 0,
    }));
  };

  const trendDates = computed(() => orderTrend.value.map((item) => item.date));
  const orderSeries = computed(() => orderTrend.value.map((item) => item.value));
  const reservationSeries = computed(() => {
    const map = new Map(
      reservationTrend.value.map((item) => [item.date, item.value])
    );
    return trendDates.value.map((date) => map.get(date) || 0);
  });

  const payStatusChartData = computed(() =>
    buildStatusData(payStatus.value, payStatusLabels)
  );
  const reservationStatusChartData = computed(() =>
    buildStatusData(reservationStatus.value, reservationStatusLabels)
  );

  // 水蓝色系颜色
  const COLORS = ['#0077B6', '#00B4D8', '#90E0EF', '#CAF0F8'];

  const { chartOption: trendOption } = useChartOption((isDark) => ({
    tooltip: { trigger: 'axis' },
    legend: {
      data: ['订单', '预约'],
      top: 0,
      textStyle: { color: isDark ? '#C9CDD4' : '#4E5969' },
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '12%',
      containLabel: true,
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: trendDates.value,
      axisLabel: {
        color: isDark ? '#C9CDD4' : '#4E5969',
      },
      axisLine: {
        lineStyle: {
          color: isDark ? '#3C3C3C' : '#E5E8EF',
        },
      },
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      axisLabel: {
        color: isDark ? '#C9CDD4' : '#4E5969',
      },
      splitLine: {
        lineStyle: {
          color: isDark ? '#2E333B' : '#E5E8EF',
        },
      },
    },
    series: [
      {
        name: '订单',
        type: 'line',
        smooth: true,
        data: orderSeries.value,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { width: 3, color: COLORS[0] },
        itemStyle: { color: COLORS[0] },
        areaStyle: { opacity: 0.15, color: COLORS[0] },
      },
      {
        name: '预约',
        type: 'line',
        smooth: true,
        data: reservationSeries.value,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { width: 3, color: COLORS[1] },
        itemStyle: { color: COLORS[1] },
        areaStyle: { opacity: 0.12, color: COLORS[1] },
      },
    ],
  }));

  const { chartOption: payStatusOption } = useChartOption((isDark) => ({
    tooltip: { trigger: 'item' },
    legend: {
      bottom: 0,
      textStyle: { color: isDark ? '#C9CDD4' : '#4E5969' },
    },
    color: COLORS,
    series: [
      {
        name: '支付状态',
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['50%', '45%'],
        label: { formatter: '{b}: {c}' },
        data: payStatusChartData.value,
      },
    ],
  }));

  const { chartOption: reservationStatusOption } = useChartOption((isDark) => ({
    tooltip: { trigger: 'axis' },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '12%',
      containLabel: true,
    },
    xAxis: {
      type: 'category',
      data: reservationStatusChartData.value.map((item) => item.name),
      axisLabel: {
        color: isDark ? '#C9CDD4' : '#4E5969',
      },
      axisLine: {
        lineStyle: {
          color: isDark ? '#3C3C3C' : '#E5E8EF',
        },
      },
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      axisLabel: {
        color: isDark ? '#C9CDD4' : '#4E5969',
      },
      splitLine: {
        lineStyle: {
          color: isDark ? '#2E333B' : '#E5E8EF',
        },
      },
    },
    series: [
      {
        type: 'bar',
        barWidth: 32,
        data: reservationStatusChartData.value.map((item, index) => ({
          value: item.value,
          itemStyle: { color: COLORS[index % COLORS.length] },
        })),
      },
    ],
  }));

  const fetchStats = async () => {
    loading.value = true;
    try {
      const [overviewRes, dashboardRes] = await Promise.all([
        getStatsOverview(),
        getDashboardStats(),
      ]);
      stats.orderCount = Number(overviewRes.data.orderCount || 0);
      stats.paidAmount = Number(overviewRes.data.paidAmount || 0);
      stats.refundAmount = Number(overviewRes.data.refundAmount || 0);

      orderTrend.value = dashboardRes.data.orderTrend || [];
      reservationTrend.value = dashboardRes.data.reservationTrend || [];
      payStatus.value = dashboardRes.data.payStatus || [];
      reservationStatus.value = dashboardRes.data.reservationStatus || [];
    } finally {
      loading.value = false;
    }
  };

  onMounted(fetchStats);
</script>

<script lang="ts">
  export default {
    name: 'SwimDashboard',
  };
</script>

<style scoped>
  .dashboard-wrapper {
    padding: 0;
  }

  .welcome-card {
    border-radius: 8px;
    background: linear-gradient(135deg, #023e8a 0%, #0077B6 50%, #00B4D8 100%);
  }

  .welcome-card :deep(.arco-card-body) {
    padding: 28px 32px;
  }

  .welcome-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .welcome-title {
    font-size: 22px;
    font-weight: 600;
    color: #fff;
    margin: 0 0 8px;
  }

  .welcome-desc {
    font-size: 14px;
    color: rgba(255, 255, 255, 0.75);
    margin: 0;
  }

  .welcome-illustration {
    flex-shrink: 0;
    opacity: 0.9;
  }

  .stat-card {
    border-radius: 8px;
    transition: transform 0.2s, box-shadow 0.2s;
  }

  .stat-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  }

  .stat-card-inner {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .stat-icon-wrap {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .stat-label {
    font-size: 13px;
    color: var(--color-text-3);
    margin-bottom: 4px;
  }

  .stat-value {
    font-size: 22px;
    font-weight: 700;
    color: var(--color-text-1);
  }

  .general-card {
    border-radius: 8px;
  }

  @media (max-width: 768px) {
    .welcome-illustration {
      display: none;
    }
  }
</style>
