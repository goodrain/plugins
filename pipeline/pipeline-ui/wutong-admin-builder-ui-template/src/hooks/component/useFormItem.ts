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

import type { UnwrapRef, Ref, WritableComputedRef, DeepReadonly } from 'vue';
import {
  reactive,
  readonly,
  computed,
  getCurrentInstance,
  watchEffect,
  unref,
  nextTick,
  toRaw,
} from 'vue';

import { isEqual } from 'lodash-es';

export function useRuleFormItem<T extends Recordable, K extends keyof T, V = UnwrapRef<T[K]>>(
  props: T,
  key?: K,
  changeEvent?,
  emitData?: Ref<any[]>,
): [WritableComputedRef<V>, (val: V) => void, DeepReadonly<V>];

export function useRuleFormItem<T extends Recordable>(
  props: T,
  key: keyof T = 'value',
  changeEvent = 'change',
  emitData?: Ref<any[]>,
) {
  const instance = getCurrentInstance();
  const emit = instance?.emit;

  const innerState = reactive({
    value: props[key],
  });

  const defaultState = readonly(innerState);

  const setState = (val: UnwrapRef<T[keyof T]>): void => {
    innerState.value = val as T[keyof T];
  };

  watchEffect(() => {
    innerState.value = props[key];
  });

  const state: any = computed({
    get() {
      return innerState.value;
    },
    set(value) {
      if (isEqual(value, defaultState.value)) return;

      innerState.value = value as T[keyof T];
      nextTick(() => {
        emit?.(changeEvent, value, ...(toRaw(unref(emitData)) || []));
      });
    },
  });

  return [state, setState, defaultState];
}
