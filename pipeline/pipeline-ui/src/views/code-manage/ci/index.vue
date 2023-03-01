<template>
  <div>
    <BasicTable @register="registerTable" v-bind="$attrs" class="!m-0 !p-0" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-22 10:02:27
   * description : 持续集成
   */
  import { onMounted, watch, onUnmounted } from 'vue';
  import { columns } from './data';
  import { getPageIntergrationlist } from '/@@/api/codeManageApi';
  import { BasicTable, useTable } from '/@/components/Table';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';

  const props = defineProps({
    active: { type: Boolean, required: true },
  });

  const appServiceStore = useAppServiceStore();
  let getListTimer: any = 0;

  const [registerTable, { reload }] = useTable({
    rowKey: 'id',
    api: getPageIntergrationlist,
    beforeFetch: ({ current, size, ...query }) => {
      const params: any = {
        current,
        size,
        queryVO: {
          ...query,
          appServiceId: appServiceStore.getCurrentService.id,
        },
      };
      return params;
    },
    columns,
    // actionColumn: {
    //   width: 130,
    //   title: '操作',
    //   align: 'left',
    //   dataIndex: 'action',
    //   slots: { customRender: 'action' },
    // },
    canResize: false,
    bordered: true,
    useSearchForm: false,
    showIndexColumn: false,
    showTableSetting: false,
    immediate: false,
  });

  function refresh() {
    if (appServiceStore.getCurrentService.id) {
      reload();
    }
  }

  onMounted(() => {
    // 应用服务改变时
    watch(() => appServiceStore.getCurrentService, refresh, { immediate: true });
  });

  watch(
    () => props.active,
    (active) => {
      if (active) {
        getListTimer = setInterval(refresh, 5000);
      } else {
        clearInterval(getListTimer);
      }
    },
  );
  onUnmounted(() => {
    clearInterval(getListTimer);
  });

  defineExpose({
    refresh,
  });
</script>
<style lang="less" scoped>
  .master {
    width: 30px;
    height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #eef2ff;
    color: #315aea;
    border-radius: 100%;
    font-size: 14px;
  }
</style>
