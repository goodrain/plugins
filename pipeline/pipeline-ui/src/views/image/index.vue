<template>
  <div class="p-4">
    <HelpInfo name="镜像仓库" :imgSrc="IconImg">
      <div class="sub-title"
        >镜像仓库展示应用服务构建后生成的镜像制品，该镜像可直接部署到{{ config.name }}Rainbond平台
      </div>
      <Divider class="!my-2" />
      <ServiceSelect />
    </HelpInfo>
    <BasicTable @register="registerTable" v-bind="$attrs" class="!m-0 !p-0 !mt-4">
      <template #action="{ record }">
        <Dropdown>
          <a-button>部署<DownOutlined /></a-button>
          <template #overlay>
            <Menu>
              <MenuItem v-for="item in envList" :key="item?.regionName">
                <div @click="deploy(record, item)">
                  {{ item?.regionAlias }}
                </div>
              </MenuItem>
            </Menu>
          </template>
        </Dropdown>
      </template>
    </BasicTable>
    <DeploymentConfig @register="registerDrawer" :projectItem="projectItem" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-22 10:02:47
   * description : 镜像仓库
   */
  import { onMounted, watch, computed, ref } from 'vue';
  import { columns } from './data';
  import { Dropdown, Menu, Divider } from 'ant-design-vue';
  import { BasicTable, useTable } from '/@/components/Table';
  import { DownOutlined } from '@ant-design/icons-vue';
  import DeploymentConfig from './modules/DeploymentConfig.vue';
  import { useDrawer } from '/@/components/Drawer';

  import { getPagelist } from '/@@/api/imageApi';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';
  import { useEnvStore } from '/@@/store/modules/envStore';
  import ServiceSelect from '/@@/views/common/service/index.vue';
  import IconImg from '/@@/assets/images/icon/image-clone.png';
  import HelpInfo from '/@@/views/common/helpInfo/index.vue';
  import { getCustomEnvConfig } from '/@@/utils/lib/util';

  const config = getCustomEnvConfig();

  const [registerDrawer, { openDrawer }] = useDrawer();

  const MenuItem = Menu.Item;

  const envStore = useEnvStore();
  envStore.setList();
  const appServiceStore = useAppServiceStore();

  const envList = computed(() => envStore.getList);
  const projectItem = ref({}); // 当前的项目信息

  const [registerTable, { reload }] = useTable({
    title: '镜像仓库',
    rowKey: 'id',
    api: getPagelist,
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

  function deploy(record: any, env) {
    projectItem.value = {
      regionCode: env?.regionName,
      appServiceId: record?.appServiceId,
      version: record?.appServiceVersion,
    };
    openDrawer();
  }

  function refresh() {
    if (appServiceStore.getCurrentService.id) {
      reload();
    }
  }

  onMounted(() => {
    // 应用服务改变时
    watch(() => appServiceStore.getCurrentService, refresh, { immediate: true });
  });
</script>
<style lang="less" scoped></style>
