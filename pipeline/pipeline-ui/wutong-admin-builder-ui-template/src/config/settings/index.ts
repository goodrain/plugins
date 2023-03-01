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

import { merge, cloneDeep } from 'lodash-es';
import componentSetting from './componentSetting';
import * as designSetting from './designSetting';
import * as encryptionSetting from './encryptionSetting';
import * as localeSetting from './localeSetting';
import projectSetting from './projectSetting';
import * as siteSetting from './siteSetting';

const settings: any = {
  componentSetting,
  designSetting,
  encryptionSetting,
  localeSetting,
  projectSetting,
  siteSetting: cloneDeep(siteSetting),
};

// 读取外部文件
const settingPath = '../../../../src/config/settings/index.ts';
const rootModules = import.meta.globEager('../../../../src/config/settings/index.ts');
if (rootModules[settingPath]) {
  Object.entries(rootModules[settingPath].default).forEach(([key, value]) => {
    settings[key] = merge(settings[key], value);
  });
}

export default settings;
