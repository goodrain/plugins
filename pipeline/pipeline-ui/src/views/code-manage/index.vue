<template>
  <div class="p-4">
    <HelpInfo name="代码管理" :imgSrc="IconImg">
      <div class="sub-title"
        >代码管理支持GitLab分支的查看，并提供持续集成以及代码质量评估功能。平台的持续集成流水线涵盖：编译构建、代码检查、生成镜像制品、通知Rainbond平台部署，能大幅降低开发人员工作负担，持续提升代码质量与开发效率。</div
      >
      <Divider class="!my-2" />
      <ServiceSelect :showGitUrl="true" />
    </HelpInfo>
    <Tabs @change="tabChange" :activeKey="activeKey" class="code-tab !mt-4">
      <Tabs.TabPane v-for="item in typeOptions" :key="item.value" :tab="item.label" />
      <template #rightExtra>
        <RedoOutlined style="font-size: 18px; cursor: pointer" @click="refresh" />
      </template>
    </Tabs>
    <Branch
      ref="branchRef"
      v-show="activeKey === branch.value"
      @success="() => tabChange(codeci.value)"
    />
    <CI ref="ciRef" v-show="activeKey === codeci.value" :active="activeKey === codeci.value" />
    <!-- <Quality ref="qualityRef" v-show="activeKey === quality.value" /> -->
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-22 10:02:27
   * description : 代码管理
   */
  import { onMounted, watch, ref } from 'vue';
  import { useAppServiceStore } from '/@@/store/modules/appServiceStore';
  import ServiceSelect from '/@@/views/common/service/index.vue';
  import { Tabs, Divider } from 'ant-design-vue';
  import Branch from './branch/index.vue';
  import CI from './ci/index.vue';
  import Quality from './quality/index.vue';
  import IconImg from '/@@/assets/images/icon/code-manage.png';
  import HelpInfo from '/@@/views/common/helpInfo/index.vue';
  import { RedoOutlined } from '@ant-design/icons-vue';

  const branchRef: any = ref(null);
  const ciRef: any = ref(null);
  const qualityRef: any = ref(null);
  const branch = {
    label: '分支管理',
    value: 'BRANCH',
  };
  const codeci = {
    label: '持续集成',
    value: 'CI',
  };
  // const quality = {
  //   label: '代码质量',
  //   value: 'QUALITY',
  // };

  const activeKey = ref(branch.value);
  const appServiceStore = useAppServiceStore();

  const typeOptions: any = [branch, codeci];

  function refresh() {
    if (appServiceStore.getCurrentService.id) {
      switch (activeKey.value) {
        case branch.value:
          branchRef.value.refresh();
          break;
        case codeci.value:
          ciRef.value.refresh();
          break;
        // case quality.value:
        //   qualityRef.value.refresh();
        //   break;
      }
    }
  }

  function tabChange(val: string) {
    activeKey.value = val;
    refresh();
  }

  onMounted(() => {
    // 应用服务改变时
    watch(() => appServiceStore.getCurrentService, refresh, { immediate: true });
  });
</script>
<style lang="less"></style>
