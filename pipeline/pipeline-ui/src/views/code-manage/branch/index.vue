<template>
  <div>
    <BasicTable @register="registerTable" v-bind="$attrs" class="!m-0 !p-0">
      <template #action="{ record }">
        <!-- <TableAction
          :actions="[
            {
              label: '构建',
              type: 'link',
              size: 'small',
              icon: 'carbon:document-view',
              onClick: build.bind(record),
            },
          ]"
        /> -->
        <a-button v-if="subService.length < 1" @click="build(record, '')">构建</a-button>
        <Dropdown v-else>
          <a-button>构建<DownOutlined /></a-button>
          <template #overlay>
            <Menu>
              <MenuItem v-for="(value, index) in subService" :key="index">
                <div @click="build(record, value)">
                  {{ value }}
                </div>
              </MenuItem>
            </Menu>
          </template>
        </Dropdown>
      </template>
    </BasicTable>
    <DcDrawer @register="dcRegister" @success="success" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-22 10:02:27
   * description : 分支管理
   */
  import { defineEmits, ref } from 'vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { Dropdown, Menu } from 'ant-design-vue';
  import { onMounted, watch } from 'vue';
  import { columns } from './data';
  import DcDrawer from './dc/dcDrawer.vue';
  import { getPagelist } from '/@@/api/codeManageApi';
  import { useDrawer } from '/@/components/Drawer';
  import { BasicTable, useTable } from '/@/components/Table';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';
  import { getSubService } from '/@@/api/appServiceApi';

  const MenuItem = Menu.Item;
  const emits = defineEmits(['success']);
  const appServiceStore = useAppServiceStore();

  const [dcRegister, { openDrawer: openDcDrawer }] = useDrawer();

  const subService = ref([]);

  const [registerTable, { reload }] = useTable({
    //title: '分支列表',
    rowKey: 'id',
    api: getPagelist,
    beforeFetch: ({ current, size, ...query }) => {
      const params: any = {
        current,
        size,
        queryVO: {
          ...query,
          // regionCode: envStore.getCurrent.regionName,
          appServiceId: appServiceStore.getCurrentService.id,
        },
      };
      return params;
    },
    columns,
    actionColumn: {
      width: 80,
      title: '操作',
      align: 'center',
      dataIndex: 'action',
      slots: { customRender: 'action' },
    },
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
      setSubService();
    }
  }

  //获取微服务列表
  function setSubService() {
    getSubService(appServiceStore.getCurrentService.id).then((res) => {
      subService.value = res;
    });
  }

  function build(record: Recordable, module: any) {
    record.module = module;
    openDcDrawer(true, record);
  }

  function success() {
    emits('success');
  }

  onMounted(() => {
    // 应用服务改变时
    watch(() => appServiceStore.getCurrentService, refresh, { immediate: true });
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

  .demo-dropdown-wrap :deep(.ant-dropdown-button) {
    margin-right: 8px;
    margin-bottom: 8px;
  }
</style>
