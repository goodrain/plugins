<template>
  <div class="flex flex-row items-center justify-center menu-wrap">
    <Dropdown v-if="current && current.id">
      <a-button>
        <div class="flex items-center team-select-wrap">
          <span class="text-gray-600 team-select-text" @click.prevent :title="current?.teamName">
            {{ current.teamName }}
          </span>
          <DownOutlined />
        </div>
      </a-button>
      <template #overlay>
        <Menu>
          <MenuItem v-for="item in list" :key="item.id" style="padding: 0">
            <a
              href="javascript:;"
              :class="{ menuLi: true, active: current.id === item.teamCode }"
              @click="selectProject(item)"
              >{{ item.teamName }}
            </a>
          </MenuItem>
          <Divider />
          <MenuItem>
            <a href="javascript:void(0)" @click="goRouter"><AppstoreOutlined /> 查看全部团队 </a>
          </MenuItem>
        </Menu>
      </template>
    </Dropdown>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-22 17:29:48
   * description : 选择项目列表
   */
  import { computed } from 'vue';
  import { Dropdown, Menu } from 'ant-design-vue';
  import { DownOutlined, AppstoreOutlined } from '@ant-design/icons-vue';
  import { useProjectStoreWithOut } from '/@@/store/modules/projectStore';
  import { PageEnum } from '/@/enums/pageEnum';
  import { router } from '/@/router/index';

  const { Item: MenuItem, Divider } = Menu;
  const projectStore = useProjectStoreWithOut();
  projectStore.getTeamList();

  const current: any = computed(() => projectStore.getCurrent);

  // 最近访问的项目
  const list: any = computed(() => projectStore.getList);

  function selectProject(item) {
    projectStore.addRecentlye(item);
  }

  function goHome() {
    router.push(PageEnum.BASE_HOME);
  }

  function goRouter() {
    router.push(PageEnum.BASE_HOME);
  }
</script>
<style lang="less" scoped>
  .team-select-wrap {
    width: 140px;
    padding: 0 8px;
    justify-content: space-between;
  }

  .team-select-text {
    width: 0px;
    flex: 1;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: block;
    text-align: left;
  }

  .menu-wrap {
    margin: 16px 0;
  }

  .menuLi {
    display: block;
    padding: 5px 12px;
  }

  .active {
    display: block;
    background-color: #08153a !important;
    border-radius: 2px;
    color: #fff !important;
  }
</style>
