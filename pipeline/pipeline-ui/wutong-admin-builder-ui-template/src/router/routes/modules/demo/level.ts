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

import { getParentLayout, LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const permission: AppRouteModule = {
  path: '/level',
  name: 'Level',
  component: LAYOUT,
  redirect: '/level/menu1/menu1-1/menu1-1-1',
  meta: {
    orderNo: 2000,
    icon: 'ion:menu-outline',
    title: t('routes.demo.level.level'),
  },

  children: [
    {
      path: 'menu1',
      name: 'Menu1Demo',
      component: getParentLayout('Menu1Demo'),
      meta: {
        title: 'Menu1',
      },
      redirect: '/level/menu1/menu1-1/menu1-1-1',
      children: [
        {
          path: 'menu1-1',
          name: 'Menu11Demo',
          component: getParentLayout('Menu11Demo'),
          meta: {
            title: 'Menu1-1',
          },
          redirect: '/level/menu1/menu1-1/menu1-1-1',
          children: [
            {
              path: 'menu1-1-1',
              name: 'Menu111Demo',
              component: () => import('/@/views/demo/level/Menu111.vue'),
              meta: {
                title: 'Menu111',
              },
            },
          ],
        },
        {
          path: 'menu1-2',
          name: 'Menu12Demo',
          component: () => import('/@/views/demo/level/Menu12.vue'),
          meta: {
            title: 'Menu1-2',
          },
        },
      ],
    },
    {
      path: 'menu2',
      name: 'Menu2Demo',
      component: () => import('/@/views/demo/level/Menu2.vue'),
      meta: {
        title: 'Menu2',
        // ignoreKeepAlive: true,
      },
    },
  ],
};

export default permission;
