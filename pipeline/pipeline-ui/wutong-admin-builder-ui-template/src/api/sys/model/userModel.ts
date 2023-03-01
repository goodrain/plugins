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

// [修改]- 全部修改
import { UserInfo } from '/#/store';
/**
 * @description: Login interface parameters
 */
export interface LoginParams {
  username: string;
  password: string;
  captchaCode: string;
  captchaKey: string;
  loginType?: string;
}

export interface RoleInfo {
  roleName: string;
  value: string;
}

/**
 * @description: Login interface return value
 */
export interface LoginResultModel {
  userId: string | number;
  Authorization: string;
  RefreshToken: string;
}

// 应用信息
export interface UserApp {
  id: string;
  name: string;
  resource_system_id: string;
}

/**
 * @description: Get user information return value
 */
export type GetUserInfoModel = UserInfo;
