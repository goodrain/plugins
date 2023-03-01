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

import { getConfigFileName } from '../../../build/getConfigFileName';
import { AuthorizationEnum } from '../../enums/authorizationEnum';
export const development = process.env.NODE_ENV === 'development';

/**
 * 从URL获取指定参数
 */
export const getQuery = (name: string, url?: string) => {
  const params = new URLSearchParams(url || window.location.search);
  const value = params.get(name);
  if (value) {
    return value;
  }
  return getQueryString(name, url);
};

export const getQueryString = (name: string, url?: string) => {
  const reg = new RegExp('(^|&|/?)' + name + '=([^&]*)(&|$)', 'i');
  const r = encodeURI(url || window.location.search || window.location.href || window.location.hash)
    .substr(1)
    .match(reg);
  if (r != null) return unescape(r[2]);
  return null;
};

// 设置空、null、underfined为默认值
export function setEmptyDefaultValue(value, devalutValue) {
  if (typeof value === 'string' && value.trim() === '') return devalutValue;
  return value ?? devalutValue;
}

// 获取自定义环境变量
export function getCustomEnvConfig() {
  const ENV_NAME = getConfigFileName(import.meta.env);

  const ENV = import.meta.env.DEV
    ? // Get the global configuration (the configuration will be extracted independently when packaging)
      (import.meta.env as any)
    : window[ENV_NAME as any];
  const {
    VITE_GLOB_OAUTHOR,
    VITE_GLOB_OAUTHOR_ID,
    VITE_GLOB_OAUTHOR_SECRET,
    VITE_GLOB_RESOURCES_URL,
    VITE_GLOB_USER_LOGIN_URL,
    VITE_GLOB_SELECT_APP_CENTER,
    VITE_GLOB_LOGIN_BACK,
  } = ENV;

  return {
    oauth: setEmptyDefaultValue(VITE_GLOB_OAUTHOR, AuthorizationEnum.JWT), // 授权方式
    oauth_id: VITE_GLOB_OAUTHOR_ID, // id
    oauth_secret: VITE_GLOB_OAUTHOR_SECRET, // secet
    resouces_url: VITE_GLOB_RESOURCES_URL, // 静态资源地址
    login_url: VITE_GLOB_USER_LOGIN_URL, // 登录页面地址
    appCenter_url: VITE_GLOB_SELECT_APP_CENTER, // 选择应用地址
    loginBack: setEmptyDefaultValue(
      VITE_GLOB_LOGIN_BACK === true || VITE_GLOB_LOGIN_BACK === 'true' || VITE_GLOB_LOGIN_BACK,
      false,
    ), // 退出或去登录页面后，登录完成后是否需要返回当前页面
  };
}
