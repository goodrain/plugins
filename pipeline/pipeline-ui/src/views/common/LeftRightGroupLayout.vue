<template>
  <div class="h-full">
    <Row :gutter="16" class="p-4">
      <Col v-bind="leftSpan" class="shadow-sm">
        <slot name="left"></slot>
      </Col>
      <Col v-bind="rightSpan">
        <slot></slot>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : bo.peng
   * createTime ： 2022-05-05 16:33:04
   * description : 左右结构
   */
  import { Row, Col } from 'ant-design-vue';
  const leftSpan = {
    md: 10,
    lg: 8,
    xl: 6,
    xxl: 4,
  };
  const rightSpan = {
    md: 24 - leftSpan.md,
    lg: 24 - leftSpan.lg,
    xl: 24 - leftSpan.xl,
    xxl: 24 - leftSpan.xxl,
  };
</script>
