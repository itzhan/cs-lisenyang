<template>
  <div class="page-wrapper">
    <a-card class="general-card" title="系统参数">
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
          <template #actions="{ record }">
            <a-space>
              <a-button size="mini" @click="openEdit(record)">编辑</a-button>
              <a-popconfirm content="确认删除该参数？" @ok="remove(record.id)">
                <a-button size="mini" status="danger">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-space>
    </a-card>

    <a-modal
      v-model:visible="visible"
      :title="isEdit ? '编辑参数' : '新增参数'"
      @ok="handleOk"
    >
      <a-form :model="form" layout="vertical">
        <a-form-item
          field="paramKey"
          label="参数键"
          :rules="[{ required: true, message: '请输入参数键' }]"
        >
          <a-input v-model="form.paramKey" :disabled="isEdit" />
        </a-form-item>
        <a-form-item
          field="paramValue"
          label="参数值"
          :rules="[{ required: true, message: '请输入参数值' }]"
        >
          <a-input v-model="form.paramValue" />
        </a-form-item>
        <a-form-item field="remark" label="备注">
          <a-input v-model="form.remark" />
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
    listParams,
    createParam,
    updateParam,
    deleteParam,
    SysParam,
  } from '@/api/swim';

  const list = ref<SysParam[]>([]);
  const loading = ref(false);
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '参数键', dataIndex: 'paramKey' },
    { title: '参数值', dataIndex: 'paramValue' },
    { title: '备注', dataIndex: 'remark' },
    { title: '状态', dataIndex: 'status', width: 80 },
    { title: '操作', slotName: 'actions', width: 160 },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listParams({
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
    paramKey: '',
    paramValue: '',
    remark: '',
    status: 1,
  });

  const openCreate = () => {
    isEdit.value = false;
    Object.assign(form, {
      id: undefined,
      paramKey: '',
      paramValue: '',
      remark: '',
      status: 1,
    });
    visible.value = true;
  };

  const openEdit = (record: SysParam) => {
    isEdit.value = true;
    Object.assign(form, { ...record });
    visible.value = true;
  };

  const handleOk = async () => {
    if (isEdit.value) {
      await updateParam(form.id, { ...form });
      Message.success('更新成功');
    } else {
      await createParam({ ...form });
      Message.success('创建成功');
    }
    visible.value = false;
    fetchList();
  };

  const remove = async (id: number) => {
    await deleteParam(id);
    Message.success('删除成功');
    fetchList();
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimParams',
  };
</script>
