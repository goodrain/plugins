<template>
  <BasicModal title="人个中心" width="60%" :showOkBtn="false" cancelText="关闭" v-bind="$attrs">
    <Setting />
  </BasicModal>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-30 10:21:11
   * description : 弹出匡打开
   */
  import { BasicModal } from '/@/components/Modal';
  import Setting from './index.vue';
</script>
<style lang="less" scoped></style>
