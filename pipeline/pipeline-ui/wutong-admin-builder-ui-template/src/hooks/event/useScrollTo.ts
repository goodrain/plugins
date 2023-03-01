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

import { isFunction, isUnDef } from '/@/utils/is';
import { ref, unref } from 'vue';

export interface ScrollToParams {
  el: any;
  to: number;
  duration?: number;
  callback?: () => any;
}

const easeInOutQuad = (t: number, b: number, c: number, d: number) => {
  t /= d / 2;
  if (t < 1) {
    return (c / 2) * t * t + b;
  }
  t--;
  return (-c / 2) * (t * (t - 2) - 1) + b;
};
const move = (el: HTMLElement, amount: number) => {
  el.scrollTop = amount;
};

const position = (el: HTMLElement) => {
  return el.scrollTop;
};
export function useScrollTo({ el, to, duration = 500, callback }: ScrollToParams) {
  const isActiveRef = ref(false);
  const start = position(el);
  const change = to - start;
  const increment = 20;
  let currentTime = 0;
  duration = isUnDef(duration) ? 500 : duration;

  const animateScroll = function () {
    if (!unref(isActiveRef)) {
      return;
    }
    currentTime += increment;
    const val = easeInOutQuad(currentTime, start, change, duration);
    move(el, val);
    if (currentTime < duration && unref(isActiveRef)) {
      requestAnimationFrame(animateScroll);
    } else {
      if (callback && isFunction(callback)) {
        callback();
      }
    }
  };
  const run = () => {
    isActiveRef.value = true;
    animateScroll();
  };

  const stop = () => {
    isActiveRef.value = false;
  };

  return { start: run, stop };
}
