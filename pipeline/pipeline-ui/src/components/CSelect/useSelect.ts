/*
 *
 * Copyright 2023 Talkweb Co., Ltd.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * /
 */

import { cloneDeep } from 'lodash-es';
import { computed, onMounted, ref, watch } from 'vue';
import { isArray, isNumber, isObject, isString } from '/@/utils/is';

export function useSelect(props) {
  const list = ref<any>([]); // 克隆过来的
  const visible = ref<boolean>(false);
  const currentValue = ref<any>(props.mode === 'single' ? null : []);
  const isSelect = ref(false); // 展示选择的数据，说明点击了下拉选择匡
  const activityKeys = ref<string[]>([]); // 当前选择的key
  const valueNotNull = computed(() =>
    props.mode === 'single'
      ? currentValue.value && currentValue.value
      : currentValue.value.length && currentValue.value.length > 0,
  );

  function initValue() {
    currentValue.value = props.mode === 'single' ? null : [];
    activityKeys.value = [];
  }

  // 转换内容
  function getValue(newValue?: any) {
    const value = newValue || props.value || props.modelValue;
    if (isObject(value) && !value.label && !value.value) {
      if (isArray(props.convertValue)) {
        const newValue = {
          ...value,
        };
        if (props.convertValue.length) {
          props.convertValue.forEach((obj) => {
            newValue[obj.newKey] = value[obj.oldKey];
          });
        }
        return newValue;
      } else if (typeof props.convertValue === 'function') {
        return props.convertValue(value);
      }
    } else if (isArray(value)) {
      return value.map(getValue);
    }
    return value;
  }

  // 单选
  function singleSelect(item) {
    isSelect.value = true;
    // fillValue 执行完后，当前选择的应该取消
    setTimeout(() => {
      isSelect.value = false;
    });
    currentValue.value = cloneDeep(item);
    list.value = list.value.map((obj) => {
      if (obj.value === item.value) {
        obj.checked = true;
      } else {
        obj.checked = false;
      }
      return obj;
    });
    activityKeys.value = [currentValue.value.value];
    return {
      item: currentValue.value,
      value: activityKeys.value[0],
    };
  }

  // 多选
  function multipleSelect(item) {
    isSelect.value = true;
    // fillValue 执行完后，当前选择的应该取消
    setTimeout(() => {
      isSelect.value = false;
    });
    const obj = list.value.find(({ value }) => value === item.value);
    if (!obj) {
      return {
        value: [],
        item: [],
      };
    }
    const index = activityKeys.value.findIndex((val) => val === item.value);
    if (index > -1) {
      activityKeys.value.splice(index, 1);
    } else {
      activityKeys.value.push(item.value);
    }
    updateListChecked();
    currentValue.value = list.value
      .filter((obj) => activityKeys.value.includes(obj.value))
      .map((item) => {
        item.checked = true;
        return item;
      });
    return {
      value: activityKeys.value,
      item: currentValue.value,
    };
  }

  // 填充内容
  let fillValueTimer;
  function fillValue() {
    clearTimeout(fillValueTimer);
    if (isSelect.value) {
      let isContinue = false;
      if (
        !props.modelValue ||
        (Array.isArray(props.modelValue)
          ? props.modelValue?.length === 0
          : props.modelValue.trim() === '')
      ) {
        isSelect.value = false;
        initValue();
      } else if (props.modelValue) {
        if (props.mode === 'single') {
          // 内容更新，说明不是当前选择项
          if (props.modelValue !== currentValue.value?.value) {
            isSelect.value = false;
            isContinue = true;
          }
        }
      }
      if (!isContinue) return;
    }
    fillValueTimer = setTimeout(() => {
      if (props.mode === 'single') {
        fillSingle();
      } else {
        fillMultiple();
      }
    }, 100);
  }

  function fillSingle() {
    const currentVal = currentValue.value?.value;
    const modelValue = getValue();
    if (currentVal && currentVal === modelValue) {
      return;
    }
    if (modelValue) {
      if (isObject(modelValue)) {
        if (modelValue.label && modelValue.value) {
          currentValue.value = cloneDeep(modelValue);
          activityKeys.value = [modelValue.value];
          return;
        }
      } else if (isString(modelValue) || isNumber(modelValue)) {
        const obj = list.value.find((item) => item.value === modelValue);
        if (obj) {
          currentValue.value = cloneDeep(obj);
          activityKeys.value = [obj.value];
        } else {
          currentValue.value = modelValue;
          activityKeys.value = [String(modelValue)];
        }
        return;
      }
    }
    initValue();
  }

  function fillMultiple() {
    const modelValue = getValue();
    if (isArray(modelValue)) {
      const firstChild = modelValue[0];
      if (isString(firstChild)) {
        activityKeys.value = modelValue;
        // 回显内容
        if (isArray(props.options)) {
          const newValue = props.options.filter((item) => modelValue.includes(item.value));
          if (newValue?.length) {
            currentValue.value = newValue;
          }
        }
      } else if (isObject(firstChild)) {
        activityKeys.value = modelValue.map(({ value }) => value);
        currentValue.value = cloneDeep(modelValue);
      }
    } else {
      initValue();
    }
  }

  function setOptions() {
    if (!props.options || !props.options.length) return;
    const arr = cloneDeep(props.options);
    //再次展开列表时，回显当前选中的项 把选择的功能加上
    arr.forEach((item) => {
      if (item.checked && !activityKeys.value.includes(item.value)) {
        activityKeys.value.push(item.value);
      }
    });
    // 去除多
    list.value = arr.map((item) => {
      item.checked = activityKeys.value.includes(item.value);
      return item;
    });
  }

  function updateListChecked() {
    list.value = list.value.map((item) => {
      item.checked = activityKeys.value.includes(item.value);
      return item;
    });
  }

  const hide = () => {
    visible.value = false;
  };
  const show = () => {
    if (props.view) return;
    isSelect.value = false;
    visible.value = true;
  };

  function clearAll() {
    hide();
    initValue();
    isSelect.value = true;
    setTimeout(() => {
      isSelect.value = false;
    }, 300);
  }

  onMounted(() => {
    // 更新选择项
    watch(() => activityKeys.value, updateListChecked, { immediate: true });

    // 列表改变时
    watch(() => props.options, setOptions, {
      immediate: true,
    });

    // value初始值
    watch(() => props.value, fillValue, {
      immediate: true,
    });

    // 双向绑定内容改变时
    watch(() => props.modelValue, fillValue, {
      immediate: true,
    });
  });

  return {
    visible,
    currentValue,
    list,
    valueNotNull,
    initValue,
    hide,
    show,
    singleSelect,
    multipleSelect,
    clearAll,
  };
}
