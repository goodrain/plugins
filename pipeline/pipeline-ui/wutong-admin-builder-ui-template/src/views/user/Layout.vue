<template>
  <div class="h-full flex flex-col bg-gray-100">
    <a-layout-header
      :style="{
        background: '#fff',
        paddingLeft: '0px',
        paddingRight: '0px',
        borderBottom: '1px solid #eee',
      }"
    >
      <div class="flex h-full full-width">
        <app-logo />
        <div class="flex-1 h-full flex justify-center items-center text-lg font-bold"> </div>
        <div class="mt-2 platform-header-r">
          <UserDropDown />
        </div>
      </div>
    </a-layout-header>
    <div class="md:container m-auto p-5 flex-1 flex flex-col align-center justify-center">
      <RouterView />
    </div>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-30 10:17:45
   * description :用户中心
   */
  import { AppLogo } from '/@/components/Application';
  import { UserDropDown } from '/@/layouts/default/header/components';
</script>
<style lang="less" scoped></style>
