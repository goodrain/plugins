<template>
  <div class="flex-1 flex flex-row gap-x-1 truncate">
    <template v-if="mode === 'single'">
      <div v-if="currentValue" class="text-gray-600 truncate" :title="currentValue.label">{{
        currentValue.label
      }}</div>
      <div v-else class="text-gray-400">{{ showText }}</div>
    </template>
    <template v-else>
      <template v-if="valueNotNull">
        <div v-for="item in currentValue" :key="item.value" class="text-gray-600">
          {{ item.label }}
        </div>
      </template>
      <div v-else class="text-gray-400">{{ showText }}</div>
    </template>
  </div>
</template>
<script lang="ts" setup>
  /**
   * author : spion@qq.com
   * createTime ： 2022-10-21 15:11:56
   * description : 展示内容
   */
  import { computed } from 'vue';
  type ModeType = 'multiple' | 'single';

  const props = defineProps({
    currentValue: { type: Object as any },
    mode: { type: String as PropType<ModeType>, default: () => 'single' }, // 多选或单选
    showText: { type: String, default: () => '未指定' }, // 默认展示文字
    // isShowEllipisis: { type: String },
  });

  const valueNotNull = computed(() =>
    props.mode === 'single'
      ? props.currentValue && props.currentValue.value
      : props.currentValue.length && props.currentValue.length > 0,
  );
</script>
<style lang="less" scoped></style>
