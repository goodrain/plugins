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

import { DefaultOptionType } from 'ant-design-vue/lib/select';

// 项目状态
export enum ProjectStatusEnums {
  CLOSED = 'CLOSED',
  DOING = 'DOING',
  WAIT = 'WAIT',
}
// 返回 option
export const projectStatusOptions: DefaultOptionType[] = [
  { label: getProjectStatusText(ProjectStatusEnums.CLOSED), value: ProjectStatusEnums.CLOSED },
  { label: getProjectStatusText(ProjectStatusEnums.DOING), value: ProjectStatusEnums.DOING },
  { label: getProjectStatusText(ProjectStatusEnums.WAIT), value: ProjectStatusEnums.WAIT },
];
// 返回状态
export function getProjectStatusText(status: ProjectStatusEnums) {
  const obj = {
    [ProjectStatusEnums.CLOSED]: '已关闭',
    [ProjectStatusEnums.DOING]: '进行中',
    [ProjectStatusEnums.WAIT]: '未开始',
  };
  return obj[status] ?? '无';
}
