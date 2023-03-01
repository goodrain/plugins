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

// 模块
import { defHttp } from '/@/utils/http/axios';
import { agile } from '/@@/api';

// 获取模块列表分页
export function getPagelist(params: any) {
  if (params) {
    params.size = 999;
  }
  return defHttp.post<any>({ url: `${agile}/project-module/page`, params });
}
// 创建或修改
export function update(params: any) {
  return defHttp.post<any>({
    url: `${agile}/project-module/${params.id ? 'update' : 'add'}`,
    params,
  });
}

// 创建或修改
export function del(params: any) {
  return defHttp.post<any>({
    url: `${agile}/project-module/delete`,
    params,
  });
}
