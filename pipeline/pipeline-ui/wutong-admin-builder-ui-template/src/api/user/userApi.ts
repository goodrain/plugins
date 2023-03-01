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

import { account } from '/@/api/index';
import { defHttp } from '/@/utils/http/axios';

const Api = {
  UserInfo: `${account}/user/querypersonaldetails`, // 用户信息
  Update: `${account}/user/update`, // 更新用户信息
  UpdatePwd: `${account}/user/updatepassword`, // 修改原密码
  BindMoblie: `${account}/user/bindmoblie`, // 绑定手机号
  BindEmail: `${account}/user/bindemail`, // 绑定邮箱
};

// 用户信息
export function getUserInfo(userId: string) {
  return defHttp.get({ url: Api.UserInfo + '/' + userId, params: { userId } });
}

// 更新用户信息
export function updateUser(params: any) {
  return defHttp.post({ url: Api.Update, params });
}

// 修改原密码
export function UpdatePwd(params: any) {
  return defHttp.post({ url: Api.UpdatePwd, params });
}

// 修改原密码
export function bindMoblie(params: { key: string; mobile: string; captchaCode: string }) {
  return defHttp.post({ url: Api.BindMoblie, params });
}

// 修改邮箱
export function bindEmail(params: { key: string; email: string; captchaCode: string }) {
  return defHttp.post({ url: Api.BindEmail, params });
}
