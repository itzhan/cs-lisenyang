<template>
  <a-card class="general-card" title="订单管理">
    <a-space direction="vertical" fill>
      <a-table
        row-key="id"
        :loading="loading"
        :columns="columns"
        :data="list"
        :pagination="pagination"
        @page-change="onPageChange"
      >
        <template #payStatus="{ record }">
          {{ payStatusText(record.payStatus) }}
        </template>
        <template #actions="{ record }">
          <a-space>
            <a-select
              v-model="record._payStatus"
              size="mini"
              style="width: 120px"
              @change="(val: number | string | boolean | Record<string, any> | (string | number | boolean | Record<string, any>)[] | undefined) => updatePay(record.id, val as number)"
            >
              <a-option :value="0">待支付</a-option>
              <a-option :value="1">已支付</a-option>
              <a-option :value="2">已退款</a-option>
            </a-select>
            <a-button
              size="mini"
              status="warning"
              :disabled="record.payStatus !== 1"
              @click="doRefund(record.id)"
            >
              退款
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-space>
  </a-card>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import {
    listOrders,
    updateOrderPayStatus,
    refundOrder,
    OrderInfo,
  } from '@/api/swim';

  const list = ref<(OrderInfo & { _payStatus?: number })[]>([]);
  const loading = ref(false);
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '订单号', dataIndex: 'orderNo' },
    { title: '用户ID', dataIndex: 'userId' },
    { title: '预约ID', dataIndex: 'reservationId' },
    { title: '金额', dataIndex: 'amount' },
    { title: '支付状态', slotName: 'payStatus', width: 100 },
    { title: '支付时间', dataIndex: 'payTime' },
    { title: '退款时间', dataIndex: 'refundTime' },
    { title: '操作', slotName: 'actions', width: 200 },
  ];

  const payStatusText = (status?: number) => {
    switch (status) {
      case 0:
        return '待支付';
      case 1:
        return '已支付';
      case 2:
        return '已退款';
      default:
        return '-';
    }
  };

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listOrders({
        page: pagination.current,
        size: pagination.pageSize,
      });
      list.value = (data.records || []).map((item) => ({
        ...item,
        _payStatus: item.payStatus,
      }));
      pagination.total = data.total || 0;
    } finally {
      loading.value = false;
    }
  };

  const onPageChange = (page: number) => {
    pagination.current = page;
    fetchList();
  };

  const updatePay = async (id: number, payStatus: number) => {
    await updateOrderPayStatus(id, payStatus);
    Message.success('状态已更新');
    fetchList();
  };

  const doRefund = async (id: number) => {
    await refundOrder(id);
    Message.success('退款成功');
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimOrders',
  };
</script>
