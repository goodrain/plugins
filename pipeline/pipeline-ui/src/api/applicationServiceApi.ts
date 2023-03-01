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

/* 应用服务 */
import { defHttp } from '/@/utils/http/axios';

// 获取应用服务表列表分页
export function pagelist(params) {
  return defHttp.post<any>({ url: `/app-service/pagelist`, params });
}
// 新增或修改应用服务表
export function update(params) {
  const url = params.id ? `/app-service/update` : `/app-service/add`;
  return defHttp.post<any>({ url, params });
}
// 新增子服务
export function addChildrenService(id: string, params: string[]) {
  return defHttp.post<any>({ url: `/app-service/${id}/subService`, params });
}
// 查询应用服务模板
export function template(params?) {
  return defHttp.get<any>({ url: `/app-service/template`, params });
}
// 获取应用服务表详情
export function serviceDetail(appServiceId) {
  return defHttp.get<any>({ url: `/app-service/detail/${appServiceId}` });
}
//是否能开启自动部署
export function canDeploy(id, subId) {
  const url = subId ? `/app-service/${id}/${subId}/can_deploy` : `app-service/${id}/can_deploy`;
  return defHttp.get<any>({ url });
}
//获取应用服务子模块列表
export function subService(id) {
  return defHttp.get<any>({ url: `/app-service/${id}/subService` });
}
//测试绑定外部gitlab仓库时，参数是否正确
export function checkGitlab(params) {
  return defHttp.get<any>({ url: `/app-service/check`, params }, { errorMessageMode: 'none' });
}

//删除应用服务
export function deleteAppService(params) {
  const { id } = params;
  return defHttp.post<any>({ url: `/app-service/delete/${id}` });
}

//获取仓库配置
export function getHubConfig(params) {
  const { id } = params;
  return defHttp.get<any>({ url: `/app-service/${id}/url` });
}

//更新仓库配置
export function updateHubConfig(params) {
  const { id, ...otherParams } = params;
  return defHttp.post<any>({ url: `/app-service/${id}/modify-externel`, params: otherParams });
}
