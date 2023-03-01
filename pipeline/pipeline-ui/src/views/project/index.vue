<template>
  <div>
    <ContentLayout>
      <div class="project">
        <Spin :spinning="loading">
          <Team @select-event="selectEvent" />
        </Spin>
      </div>
    </ContentLayout>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-22 10:06:20
   * description : project
   */
  import ContentLayout from '/@@/views/common/ContentLayout.vue';
  import { computed } from 'vue';
  import { Spin } from 'ant-design-vue';
  import { router } from '/@/router';
  import { onBeforeRouteLeave } from 'vue-router';

  import Team from './module/team/index.vue';

  import { useProjectStoreWithOut } from '/@@/store/modules/projectStore';

  const projectStore = useProjectStoreWithOut();
  const loading = computed(() => projectStore.getLoading);

  function selectEvent(item) {
    projectStore.setLoading(true);
    projectStore.addRecentlye(item);

    setTimeout(() => {
      router.push(item?.url ? item?.url : '/app-service');
      // router.push({
      //   path: item?.url ? item?.url : '/home',
      // });
    }, 700);
  }
  onBeforeRouteLeave(() => {
    projectStore.setLoading(false);
  });
</script>
<style lang="less">
  .project {
    background-color: #f9fbff;
    //padding-top: 16px;

    &-header {
      display: flex;
      column-gap: 30px;
      justify-content: space-between;
      height: 610px;
      margin-bottom: 30px;

      .work-item {
        flex: 1;
        width: 0;
      }

      .recent {
        width: 414px;
      }
    }
  }

  .home {
    padding-bottom: 40px;
  }
</style>
