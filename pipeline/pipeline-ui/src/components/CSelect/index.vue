<template>
  <Popover
    v-model:visible="visible"
    placement="bottom"
    trigger="click"
    overlayClassName="custom-cselect"
    v-bind="$attrs"
  >
    <template #content>
      <div v-if="showSearch" class="px-2 pt-2">
        <Input @change="fetchUser" search :placeholder="searchPlaceholder">
          <template #suffix> <search-outlined /> </template>
        </Input>
      </div>
      <Divider v-if="showSearch" class="!my-2" />
      <slot></slot>
    </template>
    <div @click="show" class="cursor-pointer hover:bg-gray-200 box-border rounded-md pr-1">
      <slot name="button"></slot>
    </div>
  </Popover>
</template>
<script lang="ts" setup>
  /**
   * author : spion@qq.com
   * createTime ： 2022-10-18 15:05:45
   * description : 自定义下拉匡 没有什么功能
   */
  import { ref } from 'vue';
  import { Popover, Input, Divider } from 'ant-design-vue';
  import { debounce } from 'lodash-es';
  import { SearchOutlined } from '@ant-design/icons-vue';

  const emits = defineEmits(['search']);
  defineProps({
    showSearch: { type: Boolean, default: () => false },
    searchPlaceholder: { type: String, default: () => '请输入' },
  });

  const visible = ref<boolean>(false);

  const hide = () => {
    visible.value = false;
  };
  const show = () => {
    visible.value = true;
  };

  const fetchUser = debounce((e: any) => {
    emits('search', e.target.value);
  }, 300);

  defineExpose({
    close: hide,
  });
</script>
<style lang="less">
  .custom-cselect {
    .ant-popover-inner-content {
      padding: 0;
    }
  }
</style>
