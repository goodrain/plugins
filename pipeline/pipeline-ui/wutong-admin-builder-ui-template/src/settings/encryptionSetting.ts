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

import settings from '/@/config/settings'; // [修改]-新增
const { encryptionSetting } = settings;
// [修改]全部修改

// System default cache time, in seconds
export const DEFAULT_CACHE_TIME = encryptionSetting.DEFAULT_CACHE_TIME;

// aes encryption key
export const cacheCipher = encryptionSetting.cacheCipher;

// Whether the system cache is encrypted using aes
export const enableStorageEncryption = encryptionSetting.enableStorageEncryption;
