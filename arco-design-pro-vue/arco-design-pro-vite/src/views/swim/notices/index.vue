<template>
  <div class="page-wrapper">
    <a-card class="general-card" title="公告管理">
      <a-space direction="vertical" fill>
        <a-space>
          <a-button type="primary" @click="openCreate">新增</a-button>
        </a-space>
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
              {{ record.status === 1 ? '已发布' : '草稿' }}
            </a-tag>
          </template>
          <template #actions="{ record }">
            <a-space>
              <a-button size="mini" @click="openEdit(record)">编辑</a-button>
              <a-popconfirm content="确认删除该公告？" @ok="remove(record.id)">
                <a-button size="mini" status="danger">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-space>
    </a-card>

    <a-modal
      v-model:visible="visible"
      :title="isEdit ? '编辑公告' : '新增公告'"
      @ok="handleOk"
    >
      <a-form :model="form" layout="vertical">
        <a-form-item
          field="title"
          label="标题"
          :rules="[{ required: true, message: '请输入标题' }]"
        >
          <a-input v-model="form.title" />
        </a-form-item>
        <a-form-item
          field="content"
          label="内容"
          :rules="[{ required: true, message: '请输入内容' }]"
        >
          <a-textarea v-model="form.content" />
        </a-form-item>
        <a-form-item field="publishTime" label="发布时间">
          <a-date-picker
            v-model="form.publishTime"
            show-time
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item field="status" label="状态">
          <a-select v-model="form.status">
            <a-option :value="0">草稿</a-option>
            <a-option :value="1">发布</a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import {
    listNotices,
    createNotice,
    updateNotice,
    deleteNotice,
    Notice,
  } from '@/api/swim';

  const list = ref<Notice[]>([]);
  const loading = ref(false);
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '标题', dataIndex: 'title' },
    { title: '发布时间', dataIndex: 'publishTime' },
    { title: '状态', slotName: 'status', width: 100 },
    { title: '操作', slotName: 'actions', width: 160 },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listNotices({
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
  const isEdit = ref(false);
  const form = reactive<any>({
    id: undefined,
    title: '',
    content: '',
    publishTime: '',
    status: 1,
  });

  const openCreate = () => {
    isEdit.value = false;
    Object.assign(form, {
      id: undefined,
      title: '',
      content: '',
      publishTime: '',
      status: 1,
    });
    visible.value = true;
  };

  const openEdit = (record: Notice) => {
    isEdit.value = true;
    Object.assign(form, { ...record });
    visible.value = true;
  };

  const handleOk = async () => {
    if (isEdit.value) {
      await updateNotice(form.id, { ...form });
      Message.success('更新成功');
    } else {
      await createNotice({ ...form });
      Message.success('创建成功');
    }
    visible.value = false;
    fetchList();
  };

  const remove = async (id: number) => {
    await deleteNotice(id);
    Message.success('删除成功');
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimNotices',
  };
</script>
