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

import type { Ref } from 'vue';

import { ref, onMounted, watch, onUnmounted } from 'vue';
import { isWindow, isObject } from '/@/utils/is';
import { useThrottleFn } from '@vueuse/core';

export function useScroll(
  refEl: Ref<Element | Window | null>,
  options?: {
    wait?: number;
    leading?: boolean;
    trailing?: boolean;
  },
) {
  const refX = ref(0);
  const refY = ref(0);
  let handler = () => {
    if (isWindow(refEl.value)) {
      refX.value = refEl.value.scrollX;
      refY.value = refEl.value.scrollY;
    } else if (refEl.value) {
      refX.value = (refEl.value as Element).scrollLeft;
      refY.value = (refEl.value as Element).scrollTop;
    }
  };

  if (isObject(options)) {
    let wait = 0;
    if (options.wait && options.wait > 0) {
      wait = options.wait;
      Reflect.deleteProperty(options, 'wait');
    }

    handler = useThrottleFn(handler, wait);
  }

  let stopWatch: () => void;
  onMounted(() => {
    stopWatch = watch(
      refEl,
      (el, prevEl, onCleanup) => {
        if (el) {
          el.addEventListener('scroll', handler);
        } else if (prevEl) {
          prevEl.removeEventListener('scroll', handler);
        }
        onCleanup(() => {
          refX.value = refY.value = 0;
          el && el.removeEventListener('scroll', handler);
        });
      },
      { immediate: true },
    );
  });

  onUnmounted(() => {
    refEl.value && refEl.value.removeEventListener('scroll', handler);
  });

  function stop() {
    stopWatch && stopWatch();
  }

  return { refX, refY, stop };
}
