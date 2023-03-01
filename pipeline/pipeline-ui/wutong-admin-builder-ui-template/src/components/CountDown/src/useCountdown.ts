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

import { ref, unref } from 'vue';
import { tryOnUnmounted } from '@vueuse/core';

export function useCountdown(count: number) {
  const currentCount = ref(count);

  const isStart = ref(false);

  let timerId: ReturnType<typeof setInterval> | null;

  function clear() {
    timerId && window.clearInterval(timerId);
  }

  function stop() {
    isStart.value = false;
    clear();
    timerId = null;
  }

  function start() {
    if (unref(isStart) || !!timerId) {
      return;
    }
    isStart.value = true;
    timerId = setInterval(() => {
      if (unref(currentCount) === 1) {
        stop();
        currentCount.value = count;
      } else {
        currentCount.value -= 1;
      }
    }, 1000);
  }

  function reset() {
    currentCount.value = count;
    stop();
  }

  function restart() {
    reset();
    start();
  }

  tryOnUnmounted(() => {
    reset();
  });

  return { start, reset, restart, clear, stop, currentCount, isStart };
}
