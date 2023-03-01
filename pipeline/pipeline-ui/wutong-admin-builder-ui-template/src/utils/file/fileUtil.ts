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

/* 文件工具 */
import { getCustomEnvConfig } from '../lib/util';
const config = getCustomEnvConfig();

// 通过环境变量 生成 绝对路经
export function getFileUrl(url?: string | null, repeatUrl?: string) {
  if (!url) return repeatUrl || '';
  if (url.startsWith('data:image')) return url;
  if (url.startsWith('http')) return url;
  return config.resouces_url.replace(/\/$/, '') + url;
}
//取到文件名开始到最后一个点的长度
export function getFileNameLength(fileName: string) {
  return fileName.lastIndexOf('.');
}
//截取获得后缀名
export function getFileSuffix(fileName: string) {
  if (fileName) {
    const first: number = getFileNameLength(fileName);
    const namelength: number = fileName.length; //取到文件名长度
    return fileName.substring(first + 1, namelength);
  }
  return null;
}
