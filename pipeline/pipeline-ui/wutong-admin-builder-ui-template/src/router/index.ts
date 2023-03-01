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

import type { RouteRecordRaw } from 'vue-router';
import type { App } from 'vue';

import { createRouter, createWebHistory } from 'vue-router'; // [修改]
import { basicRoutes } from './routes';

// [修改]-以下基本都修改了，修改的比较多

let newRouter: any = null;
let newResetRouter: any = null;
let newSetupRouter: any = null;

const rootKey = '../../../src/router/index.ts';
const rootRouter = import.meta.globEager('../../../src/router/index.ts');

if (rootRouter && rootRouter[rootKey]) {
  const root = rootRouter[rootKey];
  if (root.resetRouter) {
    newResetRouter = root.resetRouter;
  }
  if (root.router) {
    newRouter = root.router;
  }
  if (root.setupRouter) {
    newSetupRouter = root.setupRouter;
  }
}
// 白名单应该包含基本静态路由
const WHITE_NAME_LIST: string[] = [];
const getRouteNames = (array: any[]) =>
  array.forEach((item) => {
    WHITE_NAME_LIST.push(item.name);
    getRouteNames(item.children || []);
  });
getRouteNames(basicRoutes);

// app router
// 创建一个可以被 Vue 应用程序使用的路由实例
const router = createRouter({
  // 创建一个 hash 历史记录。
  history: createWebHistory(import.meta.env.VITE_PUBLIC_PATH), // [修改]
  // 应该添加到路由的初始路由列表。
  routes: basicRoutes as unknown as RouteRecordRaw[],
  // 是否应该禁止尾部斜杠。默认为假
  strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// reset router
function resetRouter() {
  router.getRoutes().forEach((route) => {
    const { name } = route;
    if (name && !WHITE_NAME_LIST.includes(name as string)) {
      router.hasRoute(name) && router.removeRoute(name);
    }
  });
}

// config router
// 配置路由器
function setupRouter(app: App<Element>) {
  app.use(router);
}

if (!newRouter) {
  newRouter = router;
}
if (!newResetRouter) {
  newResetRouter = resetRouter;
}
if (!newSetupRouter) {
  newSetupRouter = setupRouter;
}
export { newRouter as router, newResetRouter as resetRouter, newSetupRouter as setupRouter };
