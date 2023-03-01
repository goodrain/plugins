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

import type { Router } from 'vue-router';
import { useAppStore } from '/@/store/modules/app';
import { useMultipleTabStore } from '/@/store/modules/multipleTab';
import { useUserStore } from '/@/store/modules/user';
import { usePermissionStore } from '/@/store/modules/permission';
import { PageEnum } from '/@/enums/pageEnum';
import { removeTabChangeListener } from '/@/logics/mitt/routeChange';

export function createStateGuard(router: Router) {
  router.afterEach((to) => {
    // Just enter the login page and clear the authentication information
    if (to.path === PageEnum.BASE_LOGIN) {
      const tabStore = useMultipleTabStore();
      const userStore = useUserStore();
      const appStore = useAppStore();
      const permissionStore = usePermissionStore();
      appStore.resetAllState();
      permissionStore.resetState();
      tabStore.resetState();
      userStore.resetState();
      removeTabChangeListener();
    }
  });
}
