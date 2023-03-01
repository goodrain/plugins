<template>
  <BasicDrawer
    v-bind="$attrs"
    showFooter
    title="部署配置"
    width="26%"
    @ok="handleSubmit"
    @visible-change="visibleChange"
    okText="开始部署"
    :bodyStyle="{ background: '#E9F1F7' }"
  >
    <Spin :spinning="loading">
      <Card v-show="isShowTable" title="可部署应用" class="!mb-4" size="small">
        <BasicTable @register="registerTable" class="!m-0 !p-0 !mt-4" />
      </Card>
      <Card title="说明" size="small">
        <Input.TextArea v-model:value="description" placeholder="部署信息说明" />
      </Card>
    </Spin>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-27 17:37:18
   * description : 部署
   */
  import { ref } from 'vue';
  import { Card, Input, Spin } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicTable, useTable } from '/@/components/Table';
  import { columns } from './deployment.data';
  import { BasicDrawer } from '/@/components/Drawer';
  import { getDeployPagelist, deploy } from '/@@/api/imageApi';
  import { router } from '/@/router';
  import { useProjectStore } from '/@@/store/modules/projectStore';

  const projectStore = useProjectStore();
  const props = defineProps({
    projectItem: { type: Object as any, required: true },
  });

  const { createMessage } = useMessage();

  const loading = ref(false);
  const description = ref(''); // 说明
  const isShowTable = ref(true);

  // 选择列表
  const [registerTable, { reload, getSelectRowKeys }] = useTable({
    //title: '部署配置',
    rowKey: 'appId',
    api: getDeployPagelist,
    beforeFetch: (query) => {
      const params: any = {
        ...query,
        ...props.projectItem,
        teamId: projectStore?.getCurrent?.id,
      };
      return params;
    },
    afterFetch: (list) => {
      isShowTable.value = !(list?.length === 0);
    },
    pagination: {
      pageSize: 99,
      hideOnSinglePage: true,
    },
    columns,
    canResize: false,
    bordered: true,
    useSearchForm: false,
    showIndexColumn: false,
    showTableSetting: false,
    immediate: false,
    rowSelection: {
      type: 'radio',
    },
  });

  function visibleChange(visible) {
    if (visible) {
      reload();
      //customVariables();
    }
  }

  function handleSubmit() {
    const appId = isShowTable ? getSelectRowKeys()?.[0] : undefined;
    const params = {
      ...props.projectItem,
      regionCode: props.projectItem.regionCode,
      description: description.value,
      appId,
    };
    loading.value = true;
    deploy(params)
      .then(() => {
        router.push('/deployment-history');
        createMessage.success('成功!');
      })
      .finally(() => {
        loading.value = false;
      });
  }
</script>
<style lang="less" scoped></style>
