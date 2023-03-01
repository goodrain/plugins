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
import { LoginParams, LoginResultModel, GetUserInfoModel } from './model/userModel';
import { ErrorMessageMode } from '/#/axios';
import { oauth, account } from '/@@/api/index';

const Api = {
  Login: `${oauth}/login`,
  Logout: `${oauth}/logout`,
  // GetUserInfo: `${account}/current?ext=false`,
  GetPermCode: `${oauth}/getPermCode`,
  captcha: `${oauth}/captcha`,
  getproviderinfo: `${oauth}/thirdoauth/getproviderinfo/`,
  mail: `${oauth}/mail`,
  getSmsCode: `${oauth}/sms`,
};

/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'message') {
  return defHttp.post<LoginResultModel>(
    {
      url: Api.Login,
      params,
    },
    {
      errorMessageMode: mode,
    },
  );
}

/**
 * @description: getUserInfo
 */
// export function getUserInfo() {
//   return defHttp.get<GetUserInfoModel>({ url: Api.GetUserInfo }, { errorMessageMode: 'modal' });
// }

export function getCaptcha() {
  return defHttp.get({ url: Api.captcha });
}

export function getPermCode() {
  // 没用到，所以直接返写死
  return {
    '1': ['1000', '3000', '5000'],

    '2': ['2000', '4000', '6000'],
  }['1'];
}

export function doLogout() {
  return defHttp.get({ url: Api.Logout });
}
export function getproviderinfo() {
  return defHttp.get<any>({
    //wechat_enterprise 为当前默认 登录方式
    url: Api.getproviderinfo + 'wechat_enterprise?tenantId=defaultTenantId', //暂时默认为默认租户（defaultTenantId） 后去扩展动态
  });
}
export function getEmailCode(email: string, getType: string) {
  return defHttp.get({ url: Api.mail, params: { email: email, getType: getType } });
}

export function getSmsCode(email: string, getType: string) {
  return defHttp.get({ url: Api.getSmsCode, params: { mobile: email, getType: getType } });
}

export function getTenant(params: { loginUrl: string; tenantId?: string | null }) {
  return defHttp.get({
    url: `${account}/tenant/avoid/findtenantbyloginurl`,
    params,
  });
}
export function testRetry() {
  return defHttp.get(
    { url: '/testRetry' },
    {
      retryRequest: {
        isOpenRetry: true,
        count: 5,
        waitTime: 1000,
      },
    },
  );
}
