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

import { getConfigFileName } from '../../../build/getConfigFileName';
import { getCustomEnvConfig as _getCustomEnvConfig } from '/@/utils/lib/util';

export const development = process.env.NODE_ENV === 'development';

export const stageStatus = {
  running: { name: '运行中', icon: 'ic:sharp-play-circle-outline', color: '#006DFF' },
  success: { name: '成功', icon: 'material-symbols:check-circle-outline', color: '#06C77F' },
  pending: { name: '等待中', icon: 'mdi:clock-time-three-outline', color: '#FFBF77' },
  skipped: { name: '跳过', icon: 'bx:skip-next-circle', color: '#5FA4FD' },
  canceled: { name: '取消', icon: 'line-md:cancel', color: '#CCD9FF' },
  failed: { name: '失败', icon: 'jam:close-circle', color: '#FF5D64' },
  created: { name: '创建', icon: 'material-symbols:add-circle-outline', color: '#A4ADC5' },
};

// 获取自定义环境变量
export function getCustomEnvConfig() {
  return {
    ..._getCustomEnvConfig(),
  };
}

//生成uuid
export function getUUID(len, radix) {
  var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
  var uuid: any = [];
  var i;
  radix = radix || chars.length;
  if (len) {
    for (i = 0; i < len; i++) uuid[i] = chars[0 | (Math.random() * radix)];
  } else {
    var r;
    uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
    uuid[14] = '4';
    for (i = 0; i < 36; i++) {
      if (!uuid[i]) {
        r = 0 | (Math.random() * 16);
        uuid[i] = chars[i === 19 ? (r & 0x3) | 0x8 : r];
      }
    }
  }
  return uuid.join('');
}

//数组扁平化
export function flatten(origin) {
  let result: any[] = [];
  for (let i = 0; i < origin?.length; i++) {
    let item = origin[i];
    result.push(item);
    if (Array.isArray(item.replies)) {
      result = result.concat(flatten(item.replies));
    }
  }
  return result;
}

//从url获取参数
export const getQuery = (name: string, url?: string) => {
  const params = new URLSearchParams(url || window.location.search);
  const value = params.get(name);
  if (value) {
    return value;
  }
  return getQueryString(name, url);
};

export const getQueryString = (name: string, url?: string) => {
  const reg = new RegExp('(^|&|/?)' + name + '=([^&]*)(&|$)', 'i');
  const r = encodeURI(url || window.location.search || window.location.href || window.location.hash)
    .substr(1)
    .match(reg);
  if (r != null) return unescape(r[2]);
  return null;
};
