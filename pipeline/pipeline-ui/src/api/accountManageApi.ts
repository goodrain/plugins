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

import { account } from '/@@/api/index';
import { defHttp } from '/@/utils/http/axios';

const Api = {
  UserPageList: `${account}/user/page`, // 账户列表
  // AddUser: `${account}/user/add`, // 新增账户
  // UpdateUser: `${account}/user/update`, // 修改账户
  // Enableorforbiduser: `${account}/user/enableorforbiduser`, // 启用禁用
  // UserDelete2: `${account}/user/delete2`, // 删除
  // UserDetail: `${account}/user/detail`, // 详情
};

// 账户列表
export function getUserPageList(params) {
  return defHttp.post({ url: Api.UserPageList, params });
}
// 设置状态
// export function setEnableorforbiduser(params) {
//   return defHttp.post({ url: Api.Enableorforbiduser, params });
// }
// //新增账户
// export function addUser(params) {
//   return defHttp.post({ url: Api.AddUser, params });
// }
// //修改账户
// export function updateUser(params) {
//   return defHttp.post({ url: Api.UpdateUser, params });
// }
// //删除
// export function deleteUser(params) {
//   return defHttp.post({ url: Api.UserDelete2, params });
// }
// // 获取详情
// export function getUserDetail(id: string) {
//   return defHttp.get({ url: Api.UserDetail + '/' + id });
// }
