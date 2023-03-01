<template>
  <div
    class="p-2 flex gap-1 items-center"
    :class="showHover ? 'cursor-pointer hover:bg-blue-200' : ''"
    v-bind="$attrs"
  >
    <div class="flex-1 pl-2">{{ item.label }}</div>
    <CheckOutlined v-if="item.checked" class="!text-green-700" />
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : spion@qq.com
   * createTime ： 2022-10-18 17:56:22
   * description :
   */
  import { CheckOutlined } from '@ant-design/icons-vue';
  defineProps({
    showHover: { type: Boolean, default: () => true },
    item: {
      type: Object as PropType<any>,
    },
  });
</script>
<style lang="less" scoped></style>
