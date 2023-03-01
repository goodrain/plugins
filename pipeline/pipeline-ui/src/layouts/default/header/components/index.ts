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

import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
import FullScreen from '/@/layouts/default/header/components/FullScreen.vue';

export const UserDropDown = createAsyncComponent(
  () => import('/@/layouts/default/header/components/user-dropdown/index.vue'),
  {
    loading: true,
  },
);

export const LayoutBreadcrumb = createAsyncComponent(
  () => import('/@/layouts/default/header/components/Breadcrumb.vue'),
);

// [修改]-新增
export const ProjectDropdown = createAsyncComponent(() => import('./ProjectDropdown.vue'));

export const Notify = createAsyncComponent(
  () => import('/@/layouts/default/header/components/notify/index.vue'),
);

export const ErrorAction = createAsyncComponent(
  () => import('/@/layouts/default/header/components/ErrorAction.vue'),
);

export { FullScreen };
