<template>
  <div class="p-4">
    <HelpInfo name="流水线" :imgSrc="IconImg">
      <div class="sub-title"
        >流水线是提供自定义流程编排的工具，通过构建，部署，测试，管控等组件化能力，把从开发到交付的各项工作串联起来，从而让企业轻松的实现持续交付
        。</div
      >
    </HelpInfo>
    <Spin :spinning="loading">
      <PipeLineFlowVue @add="add" v-show="!showList" />
      <BasicTable
        v-show="showList"
        @register="registerTable"
        v-bind="$attrs"
        class="!m-0 !p-0 !mt-4"
      >
        <template #tableTitle>
          <a-button type="primary" @click="add">创建流水线 </a-button>
        </template>
        <!-- <template #toolbar>
        <a-button type="primary" @click="openDrawer"> 部署中间件 </a-button>
      </template> -->
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
                label: '复制',
                type: 'link',
                size: 'small',
                icon: 'carbon:copy',
                onClick: copyItem.bind(null, record),
              },
              {
                label: '删除',
                type: 'link',
                size: 'small',
                danger: true,
                icon: 'ic:outline-delete-outline',
                popConfirm: {
                  placement: 'right',
                  title: '是否确认删除，删除后将无法恢复',
                  confirm: delItem.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </BasicTable>
    </Spin>
    <PipelineDrawer @register="registerDrawer" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-09 08:32:26
   * description : 流水线开发
   */
  import { Spin, Modal } from 'ant-design-vue';
  import IconImg from '/@@/assets/images/icon/pipeline.png';
  import PipelineDrawer from './PipelineDrawer.vue';
  import PipeLineFlowVue from './PipeLineFlow.vue';
  import { useDrawer } from '/@/components/Drawer';
  import {
    getPipelinePage,
    copyPipeline,
    delServicePipelineBind,
    delPipeline,
  } from '/@@/api/pipelineApi';
  import { ref, onBeforeMount, watch, onMounted, createVNode } from 'vue';
  import { useProjectStoreWithOut } from '/@@/store/modules/projectStore';
  import { columns, searchFormSchema } from './data';
  import { BasicTable, TableAction, useTable } from '/@/components/Table';
  import { router } from '/@/router';
  import { flow, clearFlow } from './manage/usePipline';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import HelpInfo from '/@@/views/common/helpInfo/index.vue';
  const { createMessage } = useMessage();

  const [registerDrawer, { openDrawer }] = useDrawer();
  const loading = ref(false);
  const projectStore = useProjectStoreWithOut();

  const showList = ref(false);

  const [registerTable, { reload, setLoading }] = useTable({
    rowKey: 'id',
    api: getPipelinePage,
    beforeFetch: ({ current, size, ...query }) => {
      const params: any = {
        current,
        size,
        queryVO: {
          teamId: projectStore.getCurrent?.id,
          ...query,
        },
      };

      return params;
    },
    afterFetch: (data) => {
      loading.value = false;
      if (showList.value === false) {
        showList.value = data && data.length > 0;
      }
      return data;
    },
    columns,
    formConfig: {
      labelWidth: 80,
      schemas: searchFormSchema,
      autoSubmitOnEnter: true,
    },
    // scroll: { x: 2000 },
    actionColumn: {
      width: 220,
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

  function editorItem(record) {
    router.push({ name: 'pipelineManage', query: { listId: record.id } });
  }

  function copyItem(record) {
    setLoading(true);
    copyPipeline(record.id)
      .then((res) => {
        clearFlow();
        flow.name = '复制流水线名称';
        flow.stages = res.stages;
        flow.variables = res.variables;
        router.push({ name: 'pipelineManage' });
      })
      .finally(() => {
        setLoading(false);
      });
  }

  function delItem(record) {
    setLoading(true);

    delServicePipelineBind(record.id)
      .then((res) => {
        if (res === true) {
          setLoading(false);
          Modal.confirm({
            title: '警告',
            icon: createVNode(ExclamationCircleOutlined),
            content: '此流水线已关联应用服务，删除后将影响相关应用服务持续集成',
            okText: '删除',
            cancelText: '放弃',
            onOk() {
              deletePipeline(record);
            },
          });
          // createMessage.error('此流水线已关联应用服务，请在应用服务中取消关联后，再进行删除操作。');
        } else {
          deletePipeline(record);
        }
      })
      .catch(() => {
        setLoading(false);
      });
  }

  function deletePipeline(record) {
    setLoading(true);
    delPipeline(record.id)
      .then(() => {
        createMessage.success('删除成功！');
        reload();
      })
      .finally(() => {
        setLoading(false);
      });
  }

  function add() {
    openDrawer();
  }
  onBeforeMount(() => {
    loading.value = true;
  });

  function refresh() {
    showList.value = true;
    if (projectStore.getCurrent?.id) {
      reload();
    }
  }
  onMounted(() => {
    // 应用服务改变时
    watch(() => projectStore.getCurrent, refresh, { immediate: true });
  });
</script>
<style lang="less" scoped></style>
