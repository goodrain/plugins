<template>
  <BasicTable v-bind="$attrs" @register="registerTable" />
</template>
<script lang="ts" setup>
  /**
   * author : spion@qq.com
   * createTime ： 2022-10-13 16:12:58
   * description : 选择用户
   */
  import { ref, onMounted, watch } from 'vue';
  import { columns, searchFormSchema } from './account.data';
  import { BasicTable, useTable } from '/@/components/Table';
  import { getUserPageList } from '/@@/api/accountManageApi';

  const emits = defineEmits(['update:value', 'change']);
  const props = defineProps({
    value: String,
  });

  const selectAccount: any = ref([]);

  const selectedRowKeys = ref<any>([]);
  if (props.value) {
    selectedRowKeys.value = props.value.split(',');
  }
  const [registerTable, { getDataSource, clearSelectedRowKeys }] = useTable({
    title: '账户列表',
    rowKey: 'id',
    beforeFetch: ({ current, size, ...query }) => {
      const params: any = {
        current,
        size,
        queryVO: query,
      };
      return params;
    },
    api: getUserPageList,
    columns,
    formConfig: {
      labelWidth: 80,
      schemas: searchFormSchema,
      autoSubmitOnEnter: true,
    },
    canResize: false,
    bordered: true,
    useSearchForm: true,
    showIndexColumn: false,
    showTableSetting: true,
    pagination: false,
    rowSelection: {
      selectedRowKeys: selectedRowKeys,
      onChange: (rows) => {
        selectedRowKeys.value = rows;
        selectAccount.value = getDataSource().filter((item) => {
          return selectedRowKeys.value.includes(item.id);
        });
        emits('update:value', rows);
        emits('change', selectAccount.value);
      },
    },
  });

  onMounted(() => {
    watch(
      () => props.value,
      (val) => {
        selectedRowKeys.value = val ?? [];
      },
      {
        immediate: true,
      },
    );
  });
  defineExpose({
    clearSelectedRowKeys,
  });
</script>
<style lang="less" scoped></style>
