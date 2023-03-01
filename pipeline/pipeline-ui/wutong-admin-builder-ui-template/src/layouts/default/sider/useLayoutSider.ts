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

import { computed, unref, onMounted, nextTick } from 'vue';

import { TriggerEnum } from '/@/enums/menuEnum';

import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
import { useDebounceFn } from '@vueuse/core';
import { useAppStore } from '/@/store/modules/app';

/**
 * Handle related operations of menu events
 */
export function useSiderEvent() {
  const appStore = useAppStore();
  const { getMiniWidthNumber } = useMenuSetting();

  const getCollapsedWidth = computed(() => {
    return unref(getMiniWidthNumber);
  });

  function onBreakpointChange(broken: boolean) {
    appStore.setProjectConfig({
      menuSetting: {
        siderHidden: broken,
      },
    });
  }

  return { getCollapsedWidth, onBreakpointChange };
}

/**
 * Handle related operations of menu folding
 */
export function useTrigger(getIsMobile: Ref<boolean>) {
  const { getTrigger, getSplit } = useMenuSetting();

  const getShowTrigger = computed(() => {
    const trigger = unref(getTrigger);

    return (
      trigger !== TriggerEnum.NONE &&
      !unref(getIsMobile) &&
      (trigger === TriggerEnum.FOOTER || unref(getSplit))
    );
  });

  const getTriggerAttr = computed(() => {
    if (unref(getShowTrigger)) {
      return {};
    }
    return {
      trigger: null,
    };
  });

  return { getTriggerAttr, getShowTrigger };
}

/**
 * Handle menu drag and drop related operations
 * @param siderRef
 * @param dragBarRef
 */
export function useDragLine(siderRef: Ref<any>, dragBarRef: Ref<any>, mix = false) {
  const { getMiniWidthNumber, getCollapsed, setMenuSetting } = useMenuSetting();

  onMounted(() => {
    nextTick(() => {
      const exec = useDebounceFn(changeWrapWidth, 80);
      exec();
    });
  });

  function getEl(elRef: Ref<ElRef | ComponentRef>): any {
    const el = unref(elRef);
    if (!el) return null;
    if (Reflect.has(el, '$el')) {
      return (unref(elRef) as ComponentRef)?.$el;
    }
    return unref(elRef);
  }

  function handleMouseMove(ele: HTMLElement, wrap: HTMLElement, clientX: number) {
    document.onmousemove = function (innerE) {
      let iT = (ele as any).left + (innerE.clientX - clientX);
      innerE = innerE || window.event;
      const maxT = 800;
      const minT = unref(getMiniWidthNumber);
      iT < 0 && (iT = 0);
      iT > maxT && (iT = maxT);
      iT < minT && (iT = minT);
      ele.style.left = wrap.style.width = iT + 'px';
      return false;
    };
  }

  // Drag and drop in the menu area-release the mouse
  function removeMouseup(ele: any) {
    const wrap = getEl(siderRef);
    document.onmouseup = function () {
      document.onmousemove = null;
      document.onmouseup = null;
      wrap.style.transition = 'width 0.2s';
      const width = parseInt(wrap.style.width);

      if (!mix) {
        const miniWidth = unref(getMiniWidthNumber);
        if (!unref(getCollapsed)) {
          width > miniWidth + 20
            ? setMenuSetting({ menuWidth: width })
            : setMenuSetting({ collapsed: true });
        } else {
          width > miniWidth && setMenuSetting({ collapsed: false, menuWidth: width });
        }
      } else {
        setMenuSetting({ menuWidth: width });
      }

      ele.releaseCapture?.();
    };
  }

  function changeWrapWidth() {
    const ele = getEl(dragBarRef);
    if (!ele) return;
    const wrap = getEl(siderRef);
    if (!wrap) return;

    ele.onmousedown = (e: any) => {
      wrap.style.transition = 'unset';
      const clientX = e?.clientX;
      ele.left = ele.offsetLeft;
      handleMouseMove(ele, wrap, clientX);
      removeMouseup(ele);
      ele.setCapture?.();
      return false;
    };
  }

  return {};
}
