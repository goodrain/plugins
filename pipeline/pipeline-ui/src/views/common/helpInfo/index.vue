<template>
  <Card class="explain">
    <div class="title">
      <Image :src="imgSrc" :preview="false" /> <div class="name">{{ name }}</div>
    </div>
    <div class="content">
      <slot>{{ content }}</slot>
    </div>
  </Card>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-09-16 17:30:49
   * description : 头部帮助说明信息
   */
  import { Card, Image } from 'ant-design-vue';
  defineProps({
    imgSrc: { type: Object as any },
    name: { type: String },
    content: { type: String },
  });
</script>
<style lang="less">
  // 说明页，用的比较多就写在这里了
  .explain {
    padding: 0;
    box-shadow: 0px 1px 4px 0px rgba(0, 0, 0, 0.06);
    border-radius: 4px !important;

    .title {
      display: flex;
      font-size: 24px;
      font-weight: 400;
      line-height: 40px;
      color: #000000;

      .name {
        padding-left: 5px;
        white-space: nowrap;
      }
    }

    .content {
      padding-top: 10px;
    }

    .ant-card-body {
      padding: 15px;
    }
  }
</style>
