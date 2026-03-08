<template>
  <div class="page-wrapper">
    <a-card class="general-card" title="课程管理">
      <a-space direction="vertical" fill>
        <a-space>
          <a-input v-model="keyword" placeholder="课程名称" allow-clear />
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
              <a-popconfirm content="确认删除该课程？" @ok="remove(record.id)">
                <a-button size="mini" status="danger">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-space>
    </a-card>

    <a-modal
      v-model:visible="visible"
      :title="isEdit ? '编辑课程' : '新增课程'"
      @ok="handleOk"
    >
      <a-form :model="form" layout="vertical">
        <a-form-item
          field="courseName"
          label="课程名称"
          :rules="[{ required: true, message: '请输入课程名称' }]"
        >
          <a-input v-model="form.courseName" />
        </a-form-item>
        <a-form-item field="courseType" label="课程类型">
          <a-input v-model="form.courseType" />
        </a-form-item>
        <a-form-item field="level" label="等级">
          <a-input v-model="form.level" />
        </a-form-item>
        <a-form-item field="durationMin" label="时长(分钟)">
          <a-input-number v-model="form.durationMin" />
        </a-form-item>
        <a-form-item field="price" label="价格">
          <a-input-number v-model="form.price" :precision="2" />
        </a-form-item>
        <a-form-item field="capacity" label="容量">
          <a-input-number v-model="form.capacity" />
        </a-form-item>
        <a-form-item field="coverUrl" label="封面URL">
          <a-input v-model="form.coverUrl" />
        </a-form-item>
        <a-form-item label="上传封面">
          <a-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            @success="onUploadSuccess"
          >
            <a-button>上传文件</a-button>
          </a-upload>
        </a-form-item>
        <a-form-item field="description" label="描述">
          <a-textarea v-model="form.description" />
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
  import { computed, reactive, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import {
    listCourses,
    createCourse,
    updateCourse,
    deleteCourse,
    Course,
  } from '@/api/swim';
  import { getToken } from '@/utils/auth';

  const list = ref<Course[]>([]);
  const loading = ref(false);
  const keyword = ref('');
  const pagination = reactive({ current: 1, pageSize: 10, total: 0 });

  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '课程名称', dataIndex: 'courseName' },
    { title: '类型', dataIndex: 'courseType' },
    { title: '等级', dataIndex: 'level' },
    { title: '时长', dataIndex: 'durationMin' },
    { title: '价格', dataIndex: 'price' },
    { title: '容量', dataIndex: 'capacity' },
    { title: '操作', slotName: 'actions', width: 160 },
  ];

  const fetchList = async () => {
    loading.value = true;
    try {
      const { data } = await listCourses({
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
    courseName: '',
    courseType: '',
    level: '',
    durationMin: 60,
    price: 0,
    capacity: 20,
    coverUrl: '',
    description: '',
    status: 1,
  });

  const openCreate = () => {
    isEdit.value = false;
    Object.assign(form, {
      id: undefined,
      courseName: '',
      courseType: '',
      level: '',
      durationMin: 60,
      price: 0,
      capacity: 20,
      coverUrl: '',
      description: '',
      status: 1,
    });
    visible.value = true;
  };

  const openEdit = (record: Course) => {
    isEdit.value = true;
    Object.assign(form, { ...record });
    visible.value = true;
  };

  const handleOk = async () => {
    if (isEdit.value) {
      await updateCourse(form.id, { ...form });
      Message.success('更新成功');
    } else {
      await createCourse({ ...form });
      Message.success('创建成功');
    }
    visible.value = false;
    fetchList();
  };

  const remove = async (id: number) => {
    await deleteCourse(id);
    Message.success('删除成功');
    fetchList();
  };

  const uploadUrl = computed(() => {
    return `${import.meta.env.VITE_API_BASE_URL || ''}/api/admin/files/upload`;
  });
  const uploadHeaders = computed(() => ({
    Authorization: `Bearer ${getToken() || ''}`,
  }));

  const onUploadSuccess = (res: any) => {
    const response = res?.response || res;
    if (response?.data?.fileUrl) {
      form.coverUrl = response.data.fileUrl;
      Message.success('上传成功');
    }
  };

  fetchList();
</script>

<script lang="ts">
  export default {
    name: 'SwimCourses',
  };
</script>
