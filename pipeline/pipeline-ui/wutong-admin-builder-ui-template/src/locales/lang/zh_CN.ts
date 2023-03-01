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

import { genMessage } from '../helper';
import antdLocale from 'ant-design-vue/es/locale/zh_CN';
import { merge } from 'lodash-es'; // [修改]-新增
// [修改]-新增-开始
const rootModules = {};
try {
  const _rootModules: any = import.meta.globEager('../../../../src/locales/lang/zh-CN/**/*.ts');
  Object.entries(_rootModules).forEach(([key, value]) => {
    const newKey = key.replace('../../../../src/locales/lang/', './');
    rootModules[newKey] = value;
  });
} catch {}
// [修改]-新增-结束
const modules = import.meta.globEager('./zh-CN/**/*.ts');

const newModules = merge(modules, rootModules); // [修改]-新增

export default {
  message: {
    ...genMessage(newModules, 'zh-CN'), // [修改]
    antdLocale,
  },
};
