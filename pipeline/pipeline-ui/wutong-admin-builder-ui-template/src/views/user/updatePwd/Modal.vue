<template>
  <BasicModal title="修改密码" width="60%" :showOkBtn="false" cancelText="关闭" v-bind="$attrs">
    <Passwrod />
  </BasicModal>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-30 10:21:11
   * description : 弹出匡打开
   */
  import { BasicModal } from '/@/components/Modal';
  import Passwrod from './index.vue';
</script>
<style lang="less" scoped></style>
