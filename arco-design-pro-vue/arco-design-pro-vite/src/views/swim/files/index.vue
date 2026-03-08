<template>
  <a-card class="general-card" title="文件上传">
    <a-space direction="vertical" size="large">
      <a-upload
        :action="uploadUrl"
        :headers="uploadHeaders"
        :show-file-list="true"
        @success="onUploadSuccess"
      />
      <a-table :columns="columns" :data="files" row-key="id" />
    </a-space>
  </a-card>
</template>

<script setup lang="ts">
  import { computed, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import { getToken } from '@/utils/auth';
  import type { FileResource } from '@/api/swim';

  const files = ref<FileResource[]>([]);
  const columns = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '文件名', dataIndex: 'fileName' },
    { title: 'URL', dataIndex: 'fileUrl' },
    { title: '大小', dataIndex: 'fileSize' },
  ];

  const uploadUrl = computed(() => {
    return `${import.meta.env.VITE_API_BASE_URL || ''}/api/admin/files/upload`;
  });
  const uploadHeaders = computed(() => ({
    Authorization: `Bearer ${getToken() || ''}`,
  }));

  const onUploadSuccess = (res: any) => {
    const response = res?.response || res;
    if (response?.data) {
      files.value.unshift(response.data);
      Message.success('上传成功');
    }
  };
</script>

<script lang="ts">
  export default {
    name: 'SwimFiles',
  };
</script>
