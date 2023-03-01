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

import { defHttp } from '/@/utils/http/axios';
import { account, oauth, application } from '/@@/api/index';
import { TenantModel } from './model/tenantModel';
import { AppModule } from './model/appModule';

const Api = {
  auth: `${oauth}/auth/`,
  easy: `${oauth}/auth/easy`, // 简单登录
  tenant: `${account}/tenant/findcurrenttenantlist`,
  appList: `${application}/app/getappbyuser`,
};

/**
 * 登录信息
 * @param id
 * @returns
 */
export function ssoLoginInfo(id: number | string) {
  return defHttp.get<any>({ url: Api.auth + id });
}
// 简单登录
export function ssoLoginEasy(params: any) {
  return defHttp.get<any>({ url: Api.easy, params });
}
// 租户列表
export function getTenantList() {
  return defHttp.get<TenantModel[]>({ url: Api.tenant });
}
// app列表
export function getAppList() {
  return defHttp.get<AppModule[]>({ url: Api.appList });
}
