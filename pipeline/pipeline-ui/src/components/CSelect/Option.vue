<template>
  <slot name="view" v-if="view" :currentValue="currentValue">
    <ShowValue
      v-bind="$attrs"
      :currentValue="currentValue"
      :mode="mode"
      :view="view"
      :show-text="showText"
      isShowEllipisis="text"
    />
  </slot>
  <Popover
    v-else
    v-model:visible="visible"
    placement="bottom"
    trigger="click"
    overlayClassName="custom-cselect"
  >
    <template #content>
      <!-- 搜索 -->
      <slot name="search" v-if="showSearch">
        <div class="px-2 pt-2">
          <Input
            @change="fetchUser"
            @pressEnter="fetchUserNoDebounce"
            search
            :placeholder="searchPlaceholder"
          >
            <template #suffix> <SearchOutlined /> </template>
          </Input>
          <Divider class="!my-2" />
        </div>
      </slot>
      <!-- 清除内容 -->
      <div v-if="allowClear" :class="showSearch ? '' : 'pt-2'">
        <div v-if="mode === 'single' && currentValue && currentValue.value">
          <div @click="reset" class="text-center text-blue-600 cursor-pointer"> 清除已选 </div>
          <Divider class="!my-2" />
        </div>
        <div v-if="mode === 'multiple'" class="px-3 text-gray-500">
          <div class="flex">
            <div class="flex-1"> 已选 {{ currentValue?.length ?? 0 }} 项 </div>
            <div
              v-if="currentValue?.length"
              @click="reset"
              class="text-center text-blue-600 cursor-pointer pl-1"
              >清除已选</div
            >
          </div>
          <Divider class="!my-2" />
        </div>
      </div>
      <!-- 列表循环 -->
      <div
        class="max-h-64 overflow-y-auto"
        v-bind="optionAttrs"
        v-if="loading || options.length > 0"
      >
        <Spin :spinning="loading" class="mx-auto" style="display: block">
          <div v-if="loading" class="w-20 h-10"></div>
          <template v-for="option in list" :key="option.value">
            <slot name="option" v-bind="option" :selectEvent="selectItem">
              <Item :item="option" @click="selectItem(option)" />
            </slot>
          </template>
        </Spin>
      </div>
      <!-- 暂无数据 -->
      <NoData v-if="!loading && options.length < 1" class="w-32 flex-1 mx-auto pb-3" />
      <slot name="optionFooter"></slot>
    </template>
    <!-- 展示页 -->
    <slot name="allfooter">
      <div
        class="group overflow-hidden flex flex-row view-item flex items-center box-border py-1 px-2"
        :class="[
          view ? '' : 'hover:bg-gray-200 cursor-pointer',
          border ? 'border border-gray-200' : '',
        ]"
        @click.stop="clickEvent"
        v-bind="$attrs"
      >
        <CloseCircleFilled
          v-if="remove"
          @click.stop="removeEvent"
          class="absolute !hidden !group-hover:block -mt-8 -ml-3 !text-gray-500/40 !hover:text-gray-500 text-base"
        />
        <span v-if="label" class="text-gray-500">{{ label }}</span>
        <slot :name="valueNotNull ? 'view' : 'footer'" :currentValue="currentValue">
          <ShowValue
            @click="show"
            v-bind="$attrs"
            :currentValue="currentValue"
            :mode="mode"
            :view="view"
            :show-text="showText"
            isShowEllipisis="module"
          />
        </slot>
        <div v-if="!view" class="group-view w-5 text-right">
          <CaretDownOutlined :class="['text-sm !text-gray-400', selectClear ? 'arrow' : 'down']" />
          <CloseCircleOutlined
            @click.stop="reset"
            class="text-sm !text-gray-400 clear"
            v-if="selectClear"
          />
        </div>
      </div>
    </slot>
  </Popover>
</template>
<script lang="ts" setup>
  /**
   * author : spion@qq.com
   * createTime ： 2022-10-18 15:05:45
   * description : 自定义下拉匡 带内容匡 功能比较多
   */
  import {
    CaretDownOutlined,
    CloseCircleOutlined,
    SearchOutlined,
    CloseCircleFilled,
  } from '@ant-design/icons-vue';

  import { Divider, Input, Popover, Spin } from 'ant-design-vue';
  import { OptionProps } from 'ant-design-vue/lib/select';
  import { debounce } from 'lodash-es';
  import Item from './Item.vue';
  import ShowValue from './ShowValue.vue';
  import { useSelect } from './useSelect';
  import { NoData } from '/@@/components/NoData/index';
  const emits = defineEmits(['update:modelValue', 'search', 'change', 'triggerClick', 'remove']);
  type ModeType = 'multiple' | 'single';
  type ValueType = String | null | any;
  type ConverType = { oldKey: string; newKey: string }[] | Function;
  const props = defineProps({
    modelValue: [String, Array] as PropType<ValueType>, // 双向绑定值
    label: String,
    value: { type: [String, Object] as PropType<ValueType> }, // 主要用于回显，可传对象
    convertValue: { type: [Object, Function] as PropType<ConverType> }, // 当要回显时需要的字段转换
    mode: { type: String as PropType<ModeType>, default: () => 'single' }, // 多选或单选
    allowClear: { type: Boolean, default: () => false }, // 支持清除
    selectClear: { type: Boolean, default: () => false }, // 支持选择匡右侧清除
    view: { type: Boolean, default: () => false }, // 只展示
    showSearch: { type: Boolean, default: () => false },
    searchPlaceholder: { type: String, default: () => '请输入' },
    showText: { type: String, default: () => '未指定' }, // 默认展示文字
    options: { type: Array as PropType<OptionProps>, default: () => [] },
    border: { type: Boolean, default: () => false }, // 边匡
    loading: { type: Boolean, default: () => false }, //
    remove: { type: Boolean, default: () => false }, // 删除该组件
    // attrs
    optionAttrs: { type: Object, default: () => {} },
  });

  const {
    list,
    visible,
    currentValue,
    valueNotNull,
    hide,
    show,
    clearAll,
    singleSelect,
    multipleSelect,
  } = useSelect(props);

  function selectItem(item) {
    if (props.mode === 'single') {
      const result = singleSelect(item);
      emits('update:modelValue', result.value);
      emits('change', result.item, result, item);
      hide();
      return result.value;
    } else {
      const mulitple = multipleSelect(item);
      emits('update:modelValue', mulitple.value);
      emits('change', mulitple.item, mulitple.value, item);
      return mulitple.value;
    }
  }

  //重置，清空内容
  function reset() {
    clearAll();
    emits('update:modelValue', currentValue.value);
    emits('change', currentValue.value);
  }
  function removeEvent() {
    emits('remove');
  }

  const fetchUser = debounce((e: any) => {
    emits('search', e.target.value);
  }, 100);

  const fetchUserNoDebounce = (e: any) => emits('search', e.target.value);

  // 点击事件
  function clickEvent() {
    emits('triggerClick');
  }

  defineExpose({
    close: hide,
    open: show,
  });
</script>
<style lang="less">
  .custom-cselect {
    .ant-popover-inner-content {
      padding: 0;
    }
  }
</style>
<style lang="less">
  .view-item {
    .clear,
    .arrow,
    .down {
      display: none;
    }

    &:hover {
      .arrow,
      .down {
        display: inline-block;
      }
    }

    .group-view {
      &:hover {
        .arrow {
          display: none;
        }

        .clear {
          display: inline-block;
        }
      }
    }
  }
</style>
