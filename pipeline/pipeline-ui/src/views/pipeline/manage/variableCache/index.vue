<template>
  <div class="p-4">
    <HelpInfo name="流程环境变量" :imgSrc="IconImg">
      <div class="sub-title">
        通过定义环境变量实现流水线过程定制化，可以在执行过程的任何阶段使用这些变量</div
      >
    </HelpInfo>
    <Variable />
    <!-- <CacheDir /> -->
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-08-10 11:34:18
   * description : 变量与缓存
   */
  import IconImg from '/@@/assets/images/icon/middleware.png';
  import Variable from './Variable.vue';
  import CacheDir from './CacheDir.vue';
  import HelpInfo from '/@@/views/common/helpInfo/index.vue';
</script>
<style lang="less" scoped></style>
