<template>
  <div v-bind="$attrs">
    <div class="text-center">
      <Image :src="NodataImg" class="!mx-auto" :preview="false" />
    </div>
    <div class="text-center text-lg">暂无数据</div>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-01 16:29:00
   * description : 暂无数据
   */
  import { Image } from 'ant-design-vue';
  import NodataImg from '/@/assets/svg/no-data.png';
</script>
<style lang="less" scoped></style>
