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

/**
The routing of this file will not show the layout.
It is an independent new page.
the contents of the file still need to log in to access
 */
import type { AppRouteModule } from '/@/router/types';
import UserLayout from '/@/views/user/Layout.vue';

const mainOutKey = '../../../../src/router/routes/mainOut.ts';
const _rootMainOut = import.meta.globEager('../../../../src/router/routes/mainOut.ts');
let rootMainOut = [];
if (_rootMainOut && _rootMainOut[mainOutKey]) {
  rootMainOut = _rootMainOut[mainOutKey]?.mainOutRoutes || [];
}

// test
// http:ip:port/main-out
export const mainOutRoutes: AppRouteModule[] = [
  {
    path: '/main-out',
    name: 'MainOut',
    component: () => import('/@/views/demo/main-out/index.vue'),
    meta: {
      title: 'MainOut',
      ignoreAuth: true,
    },
  },
  // {
  //   path: '/user',
  //   name: 'user',
  //   component: UserLayout,
  //   redirect: '/user/setting',
  //   meta: {
  //     title: '用户中心',
  //   },
  //   children: [
  //     {
  //       path: 'setting',
  //       name: 'setting',
  //       component: () => import('/@/views/user/basic/Setting.vue'),
  //       meta: {
  //         // affix: true,
  //         title: '基本信息',
  //       },
  //     },
  //     {
  //       path: 'pwd',
  //       name: 'pwd',
  //       component: () => import('/@/views/user/updatePwd/Passwrod.vue'),
  //       meta: {
  //         title: '修改密码',
  //       },
  //     },
  //   ],
  // },
  ...rootMainOut,
];

export const mainOutRouteNames = mainOutRoutes.map((item) => item.name);
