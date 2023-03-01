<template>
  <Layout :class="prefixCls" v-bind="lockEvents">
    <LayoutFeatures />
    <LayoutHeader fixed v-if="getShowFullHeaderRef" />
    <Layout :class="[layoutClass]">
      <LayoutSideBar v-if="getShowSidebar || getIsMobile" />
      <Layout :class="`${prefixCls}-main`">
        <!-- <LayoutMultipleHeader /> -->
        <LayoutContent />
        <LayoutFooter />
      </Layout>
    </Layout>
  </Layout>
</template>

<script lang="ts">
  import { defineComponent, computed, unref } from 'vue';
  import { Layout } from 'ant-design-vue';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';

  import LayoutHeader from './header/index.vue';
  import LayoutContent from './content/index.vue';
  import LayoutSideBar from './sider/index.vue';
  import LayoutMultipleHeader from './header/MultipleHeader.vue';

  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useLockPage } from '/@/hooks/web/useLockPage';

  import { useAppInject } from '/@/hooks/web/useAppInject';

  // [修改]-新增-开始
  let NewLayoutHeader = LayoutHeader;
  let NewLayoutContent = LayoutContent;
  let NewLayoutSideBar = LayoutSideBar;
  let NewLayoutMultipleHeader = LayoutMultipleHeader;
  try {
    const headKey = '../../../../src/layouts/default/header/index.vue';
    const contentKey = '../../../../src/layouts/default/content/index.vue';
    const sideKey = '../../../../src/layouts/default/sider/index.vue';
    const multipKey = '../../../../src/layouts/default/header/MultipleHeader.vue';
    const rootHeader = import.meta.globEager('../../../../src/layouts/default/header/index.vue');
    const rootContent = import.meta.globEager('../../../../src/layouts/default/content/index.vue');
    const rootSideBar = import.meta.globEager('../../../../src/layouts/default/sider/index.vue');
    const rootMultipleHeader = import.meta.globEager(
      '../../../../src/layouts/default/header/MultipleHeader.vue',
    );
    if (rootHeader[headKey]) {
      NewLayoutHeader = rootHeader[headKey].default;
    }
    if (rootContent[contentKey]) {
      NewLayoutContent = rootContent[contentKey].default;
    }
    if (rootSideBar[sideKey]) {
      NewLayoutSideBar = rootSideBar[sideKey].default;
    }
    if (rootMultipleHeader[multipKey]) {
      NewLayoutMultipleHeader = rootMultipleHeader[multipKey].default;
    }
  } catch {}

  // [修改]-新增-结束

  export default defineComponent({
    name: 'DefaultLayout',
    components: {
      LayoutFeatures: createAsyncComponent(() => import('/@/layouts/default/feature/index.vue')),
      LayoutFooter: createAsyncComponent(() => import('/@/layouts/default/footer/index.vue')),
      LayoutHeader: NewLayoutHeader, // [修改]
      LayoutContent: NewLayoutContent, // [修改]
      LayoutSideBar: NewLayoutSideBar, // [修改]
      LayoutMultipleHeader: NewLayoutMultipleHeader, // [修改]
      Layout,
    },
    setup() {
      const { prefixCls } = useDesign('default-layout');
      const { getIsMobile } = useAppInject();
      const { getShowFullHeaderRef } = useHeaderSetting();
      const { getShowSidebar, getIsMixSidebar, getShowMenu } = useMenuSetting();

      // Create a lock screen monitor
      const lockEvents = useLockPage();

      const layoutClass = computed(() => {
        let cls: string[] = ['ant-layout'];
        if (unref(getIsMixSidebar) || unref(getShowMenu)) {
          cls.push('ant-layout-has-sider');
        }
        return cls;
      });

      return {
        getShowFullHeaderRef,
        getShowSidebar,
        prefixCls,
        getIsMobile,
        getIsMixSidebar,
        layoutClass,
        lockEvents,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-default-layout';

  .@{prefix-cls} {
    display: flex;
    width: 100%;
    min-height: 100%;
    background-color: @content-bg;
    flex-direction: column;

    > .ant-layout {
      min-height: 100%;
    }

    &-main {
      width: 100%;
      margin-left: 1px;
    }
  }
</style>
