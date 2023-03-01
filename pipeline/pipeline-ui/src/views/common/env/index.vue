<template>
  <div class="inline-block">
    <a-button style="border-right: none; color: #999"> 环境 </a-button>
    <Dropdown>
      <template #overlay>
        <Menu @click="handleMenuClick">
          <MenuItem v-for="item in list" :key="item?.regionName">
            <span :class="item.id === current.id ? 'text-blue-600' : ''">{{
              item.regionAlias
            }}</span>
          </MenuItem>
        </Menu>
      </template>
      <a-button>
        {{ current?.regionAlias }}
        <DownOutlined />
      </a-button>
    </Dropdown>
  </div>
</template>
<script type="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-07-23 08:35:24
   * description : 开发环境下拉列表
   */
  import {computed} from "vue"
  import { Dropdown, Menu } from 'ant-design-vue';
  import { useEnvStore } from "/@@/store/modules/envStore";
  import { DownOutlined } from '@ant-design/icons-vue';
  const {Item : MenuItem} = Menu;

  const envStore = useEnvStore();
  envStore.setList();

  const list = computed(()=> envStore.getList);
  const current = computed(()=> envStore.getCurrent)

  function handleMenuClick(target){
    const item = envStore.getList.find((item) => item?.regionName === target.key);
    envStore.setCurrent(item);
  }
</script>
<style lang="less" scoped></style>
