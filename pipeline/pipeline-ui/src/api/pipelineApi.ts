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

// 分页自定义流水线
export function getPipelinePage(params: any) {
  return defHttp.post<any>({ url: `/custom-pipeline/page`, params });
}
// 查询自定义流水线模板列表
export function getPipelineTempList() {
  return defHttp.get<any>({ url: `/custom-pipeline-temp/list` });
}

// 自定义流水线列表
export function getPipelineList(teamId: string) {
  return defHttp.get<any>({ url: `/custom-pipeline/list/${teamId}` });
}

// 查询自定义流水线模板列表-详情
export function getPipelineTempDetail(id: string) {
  return defHttp.get<any>({ url: `/custom-pipeline-temp/detail/${id}` });
}
// 查询自定义流水线-详情
export function getPipelineDetail(id: string) {
  return defHttp.get<any>({ url: `/custom-pipeline/detail/${id}` });
}

// 创建自定义流水线
export function addAndUpdatePipeline(params) {
  if (params.id) return updatePipeline(params);
  return defHttp.post<any>({ url: `/custom-pipeline/add`, params });
}

// 更新自定义流水线
function updatePipeline(params: any) {
  return defHttp.post<any>({ url: `/custom-pipeline/update`, params });
}

// 复制自定义流水线
export function copyPipeline(id: string) {
  return defHttp.get<any>({ url: `/custom-pipeline/copy/${id}` });
}

// 删除自定义流水线
export function delPipeline(id: string) {
  return defHttp.get<any>({ url: `/custom-pipeline/delete/${id}` });
}
// 删除自定义流水线前需要判断是否绑定服务
export function delServicePipelineBind(id: string) {
  return defHttp.get<any>({ url: `/custom-pipeline/use/${id}` });
}
