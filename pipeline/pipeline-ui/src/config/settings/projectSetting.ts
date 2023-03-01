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

import type { ProjectConfig } from '/#/config';
import { PermissionModeEnum } from '/@/enums/appEnum';
import { primaryColor } from '../themeConfig';
// ! You need to clear the browser cache after the change
const setting: ProjectConfig = {
  // Permission mode
  permissionMode: PermissionModeEnum.ROUTE_MAPPING, // PermissionModeEnum.BACK 为请求接口
  // color [修改]
  themeColor: primaryColor,
  multiTabsSetting: {
    show: false,
  },
} as ProjectConfig;

export default setting;
