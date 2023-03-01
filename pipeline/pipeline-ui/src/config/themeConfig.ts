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

import themeConfig from '../../wutong-admin-builder-ui-template/src/config/themeConfig';

export const primaryColor = '#0070FF';
export const globalPrimaryColor = '#08153a'; // 黑色主题
export default {
  ...themeConfig,
  'header-height': '56px',
  'header-light-bottom-border-color': '#d9d9d9',
  // btn
  'btn-primary-bg': globalPrimaryColor,
  'button-primary-color': globalPrimaryColor,
  'button-primary-hover-color': '#0b215e',
  //radio
  'radio-button-checked-bg': globalPrimaryColor,
  'radio-button-active-color': primaryColor,
};
