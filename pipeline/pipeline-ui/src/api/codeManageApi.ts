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

type Params = {
  regionCode: string;
  appServiceId: string;
  [prop: string]: any;
};

// 获取分支表列表分页
export function getPagelist(params: Params) {
  return defHttp.post<any>({ url: `/branch/pagelist`, params });
}

// 获取应用服务的ci流水线表列表分页
export function getPageIntergrationlist(params: Params) {
  return defHttp.post<any>({ url: `/app-service-pipeline/pagelist`, params });
}

// 构建服务
export function serviceRun(params: any) {
  return defHttp.post<any>({ url: `/app-service-pipeline/pipeline/run-vars`, params });
}

// 获取git-ci 环境变量
export function getEnvs(params: any) {
  return defHttp.get<any>({ url: `/app-service-pipeline/pipeline/vars`, params });
}
