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

import type { DropMenu } from '../components/Dropdown';
import type { LocaleSetting, LocaleType } from '/#/config';
import { merge } from 'lodash-es';
import settings from '/@/config/settings'; // [修改]-新增
// [修改]全部修改

export const LOCALE: { [key: string]: LocaleType } = settings.localeSetting.LOCALE;

export const localeSetting: LocaleSetting = merge(
  {
    showPicker: true,
    // Locale
    locale: LOCALE.ZH_CN,
    // Default locale
    fallback: LOCALE.ZH_CN,
    // available Locales
    availableLocales: [LOCALE.ZH_CN, LOCALE.EN_US],
  },
  settings.localeSetting.localeSetting,
);

// locale list
export const localeList: DropMenu[] = settings.localeSetting.localeList;
