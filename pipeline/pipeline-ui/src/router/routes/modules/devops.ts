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

import type { AppRouteModule } from '/@/router/types';
import { LAYOUT } from '/@/router/constant';

// test
// http:ip:port/main-out
export const devops: AppRouteModule[] = [
  // {
  //   path: '',
  //   name: '首页Parent',
  //   component: LAYOUT,
  //   meta: {
  //     single: true,
  //     title: '首页',
  //     affix: false,
  //   },
  //   children: [
  //     {
  //       path: '/home',
  //       name: '首页',
  //       component: () => import('/@@/views/home/index.vue'),
  //       meta: {
  //         title: '首页',
  //         icon: 'dashboard|svg',
  //       },
  //     },
  //   ],
  // },
  {
    path: '',
    name: '应用服务Parent',
    component: LAYOUT,
    meta: {
      single: true,
      title: '应用服务',
      affix: false,
    },
    children: [
      {
        path: '/app-service',
        name: '应用服务',
        component: () => import('/@@/views/app-service/index.vue'),
        meta: {
          title: '应用服务',
          icon: 'application|svg',
        },
      },
    ],
  },
  {
    path: '',
    name: '代码管理Parent',
    component: LAYOUT,
    meta: {
      single: true,
      title: '代码管理',
      affix: false,
    },
    children: [
      {
        path: '/code-manage',
        name: '代码管理',
        component: () => import('/@@/views/code-manage/index.vue'),
        meta: {
          title: '代码管理',
          icon: 'code_manager|svg',
        },
      },
    ],
  },
  {
    path: '',
    name: '镜像仓库Parent',
    component: LAYOUT,
    meta: {
      single: true,
      title: '镜像仓库',
      affix: false,
    },
    children: [
      {
        path: '/image',
        name: '镜像仓库',
        component: () => import('/@@/views/image/index.vue'),
        meta: {
          title: '镜像仓库',
          icon: 'image|svg',
        },
      },
    ],
  },
  {
    path: '',
    name: '部署历史Parent',
    component: LAYOUT,
    meta: {
      single: true,
      title: '部署历史',
      affix: false,
    },
    children: [
      {
        path: '/deployment-history',
        name: '部署历史',
        component: () => import('/@@/views/deployment-history/index.vue'),
        meta: {
          title: '部署历史',
          icon: 'history_deploy|svg',
        },
      },
    ],
  },
  {
    path: '',
    name: '流水线管理Parent',
    component: LAYOUT,
    meta: {
      single: true,
      title: '流水线管理',
      affix: false,
      hidePathForChildren: false,
    },
    children: [
      {
        path: '/pipeline',
        name: '流水线管理',
        component: () => import('/@@/views/pipeline/index.vue'),
        meta: {
          icon: 'pipeline|svg',
          title: '流水线管理',
        },
      },
    ],
  },
];

export default devops;
