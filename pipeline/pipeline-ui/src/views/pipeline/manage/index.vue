<template>
  <Layout :class="`${prefixCls}`">
    <!-- <div>
      <LayoutHeader class="pipline-head" v-if="getShowInsetHeaderRef" />
    </div> -->
    <Tabs :centered="true" :tabBarStyle="{ backgroundColor: '#fff', borderTop: '1px solid #ddd' }">
      <template #leftExtra>
        <div class="flex">
          <div class="ml-5 flex items-center cursor-pointer" @click="back">
            <LeftOutlined />
            <div class="pl-2"> 返回 </div>
          </div>
          <div class="pl-4 flex items-center">
            <template v-if="editorName">
              <Input placeholder="请输入" :maxlength="20" v-model:value="name" />
              <a-button @click="saveName">保存</a-button>
            </template>
            <template v-else>
              {{ flow.name }}
              <FormOutlined class="pl-2" @click="() => (editorName = true)" />
            </template>
          </div>
        </div>
      </template>
      <template #rightExtra>
        <div class="pr-5">
          <a-button type="primary" @click="submit" class="!rounded-3xl">保存</a-button>
        </div>
      </template>
      <TabPane key="1" tab="流程配置">
        <Spin :spinning="loading">
          <ProcessConfig />
        </Spin>
      </TabPane>
      <TabPane key="2" tab="变量与缓存">
        <Spin :spinning="loading">
          <VariableCache />
        </Spin>
      </TabPane>
    </Tabs>
  </Layout>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-09 16:54:51
   * description : 流水线管理
   */
  import { Layout, Spin, Tabs, Input } from 'ant-design-vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import LayoutHeader from '/@/layouts/default/header/index.vue';
  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { router } from '/@/router';
  import { useMessage } from '/@/hooks/web/useMessage';
  import ProcessConfig from './processConfig/index.vue';
  import VariableCache from './variableCache/index.vue';
  import { LeftOutlined, FormOutlined } from '@ant-design/icons-vue';
  import { useRoute } from 'vue-router';
  import { ref, watch } from 'vue';
  import { loading, setLoading, getPipelineInfo } from './usePipline';
  import { addAndUpdatePipeline } from '/@@/api/pipelineApi';
  import { useProjectStore } from '/@@/store/modules/projectStore';
  import { flow } from './usePipline';

  const { createMessage } = useMessage();
  const projectStore = useProjectStore();

  const editorName = ref(false);
  const name = ref(flow.name);

  const { TabPane } = Tabs;

  const { getShowInsetHeaderRef } = useHeaderSetting();
  const { prefixCls } = useDesign('pipeline-manage-layout');

  const route = useRoute();

  getPipelineInfo(route?.query);

  function back() {
    router.push('/pipeline');
  }

  function saveName() {
    flow.name = name.value;
    editorName.value = false;
  }

  function submit() {
    if (!flow.name || flow.name.trim() === '') {
      return createMessage.error('请输入流水线名称！');
    }
    setLoading(true);
    addAndUpdatePipeline({
      teamId: projectStore.getCurrent.id,
      tempId: route?.query.id,
      ...flow,
    })
      .then(() => {
        createMessage.success(flow.id ? '修改成功！' : '新增成功！');
        flow.name = '无标题';
        back();
      })
      .finally(() => {
        setLoading(false);
      });
  }
  watch(
    () => flow.name,
    () => {
      name.value = flow.name;
    },
  );
</script>
<style lang="less" scoped></style>
<style lang="less">
  @layout-prefix-cls: ~'@{namespace}-pipeline-manage-layout';
  @header-prefix-cls: ~'@{namespace}-layout-header';

  .@{layout-prefix-cls}{
    min-height: 100%;
    background-color: #E9F1F7;
  }

  .pipline-head {
    .@{header-prefix-cls}-trigger {
      display: none;
    }
  }
</style>
