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

// 参数工具

// 删除指定参数及相关的值
export function deleteParams(url: string, params: string[]) {
  for (let index = 0; index < params.length; index++) {
    const item = params[index];
    const fromIndex = url.indexOf(item + '='); //必须加=号，避免参数值中包含item字符串
    if (fromIndex !== -1) {
      // 通过url特殊符号，计算出=号后面的的字符数，用于生成replace正则
      const startIndex = url.indexOf('=', fromIndex);
      const endIndex = url.indexOf('&', fromIndex);
      const hashIndex = url.indexOf('#', fromIndex);

      let reg;
      if (endIndex !== -1) {
        // 后面还有search参数的情况
        const num = endIndex - startIndex;
        reg = new RegExp(item + '=.{' + num + '}');
        url = url.replace(reg, '');
      } else if (hashIndex !== -1) {
        // 有hash参数的情况
        const num = hashIndex - startIndex - 1;
        reg = new RegExp('&?' + item + '=.{' + num + '}');
        url = url.replace(reg, '');
      } else {
        // search参数在最后或只有一个参数的情况
        reg = new RegExp('&?' + item + '=.+');
        url = url.replace(reg, '');
      }
    }
  }
  const noSearchParam = url.indexOf('=');
  if (noSearchParam === -1) {
    url = url.replace(/\?/, ''); // 如果已经没有参数，删除？号
  }
  return url;
}

// 指定url添加参数
export function addParams(url: string, params: string) {
  let prefix = '?';
  if (url.indexOf('?') > -1) prefix = '&';
  url += prefix + params;
}
