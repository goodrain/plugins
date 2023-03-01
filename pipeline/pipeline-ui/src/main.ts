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

import 'virtual:windi-base.css';
import 'virtual:windi-components.css';
import 'virtual:windi-utilities.css';
import '/@/design/index.less';
import '/@@/assets/style/base.less';
// Register icon sprite
import { Collapse, Tabs } from 'ant-design-vue';
import 'virtual:svg-icons-register';
import { createApp } from 'vue';
import App from './App.vue';
import { registerGlobComp } from '/@/components/registerGlobComp';
import { setupGlobDirectives } from '/@/directives';
import { setupI18n } from '/@/locales/setupI18n';
import { setupErrorHandle } from '/@/logics/error-handle';
import { initAppConfigStore } from '/@/logics/initAppConfig';
import { router, setupRouter } from '/@/router';
import { setupRouterGuard } from '/@/router/guard';
import { setupStore } from '/@/store';

async function bootstrap() {
  const app = createApp(App);

  app.use(Collapse).use(Tabs);

  // Configure store
  // 配置 store
  setupStore(app);

  // Initialize internal system configuration
  // 初始化内部系统配置
  initAppConfigStore();

  // Register global components
  // 注册全局组件
  registerGlobComp(app);

  // Multilingual configuration
  // 多语言配置
  // Asynchronous case: language files may be obtained from the server side
  // 异步案例：语言文件可能从服务器端获取
  await setupI18n(app);

  // Configure routing
  // 配置路由
  setupRouter(app);

  // router-guard
  // 路由守卫
  setupRouterGuard(router);

  // Register global directive
  // 注册全局指令
  setupGlobDirectives(app);

  // Configure global error handling
  // 配置全局错误处理
  setupErrorHandle(app);

  // https://next.router.vuejs.org/api/#isready
  // await router.isReady();

  app.mount('#app');
}

bootstrap();
