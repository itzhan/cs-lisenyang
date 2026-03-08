<template>
  <a-card class="general-card" title="我的预约">
    <a-table
      row-key="id"
      :loading="loading"
      :columns="columns"
      :data="list"
      :pagination="pagination"
      @page-change="onPageChange"
    >
      <template #status="{ record }">
        {{ statusText(record.status) }}
      </template>
      <template #actions="{ record }">
        <a-select
          v-model="record._status"
          size="mini"
          style="width: 120px"
          @change="(val: number | string | boolean | Record<string, any> | (string | number | boolean | Record<string, any>)[] | undefined) => updateStatus(record.id, val as number)"
        >
          <a-option :value="1">已预约</a-option>
          <a-option :value="2">已取消</a-option>
          <a-option :value="3">已到课</a-option>
          <a-option :value="4">爽约</a-option>
        </a-select>
      </template>
    </a-table>
  </a-card>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import { listCoachReservations, updateCoachReservationStatus, Reservation } from '@/api/swim';

  const list = ref<(Reservation & { _status?: number })[]>([]);
  const loading = ref(false);
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '排课ID', dataIndex: 'scheduleId' },
    { title: '用户ID', dataIndex: 'userId' },
    { title: '预约时间', dataIndex: 'reserveTime' },
    { title: '状态', slotName: 'status', width: 100 },
    { title: '操作', slotName: 'actions', width: 140 },
  ];

  const statusText = (status?: number) => {
    switch (status) {
      case 1:
        return '已预约';
      case 2:
        return '已取消';
      case 3:
        return '已到课';
      case 4:
        return '爽约';
      default:
        return '-';
    }
  };

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listCoachReservations({
        page: pagination.current,
        size: pagination.pageSize,
      });
      list.value = (data.records || []).map((item) => ({
        ...item,
        _status: item.status,
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

  const updateStatus = async (id: number, status: number) => {
    await updateCoachReservationStatus(id, status);
    Message.success('状态已更新');
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'CoachReservations',
  };
</script>
