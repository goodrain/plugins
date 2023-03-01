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

import contextMenuVue from './ContextMenu.vue';
import { isClient } from '/@/utils/is';
import { CreateContextOptions, ContextMenuProps } from './typing';
import { createVNode, render } from 'vue';

const menuManager: {
  domList: Element[];
  resolve: Fn;
} = {
  domList: [],
  resolve: () => {},
};

export const createContextMenu = function (options: CreateContextOptions) {
  const { event } = options || {};

  event && event?.preventDefault();

  if (!isClient) {
    return;
  }
  return new Promise((resolve) => {
    const body = document.body;

    const container = document.createElement('div');
    const propsData: Partial<ContextMenuProps> = {};
    if (options.styles) {
      propsData.styles = options.styles;
    }

    if (options.items) {
      propsData.items = options.items;
    }

    if (options.event) {
      propsData.customEvent = event;
      propsData.axis = { x: event.clientX, y: event.clientY };
    }

    const vm = createVNode(contextMenuVue, propsData);
    render(vm, container);

    const handleClick = function () {
      menuManager.resolve('');
    };

    menuManager.domList.push(container);

    const remove = function () {
      menuManager.domList.forEach((dom: Element) => {
        try {
          dom && body.removeChild(dom);
        } catch (error) {}
      });
      body.removeEventListener('click', handleClick);
      body.removeEventListener('scroll', handleClick);
    };

    menuManager.resolve = function (arg) {
      remove();
      resolve(arg);
    };
    remove();
    body.appendChild(container);
    body.addEventListener('click', handleClick);
    body.addEventListener('scroll', handleClick);
  });
};

export const destroyContextMenu = function () {
  if (menuManager) {
    menuManager.resolve('');
    menuManager.domList = [];
  }
};
