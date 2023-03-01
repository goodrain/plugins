<template>
  <!-- <ScrollContainer> -->
  <div ref="wrapperRef" :class="prefixCls">
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
      <!-- <TabPane v-for="item in settingList" :key="item.key">
        <template #tab>
          <div style="padding: 0 15px">{{ item.name }}</div>
        </template>
        <component :is="item.component" />
      </TabPane> -->
    </Tabs>
  </div>
  <!-- </ScrollContainer> -->
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import { Tabs } from 'ant-design-vue';

  /*   import { ScrollContainer } from '/@/components/Container/index'; */
  import { settingList } from './data';

  import BaseSetting from './BaseSetting.vue';
  import SecureSetting from './SecureSetting.vue';
  /*  import AccountBind from './AccountBind.vue';
  import MsgNotify from './MsgNotify.vue'; */

  export default defineComponent({
    components: {
      /* ScrollContainer, */
      Tabs,
      TabPane: Tabs.TabPane,
      BaseSetting,
      SecureSetting,
      /* AccountBind,
      MsgNotify, */
    },
    setup() {
      return {
        prefixCls: 'account-setting',
        settingList,
      };
    },
  });
</script>
<style lang="less">
  .account-setting {
    margin: 12px;
    background-color: @component-background;

    .base-title {
      padding-left: 0;
    }

    .ant-tabs-tab-active {
      background-color: @item-active-bg;
    }
  }
</style>
