<template>
  <div class="p-4">
    <HelpInfo name="部署历史" :imgSrc="IconImg">
      <div class="sub-title"> 各集群下的应用服务部署记录 </div>
      <Divider class="!my-2" />
      <EnvSelect />
      <ServiceSelect class="ml-4" />
    </HelpInfo>
    <BasicTable @register="registerTable" v-bind="$attrs" class="!m-0 !p-0 !mt-4">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label: '部署详情',
              type: 'link',
              size: 'small',
              icon: 'bx:detail',
              onClick: detail.bind(null, record),
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
   * createTime ： 2022-07-22 10:03:10
   * description : 部署历史
   */
  import { onMounted, watch } from 'vue';
  import { columns } from './data';
  import { Divider, message } from 'ant-design-vue';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';

  import { getPagelist } from '/@@/api/deploymentHistoryApi';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';
  import { useEnvStore } from '/@@/store/modules/envStore';
  import EnvSelect from '/@@/views/common/env/index.vue';
  import ServiceSelect from '/@@/views/common/service/index.vue';
  // import { openWindow } from '/@/utils';
  import IconImg from '/@@/assets/images/icon/deployment-history.png';
  import HelpInfo from '/@@/views/common/helpInfo/index.vue';

  const envStore = useEnvStore();
  const appServiceStore = useAppServiceStore();

  const [registerTable, { reload }] = useTable({
    title: '部署历史',
    rowKey: 'id',
    api: getPagelist,
    beforeFetch: ({ current, size, ...query }) => {
      const params: any = {
        current,
        size,
        queryVO: {
          ...query,
          regionCode: envStore.getCurrent?.regionName,
          appServiceId: appServiceStore.getCurrentService.id,
        },
      };
      return params;
    },
    columns,
    actionColumn: {
      width: 130,
      title: '操作',
      align: 'center',
      dataIndex: 'action',
      slots: { customRender: 'action' },
    },
    canResize: false,
    bordered: true,
    useSearchForm: false,
    showIndexColumn: false,
    showTableSetting: true,
    immediate: false,
  });

  function detail(record) {
    const {
      regionCode: region_name,
      teamCode: team_name,
      componentCode: service_alias,
    } = record || {};
    try {
      window?.top?.postMessage(
        {
          flag: 'GOTO_DEPLOY_DETAIL',
          region_name,
          team_name,
          service_alias,
        },
        '*',
      );
    } catch (err: any) {
      message.error(err);
    }
  }

  function refresh() {
    if (envStore.getCurrent?.regionName && appServiceStore.getCurrentService.id) {
      reload();
    }
  }

  onMounted(() => {
    // 环境改变时
    watch(() => envStore.getCurrent, refresh, { immediate: true });
    // 应用服务改变时
    watch(() => appServiceStore.getCurrentService, refresh, { immediate: true });
  });
</script>
<style lang="less" scoped></style>
