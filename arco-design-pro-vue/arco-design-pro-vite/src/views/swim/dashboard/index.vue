<template>
  <div class="page-wrapper">
    <a-spin :loading="loading" style="width: 100%">
      <a-grid :cols="24" :col-gap="16" :row-gap="16">
        <a-grid-item :span="24">
          <a-card class="general-card" title="运营概览">
            <a-space direction="horizontal" size="large" wrap>
              <a-statistic title="订单总数" :value="stats.orderCount" />
              <a-statistic
                title="已收金额"
                :value="stats.paidAmount"
                :precision="2"
              />
              <a-statistic
                title="退款金额"
                :value="stats.refundAmount"
                :precision="2"
              />
            </a-space>
          </a-card>
        </a-grid-item>

        <a-grid-item :span="{ xs: 24, sm: 24, md: 24, lg: 16, xl: 16 }">
          <a-card class="general-card" title="近7日订单与预约趋势">
            <Chart height="320px" :options="trendOption" />
          </a-card>
        </a-grid-item>
        <a-grid-item :span="{ xs: 24, sm: 24, md: 24, lg: 8, xl: 8 }">
          <a-card class="general-card" title="支付状态分布">
            <Chart height="320px" :options="payStatusOption" />
          </a-card>
        </a-grid-item>

        <a-grid-item :span="24">
          <a-card class="general-card" title="预约状态分布">
            <Chart height="320px" :options="reservationStatusOption" />
          </a-card>
        </a-grid-item>
      </a-grid>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
  import { computed, onMounted, reactive, ref } from 'vue';
  import Chart from '@/components/chart/index.vue';
  import useChartOption from '@/hooks/chart-option';
  import {
    getStatsOverview,
    getDashboardStats,
    TrendPoint,
    StatusPoint,
  } from '@/api/swim';

  const loading = ref(false);
  const stats = reactive({
    orderCount: 0,
    paidAmount: 0,
    refundAmount: 0,
  });

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
        lineStyle: { width: 3 },
        areaStyle: { opacity: 0.15 },
      },
      {
        name: '预约',
        type: 'line',
        smooth: true,
        data: reservationSeries.value,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { width: 3 },
        areaStyle: { opacity: 0.12 },
      },
    ],
  }));

  const { chartOption: payStatusOption } = useChartOption((isDark) => ({
    tooltip: { trigger: 'item' },
    legend: {
      bottom: 0,
      textStyle: { color: isDark ? '#C9CDD4' : '#4E5969' },
    },
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
        data: reservationStatusChartData.value.map((item) => item.value),
        itemStyle: {
          color: '#4F7BFF',
        },
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
