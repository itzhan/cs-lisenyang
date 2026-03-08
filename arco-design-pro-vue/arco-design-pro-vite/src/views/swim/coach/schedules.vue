<template>
  <a-card class="general-card" title="我的排课">
    <a-table
      row-key="id"
      :loading="loading"
      :columns="columns"
      :data="list"
      :pagination="pagination"
      @page-change="onPageChange"
    />
  </a-card>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { listCoachSchedules, CourseSchedule } from '@/api/swim';

  const list = ref<CourseSchedule[]>([]);
  const loading = ref(false);
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '课程ID', dataIndex: 'courseId' },
    { title: '日期', dataIndex: 'courseDate' },
    { title: '开始时间', dataIndex: 'startTime' },
    { title: '结束时间', dataIndex: 'endTime' },
    { title: '地点', dataIndex: 'location' },
    { title: '容量', dataIndex: 'maxCapacity' },
    { title: '已约', dataIndex: 'currentCount' },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listCoachSchedules({
        page: pagination.current,
        size: pagination.pageSize,
      });
      list.value = data.records || [];
      pagination.total = data.total || 0;
    } finally {
      loading.value = false;
    }
  };

  const onPageChange = (page: number) => {
    pagination.current = page;
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'CoachSchedules',
  };
</script>
