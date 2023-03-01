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

import { ThemeEnum } from '../enums/appEnum';
import settings from '/@/config/settings'; // [修改]-新增
const { designSetting } = settings;
// [修改]全部修改

export const prefixCls = designSetting.prefixCls;

export const darkMode = ThemeEnum.LIGHT;

// app theme preset color
export const APP_PRESET_COLOR_LIST: string[] = designSetting.APP_PRESET_COLOR_LIST;

// header preset color
export const HEADER_PRESET_BG_COLOR_LIST: string[] = designSetting.HEADER_PRESET_BG_COLOR_LIST;

// sider preset color
export const SIDE_BAR_BG_COLOR_LIST: string[] = designSetting.SIDE_BAR_BG_COLOR_LIST;
