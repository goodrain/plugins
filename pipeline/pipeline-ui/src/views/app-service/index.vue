<template>
  <div class="p-4">
    <HelpInfo name="应用服务" :imgSrc="ServiceImg">
      <div>
        应用服务是满足用户某些需求的程序代码的集合，可以是某个解耦的微服务或是某个单体应用，应用服务是整个系统最小的实体单位，{{
          config.name
        }}
        本插件中所有的开发、集成、部署等都是基于应用服务的。
      </div>
    </HelpInfo>

    <BasicTable @register="registerTable" class="mt-4 !p-0" v-bind="$attrs">
      <template #tableTitle>
        <a-button type="primary" @click="editorItem()"> <PlusOutlined />创建应用服务 </a-button>
      </template>
      <template #status="{ text, record }">
        <Switch :checked="text === StatusEnum.ENABLE" @change="switchChange(record, text)" />
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label: '编辑',
              type: 'link',
              size: 'small',
              icon: 'fa6-regular:pen-to-square',
              onClick: editorItem.bind(null, record),
            },
            {
              label: '添加子服务',
              type: 'link',
              size: 'small',
              ifShow: ConstantsEnum.mavenChildren === record.type,
              icon: 'ant-design:plus-outlined',
              onClick: addChild.bind(null, record),
            },
            {
              label: '仓库配置',
              type: 'link',
              size: 'small',
              icon: 'ant-design:gitlab-outlined',
              onClick: editorHubItem.bind(null, record),
            },
            {
              label: '删除',
              type: 'link',
              size: 'small',
              danger: true,
              icon: 'ant-design:delete-outlined',
              popConfirm: {
                placement: 'right',
                title: '是否确认删除，删除后将无法恢复',
                confirm: deleteItem.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <AddChildrenService @register="registerChildDrawer" @success="success" />
    <EditorDrawer @register="registerEditorDrawer" @success="success" />
    <EditorHubDrawer @register="registerHubDrawer" @success="success" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-22 10:01:28
   * description : 应用服务
   */
  import { watch, onMounted } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { message, Switch } from 'ant-design-vue';
  import { columns, searchFormSchema } from './service.data';
  import { useDrawer } from '/@/components/Drawer';
  import EditorDrawer from './EditorDrawer.vue';
  import EditorHubDrawer from './EditorHubDrawer.vue';
  import AddChildrenService from './AddChildrenService.vue';
  import { pagelist, update, deleteAppService } from '/@@/api/applicationServiceApi';
  import { ConstantsEnum } from '/@@/enums/constantsEnum';
  import { useProjectStoreWithOut } from '/@@/store/modules/projectStore';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import ServiceImg from '/@@/assets/images/service.png';
  import { StatusEnum } from '/@@/enums/StatusEnum';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';
  import HelpInfo from '/@@/views/common/helpInfo/index.vue';
  import { getCustomEnvConfig } from '/@@/utils/lib/util';
  const [registerEditorDrawer, { openDrawer: openEditor }] = useDrawer();
  const [registerChildDrawer, { openDrawer: openChild }] = useDrawer();
  const [registerHubDrawer, { openDrawer: openHubDrawer }] = useDrawer();

  const config = getCustomEnvConfig();

  const appServiceStore = useAppServiceStore();
  const projectStore = useProjectStoreWithOut();

  const [registerTable, { reload, setLoading }] = useTable({
    //title: '应用服务',
    rowKey: 'id',
    api: pagelist,
    beforeFetch: ({ current, size, ...query }) => {
      const params: any = {
        current,
        size,
        queryVO: {
          ...query,
          teamId: projectStore.getCurrent.id,
        },
      };
      return params;
    },
    columns,
    formConfig: {
      labelWidth: 80,
      schemas: searchFormSchema,
      autoSubmitOnEnter: true,
    },
    scroll: { x: 2000 },
    actionColumn: {
      width: 340,
      title: '操作',
      align: 'center',
      dataIndex: 'action',
      fixed: 'right',
      slots: { customRender: 'action' },
    },
    canResize: false,
    bordered: true,
    useSearchForm: true,
    showIndexColumn: false,
    showTableSetting: true,
    immediate: false,
  });

  function addChild(record) {
    openChild(true, {
      record,
      isUpdate: true,
    });
  }

  function editorItem(record: any = {}) {
    openEditor(true, {
      record,
      isUpdate: !!record.id,
    });
  }
  function editorHubItem(record: any = {}) {
    openHubDrawer(true, {
      record,
    });
  }

  async function deleteItem(record: any = {}) {
    const { id } = record;
    try {
      if (id) {
        await deleteAppService({ id });
        reload()
      }
    } catch (e: any) {
      message.error(e);
    }
  }

  // 修改状态
  function switchChange(record, value) {
    record.status = StatusEnum.ENABLE === value ? StatusEnum.DISABLE : StatusEnum.ENABLE;
    setLoading(true);
    update(record).finally(() => {
      setLoading(false);
    });
  }

  function success() {
    reload();
    appServiceStore.setServiceList(true);
  }

  onMounted(() => {
    watch(
      () => projectStore.getCurrent,
      (item) => {
        if (item) {
          reload();
        }
      },
      {
        immediate: true,
      },
    );
  });
</script>
<style lang="less" scoped></style>
