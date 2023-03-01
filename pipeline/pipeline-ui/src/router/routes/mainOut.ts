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

// test
// http:ip:port/main-out
export const mainOutRoutes: AppRouteModule[] = [
  {
    path: '/team',
    name: '项目列表',
    component: () => import('/@@/views/project/index.vue'),
    meta: {
      title: '项目首页',
      ignoreAuth: true,
    },
  },
  {
    path: '/pipeline-manage',
    name: 'pipelineManage',
    component: () => import('/@@/views/pipeline/manage/index.vue'),
    meta: {
      icon: 'ion:grid-outline',
      title: '流水线管理',
    },
  },
  /* {
    path: '/test',
    name: '项目列表',
    component: () => import('/@@/views/project-collaboration/index.vue'),
    meta: {
      title: '项目首页',
      ignoreAuth: true,
    },
  }, */
];

export const mainOutRouteNames = mainOutRoutes.map((item) => item.name);
