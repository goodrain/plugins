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

import { getQuery } from '../lib/util';
import { openWindow } from '..';
import { oauth } from '/@/api';
import { defHttp } from '/@/utils/http/axios';
/* 权限相关操作 */
import { AuthorizationEnum } from '/@/enums/authorizationEnum';
import { message, Modal } from 'ant-design-vue';
import { useUserStore } from '/@/store/modules/user';
import { getAuthCache, setAuthCache } from '.';
import { TOKEN_KEY } from '/@/enums/cacheEnum';
import { getCustomEnvConfig } from '../lib/util';
import rootConfig from '../../../../src/config';
import { router } from '/@/router/index';

const state = import.meta.env.VITE_GLOB_APP_SHORT_NAME || 'build';
const config = getCustomEnvConfig();

// 当退出后，需要登录，登录完成后还要返回当前页面时
function setBackConfig() {
  let backUrl: string | null = null;
  if (config.loginBack) {
    backUrl = `?appCode=${rootConfig?.appCode}&goBack=true`; // 退出时，需要登录后再返回
  }
  return backUrl;
}
// 登出
export const LoginOut = () => {
  if (config.oauth === AuthorizationEnum.JWT) {
    const backUrl: string | null = setBackConfig();
    const loginUrl = config.login_url.replace(/\/$/, '') + `/loginout${backUrl}`;
    openWindow(loginUrl, { target: '_self' });
  } else {
    openWindow(config.login_url, { target: '_self' });
  }
};
// 登录页
export const Login = () => {
  if (config.oauth === AuthorizationEnum.JWT) {
    // const loginUrl = config.login_url.replace(/\/$/, '') + `/?appCode=${rootConfig?.appCode}`;
    // openWindow(loginUrl, { target: '_self' });
    router.push('/401');
  } else {
    const code = getQuery('code');
    const queryState = getQuery('state');
    if (queryState && code && queryState === state) {
      // 删除code
      const href = decodeURIComponent(window.location.href);
      const url = href.replace('code=' + code, '');
      window.history.replaceState({}, '', url);
      defHttp
        .get<any>(
          {
            url: `${oauth}/auth/token`,
            params: {
              client_id: config.oauth_id,
              client_secret: config.oauth_secret,
              grant_type: 'authorization_code',
              code,
            },
          },
          { errorMessageMode: 'none', isReturnNativeResponse: true },
        )
        .then((res) => {
          if (res.data.access_token) {
            const userStore = useUserStore();
            userStore.setToken('Bearer ' + res.data.access_token);
            userStore.afterLoginAction(true);
          } else if (res.data.code === '0-120001') {
            Modal.error({
              title: '错误',
              content: 'code已过期，请重新登录！',
              okText: '确定',
              onOk: goOauth,
            });
          } else {
            message.error(res.data.msg);
          }
        });
    } else {
      router.push('/401');
      //goOauth();
    }
  }
};

function goOauth() {
  const localUrl = location.origin + import.meta.env.VITE_PUBLIC_PATH || '';
  /* const params = encodeURIComponent(
    `${localUrl}&state=${state}&client_id=${OAUTH2.id}&response_type=code`,
  ); */
  const params = `${localUrl}&state=${state}&client_id=${config.oauth_id}&response_type=code`;
  const oauthUrl = `${config.login_url.replace(/\/$/, '')}/login?redirect_uri=${params}`;
  openWindow(oauthUrl, { target: '_self' });
}

// 获取token信息并删除token参数
export function getToken(): string | false {
  // 先获取url
  const token = getQuery('token') as string;
  const teamId = getQuery('teamId') as string;
  const teamName = getQuery('teamName') as string;

  if (teamId && teamName) {
    localStorage.setItem('TEAMID', teamId);
    localStorage.setItem('TEAMNAME', teamName);
  }
  const urlToken = decodeURIComponent(token);
  if (urlToken) {
    setAuthCache(TOKEN_KEY, urlToken);
    const href = decodeURIComponent(window.location.href);
    const hrefToken = getQuery('token', href) as string;
    let url = href.replace(hrefToken, '');
    url = url.replace('?token=', '');
    window.history.replaceState({}, '', url);
    return urlToken;
  }

  const jwtToken = localStorage.getItem('JWTTOKEN') as string;
  if (jwtToken) {
    setAuthCache(TOKEN_KEY, jwtToken);
    localStorage.removeItem('JWTTOKEN');
    return jwtToken;
  }
  return getAuthCache<string>(TOKEN_KEY);
}
