<template>
  <div class="page-wrapper">
    <a-card class="general-card" title="用户管理">
      <a-space direction="vertical" fill>
        <a-space>
          <a-input v-model="keyword" placeholder="用户名/姓名" allow-clear />
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
          <template #status="{ record }">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? '启用' : '禁用' }}
            </a-tag>
          </template>
          <template #actions="{ record }">
            <a-space>
              <a-button size="mini" @click="openEdit(record)">编辑</a-button>
              <a-popconfirm content="确认删除该用户？" @ok="remove(record.id)">
                <a-button size="mini" status="danger">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-space>
    </a-card>

    <a-modal
      v-model:visible="visible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      @ok="handleOk"
    >
      <a-form :model="form" layout="vertical">
        <a-form-item
          field="username"
          label="用户名"
          :rules="[{ required: true, message: '请输入用户名' }]"
        >
          <a-input v-model="form.username" :disabled="isEdit" />
        </a-form-item>
        <a-form-item
          v-if="!isEdit"
          field="password"
          label="密码"
          :rules="[{ required: true, message: '请输入密码' }]"
        >
          <a-input v-model="form.password" />
        </a-form-item>
        <a-form-item field="realName" label="姓名">
          <a-input v-model="form.realName" />
        </a-form-item>
        <a-form-item field="phone" label="手机号">
          <a-input v-model="form.phone" />
        </a-form-item>
        <a-form-item field="email" label="邮箱">
          <a-input v-model="form.email" />
        </a-form-item>
        <a-form-item v-if="!isEdit" field="roleKey" label="角色">
          <a-select v-model="form.roleKey" placeholder="请选择角色">
            <a-option value="ADMIN">管理员</a-option>
            <a-option value="USER">用户</a-option>
            <a-option value="COACH">教练</a-option>
          </a-select>
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
    listUsers,
    createUser,
    updateUser,
    deleteUser,
    SysUser,
  } from '@/api/swim';

  const list = ref<SysUser[]>([]);
  const loading = ref(false);
  const keyword = ref('');
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '用户名', dataIndex: 'username' },
    { title: '姓名', dataIndex: 'realName' },
    { title: '手机号', dataIndex: 'phone' },
    { title: '邮箱', dataIndex: 'email' },
    { title: '状态', slotName: 'status', width: 100 },
    { title: '操作', slotName: 'actions', width: 160 },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listUsers({
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
    username: '',
    password: '',
    realName: '',
    phone: '',
    email: '',
    roleKey: 'USER',
    status: 1,
  });

  const openCreate = () => {
    isEdit.value = false;
    Object.assign(form, {
      id: undefined,
      username: '',
      password: '',
      realName: '',
      phone: '',
      email: '',
      roleKey: 'USER',
      status: 1,
    });
    visible.value = true;
  };

  const openEdit = (record: SysUser) => {
    isEdit.value = true;
    Object.assign(form, {
      id: record.id,
      username: record.username,
      realName: record.realName,
      phone: record.phone,
      email: record.email,
      status: record.status ?? 1,
    });
    visible.value = true;
  };

  const handleOk = async () => {
    if (isEdit.value) {
      await updateUser(form.id, {
        realName: form.realName,
        phone: form.phone,
        email: form.email,
        status: form.status,
      });
      Message.success('更新成功');
    } else {
      await createUser({
        username: form.username,
        password: form.password,
        realName: form.realName,
        phone: form.phone,
        email: form.email,
        roleKey: form.roleKey,
      });
      Message.success('创建成功');
    }
    visible.value = false;
    fetchList();
  };

  const remove = async (id: number) => {
    await deleteUser(id);
    Message.success('删除成功');
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimUsers',
  };
</script>
