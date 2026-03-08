<template>
  <div class="page-wrapper">
    <a-card class="general-card" title="排课管理">
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
              <a-button size="mini" @click="openBind(record)">协同教练</a-button>
              <a-popconfirm content="确认删除该排课？" @ok="remove(record.id)">
                <a-button size="mini" status="danger">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-space>
    </a-card>

    <a-modal
      v-model:visible="visible"
      :title="isEdit ? '编辑排课' : '新增排课'"
      @ok="handleOk"
    >
      <a-form :model="form" layout="vertical">
        <a-form-item
          field="courseId"
          label="课程ID"
          :rules="[{ required: true, message: '请输入课程ID' }]"
        >
          <a-input-number v-model="form.courseId" />
        </a-form-item>
        <a-form-item field="coachId" label="主教练ID">
          <a-input-number v-model="form.coachId" />
        </a-form-item>
        <a-form-item
          field="courseDate"
          label="日期"
          :rules="[{ required: true, message: '请选择日期' }]"
        >
          <a-date-picker v-model="form.courseDate" value-format="YYYY-MM-DD" />
        </a-form-item>
        <a-form-item
          field="startTime"
          label="开始时间"
          :rules="[{ required: true, message: '请选择开始时间' }]"
        >
          <a-date-picker
            v-model="form.startTime"
            show-time
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item
          field="endTime"
          label="结束时间"
          :rules="[{ required: true, message: '请选择结束时间' }]"
        >
          <a-date-picker
            v-model="form.endTime"
            show-time
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item field="location" label="地点">
          <a-input v-model="form.location" />
        </a-form-item>
        <a-form-item field="maxCapacity" label="最大容量">
          <a-input-number v-model="form.maxCapacity" />
        </a-form-item>
        <a-form-item field="status" label="状态">
          <a-select v-model="form.status">
            <a-option :value="1">可预约</a-option>
            <a-option :value="0">停用</a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
      v-model:visible="bindVisible"
      title="协同教练绑定"
      @ok="handleBindOk"
    >
      <a-form :model="bindForm" layout="vertical">
        <a-form-item label="教练ID列表（逗号分隔）">
          <a-textarea
            v-model="bindForm.coachIdsText"
            placeholder="例如：1,2,3"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import {
    listSchedules,
    createSchedule,
    updateSchedule,
    deleteSchedule,
    updateScheduleCoaches,
    CourseSchedule,
  } from '@/api/swim';

  const list = ref<CourseSchedule[]>([]);
  const loading = ref(false);
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '课程ID', dataIndex: 'courseId' },
    { title: '主教练ID', dataIndex: 'coachId' },
    { title: '日期', dataIndex: 'courseDate' },
    { title: '开始时间', dataIndex: 'startTime' },
    { title: '结束时间', dataIndex: 'endTime' },
    { title: '地点', dataIndex: 'location' },
    { title: '容量', dataIndex: 'maxCapacity' },
    { title: '已约', dataIndex: 'currentCount' },
    { title: '操作', slotName: 'actions', width: 220 },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listSchedules({
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
    courseId: undefined,
    coachId: undefined,
    courseDate: '',
    startTime: '',
    endTime: '',
    location: '',
    maxCapacity: 20,
    status: 1,
  });

  const openCreate = () => {
    isEdit.value = false;
    Object.assign(form, {
      id: undefined,
      courseId: undefined,
      coachId: undefined,
      courseDate: '',
      startTime: '',
      endTime: '',
      location: '',
      maxCapacity: 20,
      status: 1,
    });
    visible.value = true;
  };

  const openEdit = (record: CourseSchedule) => {
    isEdit.value = true;
    Object.assign(form, { ...record });
    visible.value = true;
  };

  const handleOk = async () => {
    if (isEdit.value) {
      await updateSchedule(form.id, { ...form });
      Message.success('更新成功');
    } else {
      await createSchedule({ ...form });
      Message.success('创建成功');
    }
    visible.value = false;
    fetchList();
  };

  const remove = async (id: number) => {
    await deleteSchedule(id);
    Message.success('删除成功');
    fetchList();
  };

  const bindVisible = ref(false);
  const bindForm = reactive({
    scheduleId: 0,
    coachIdsText: '',
  });
  const openBind = (record: CourseSchedule) => {
    bindForm.scheduleId = record.id as number;
    bindForm.coachIdsText = '';
    bindVisible.value = true;
  };
  const handleBindOk = async () => {
    const ids = bindForm.coachIdsText
      .split(',')
      .map((v) => Number(v.trim()))
      .filter((v) => !Number.isNaN(v));
    await updateScheduleCoaches(bindForm.scheduleId, ids);
    Message.success('绑定成功');
    bindVisible.value = false;
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimSchedules',
  };
</script>
