<template>
  <Tabs tab-position="left">
    <TabPane key="1">
      <template #tab>
        <div style="padding: 0 15px">个人设置</div>
      </template>
      <BaseSetting />
    </TabPane>
    <TabPane key="2">
      <template #tab>
        <div style="padding: 0 15px">安全设置</div>
      </template>
      <SecureSetting />
    </TabPane>
  </Tabs>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-09-09 17:45:34
   * description : 个人中心
   */
  import { Tabs } from 'ant-design-vue';
  import BaseSetting from './BaseSetting.vue';
  import SecureSetting from './SecureSetting.vue';

  const { TabPane } = Tabs;
</script>
<style lang="less" scoped></style>
