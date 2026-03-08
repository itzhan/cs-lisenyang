<template>
  <div class="page-wrapper">
    <a-card class="general-card" title="反馈管理">
      <a-space direction="vertical" fill>
        <a-table
          row-key="id"
          :loading="loading"
          :columns="columns"
          :data="list"
          :pagination="pagination"
          @page-change="onPageChange"
        >
          <template #status="{ record }">
            <a-tag :color="record.status === 1 ? 'green' : 'orange'">
              {{ record.status === 1 ? '已处理' : '未处理' }}
            </a-tag>
          </template>
          <template #actions="{ record }">
            <a-button size="mini" @click="openReply(record)">回复</a-button>
          </template>
        </a-table>
      </a-space>
    </a-card>

    <a-modal v-model:visible="visible" title="回复反馈" @ok="handleReply">
      <a-form :model="form" layout="vertical">
        <a-form-item label="回复内容">
          <a-textarea v-model="form.replyContent" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import { listFeedbacks, replyFeedback, Feedback } from '@/api/swim';

  const list = ref<Feedback[]>([]);
  const loading = ref(false);
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '用户ID', dataIndex: 'userId' },
    { title: '类型', dataIndex: 'type' },
    { title: '内容', dataIndex: 'content' },
    { title: '状态', slotName: 'status', width: 100 },
    { title: '回复内容', dataIndex: 'replyContent' },
    { title: '回复时间', dataIndex: 'replyTime' },
    { title: '操作', slotName: 'actions', width: 120 },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listFeedbacks({
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

  const visible = ref(false);
  const form = reactive({ id: 0, replyContent: '' });

  const openReply = (record: Feedback) => {
    form.id = record.id as number;
    form.replyContent = record.replyContent || '';
    visible.value = true;
  };

  const handleReply = async () => {
    await replyFeedback(form.id, form.replyContent);
    Message.success('回复成功');
    visible.value = false;
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimFeedbacks',
  };
</script>
