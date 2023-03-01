<template>
  <div class="mt-4">
    <Card class="explain">
      <div class="title">
        <div class="name">缓存目录</div>
      </div>
      <div class="content">
        <div class="sub-title"> 开启缓存能够避免每次构建重复下载依赖文件，大幅提升构建速度</div>
      </div>
    </Card>
    <BasicTable @register="registerTable" v-bind="$attrs" class="!m-0 !p-0 !mt-4">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label: '删除',
              type: 'link',
              size: 'small',
              icon: 'ic:outline-delete-outline',
              disabled: true,
            },
          ]"
        />
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-10 11:57:26
   * description : 缓存目录
   */
  import { onMounted } from 'vue';
  import { Card } from 'ant-design-vue';
  import { columns } from './chache.data';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';

  const [registerTable, { setTableData }] = useTable({
    title: '',
    rowKey: 'id',
    columns,
    actionColumn: {
      width: 80,
      title: '操作',
      align: 'center',
      dataIndex: 'action',
      slots: { customRender: 'action' },
    },
    pagination: false,
    canResize: false,
    bordered: true,
    useSearchForm: false,
    showIndexColumn: false,
    showTableSetting: false,
    immediate: false,
  });

  onMounted(() => {
    setTableData([
      {
        id: 1,
        name: '/root/.m2',
        dec: 'maven依赖缓存',
      },
    ]);
  });
</script>
<style lang="less" scoped></style>
