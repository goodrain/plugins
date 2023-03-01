<template>
  <div class="team-wrap">
    <div class="team-content" v-if="programs.length > 0">
      <div v-for="item in programs" :key="item.id" @click="selectEvent(item)" class="team-box">
        <div class="team-box-title" :title="item.teamName">{{ item.teamName }}</div>
        <div class="team-box-text">{{ item.teamCode }}</div>
      </div>
    </div>
    <Empty v-else description="暂无团队，请联系管理员加入团队" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-23 15:42:40
   * description : 全部团队
   */
  import { computed, onMounted } from 'vue';
  import { Empty } from 'ant-design-vue';
  import { useProjectStoreWithOut } from '/@@/store/modules/projectStore';

  const emits = defineEmits(['selectEvent']);

  const projectStore = useProjectStoreWithOut();

  // 团队列表
  const programs: any = computed(() => projectStore.getList);

  function refreshTeamList() {
    projectStore.getTeamList();
  }

  function selectEvent(item) {
    emits('selectEvent', item);
  }

  onMounted(() => {
    refreshTeamList();
  });
</script>
<style lang="less" scoped>
  .team-wrap {
    width: 100%;
    min-height: 100vh;
    background-color: #e9f1f7;
    display: flex;
    justify-content: center;
    align-items: center;

    .team-content {
      width: 80%;
      display: flex;
      column-gap: 16px;
      row-gap: 16px;
      justify-content: center;

      .team-box {
        width: 180px;
        padding: 16px;
        height: 160px;
        border-radius: 4px;
        background-color: #fff;
        text-align: left;
        transition: all 0.2s;
        cursor: pointer;

        &-title {
          font-size: 24px;
          font-weight: 500;
          width: 100%;
          text-overflow: ellipsis;
          white-space: nowrap;
          overflow: hidden;
        }

        &:hover {
          box-shadow: 0 2px 6px 0 rgb(0 0 0 / 10%);
          -webkit-transform: translateY(-5px);
          transform: translateY(-5px);
        }

        &:hover .team-box-title {
          color: blue;
        }

        &-text {
          font-size: 14px;
          font-weight: 400;
          color: #999;
          margin-top: 10px;
        }
      }
    }
  }
</style>
