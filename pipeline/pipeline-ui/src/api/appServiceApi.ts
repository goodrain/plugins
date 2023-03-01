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

// 获取应用服务表列表分页
export function getServiceList(teamId) {
  return defHttp.get<any>({ url: `/app-service/list/${teamId}` });
}

// 获取应用服务代码仓库的
export function getCloneUrl(appId) {
  return defHttp.get<any>({ url: `/app-service/url/${appId}` });
}

// 获取应用服务子模块列表（微服务模块类型的应用服务）
export function getSubService(appId) {
  return defHttp.get<any>({ url: `/app-service/${appId}/subService` });
}

//获取代码质量检查最近的记录
export function findCodeQualityLatest(appId) {
  return defHttp.get<any>({ url: `/sonarqube/find-latest?appServiceId=${appId}` });
}
