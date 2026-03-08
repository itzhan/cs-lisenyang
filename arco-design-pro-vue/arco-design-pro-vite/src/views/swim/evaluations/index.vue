<template>
  <a-card class="general-card" title="评价管理">
    <a-space direction="vertical" fill>
      <a-table
        row-key="id"
        :loading="loading"
        :columns="columns"
        :data="list"
        :pagination="pagination"
        @page-change="onPageChange"
      >
        <template #actions="{ record }">
          <a-popconfirm content="确认删除该评价？" @ok="remove(record.id)">
            <a-button size="mini" status="danger">删除</a-button>
          </a-popconfirm>
        </template>
      </a-table>
    </a-space>
  </a-card>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import { listEvaluations, deleteEvaluation, Evaluation } from '@/api/swim';

  const list = ref<Evaluation[]>([]);
  const loading = ref(false);
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '用户ID', dataIndex: 'userId' },
    { title: '课程ID', dataIndex: 'courseId' },
    { title: '预约ID', dataIndex: 'reservationId' },
    { title: '评分', dataIndex: 'score' },
    { title: '内容', dataIndex: 'content' },
    { title: '创建时间', dataIndex: 'createTime' },
    { title: '操作', slotName: 'actions', width: 120 },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listEvaluations({
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

  const remove = async (id: number) => {
    await deleteEvaluation(id);
    Message.success('删除成功');
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimEvaluations',
  };
</script>
