<template>
  <div class="page-wrapper">
    <a-card class="general-card" title="教练管理">
      <a-space direction="vertical" fill>
        <a-space>
          <a-input v-model="keyword" placeholder="资质/擅长/简介" allow-clear />
          <a-button type="primary" @click="fetchList">查询</a-button>
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
          <template #actions="{ record }">
            <a-space>
              <a-button size="mini" @click="openEdit(record)">编辑</a-button>
              <a-popconfirm content="确认删除该教练？" @ok="remove(record.id)">
                <a-button size="mini" status="danger">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-space>
    </a-card>

    <a-modal
      v-model:visible="visible"
      :title="isEdit ? '编辑教练' : '新增教练'"
      @ok="handleOk"
    >
      <a-form :model="form" layout="vertical">
        <a-form-item
          field="userId"
          label="教练账号ID"
          :rules="[{ required: true, message: '请输入用户ID' }]"
        >
          <a-input-number v-model="form.userId" :disabled="isEdit" />
        </a-form-item>
        <a-form-item field="qualification" label="资质">
          <a-input v-model="form.qualification" />
        </a-form-item>
        <a-form-item field="specialty" label="擅长项目">
          <a-input v-model="form.specialty" />
        </a-form-item>
        <a-form-item field="intro" label="介绍">
          <a-textarea v-model="form.intro" />
        </a-form-item>
        <a-form-item field="avatarUrl" label="头像URL">
          <a-input v-model="form.avatarUrl" />
        </a-form-item>
        <a-form-item field="status" label="状态">
          <a-select v-model="form.status">
            <a-option :value="1">启用</a-option>
            <a-option :value="0">禁用</a-option>
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
    listCoaches,
    createCoach,
    updateCoach,
    deleteCoach,
    CoachProfile,
  } from '@/api/swim';

  const list = ref<CoachProfile[]>([]);
  const loading = ref(false);
  const keyword = ref('');
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '账号ID', dataIndex: 'userId', width: 100 },
    { title: '资质', dataIndex: 'qualification' },
    { title: '擅长项目', dataIndex: 'specialty' },
    { title: '介绍', dataIndex: 'intro' },
    { title: '头像', dataIndex: 'avatarUrl' },
    { title: '操作', slotName: 'actions', width: 160 },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listCoaches({
        page: pagination.current,
        size: pagination.pageSize,
        keyword: keyword.value || undefined,
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
    userId: undefined,
    qualification: '',
    specialty: '',
    intro: '',
    avatarUrl: '',
    status: 1,
  });

  const openCreate = () => {
    isEdit.value = false;
    Object.assign(form, {
      id: undefined,
      userId: undefined,
      qualification: '',
      specialty: '',
      intro: '',
      avatarUrl: '',
      status: 1,
    });
    visible.value = true;
  };

  const openEdit = (record: CoachProfile) => {
    isEdit.value = true;
    Object.assign(form, { ...record });
    visible.value = true;
  };

  const handleOk = async () => {
    if (isEdit.value) {
      await updateCoach(form.id, {
        userId: form.userId,
        qualification: form.qualification,
        specialty: form.specialty,
        intro: form.intro,
        avatarUrl: form.avatarUrl,
        status: form.status,
      });
      Message.success('更新成功');
    } else {
      await createCoach({
        userId: form.userId,
        qualification: form.qualification,
        specialty: form.specialty,
        intro: form.intro,
        avatarUrl: form.avatarUrl,
        status: form.status,
      });
      Message.success('创建成功');
    }
    visible.value = false;
    fetchList();
  };

  const remove = async (id: number) => {
    await deleteCoach(id);
    Message.success('删除成功');
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimCoaches',
  };
</script>
