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

export enum ItemCateGoryEnums {
  ALL = 'ALL',
  UN_FINISHED = 'unfinished',
  WORK = 'myWork',
  CREATE = 'myCreate',
  FOLLOW = 'myFollow',
  OPEN_ALL = 'openAll',
}

export const itemCateGoryOption = [
  { label: '全部', value: ItemCateGoryEnums.ALL, num: 0 },
  { label: '我未完成的', value: ItemCateGoryEnums.UN_FINISHED, num: 0 },
  { label: '我处理的', value: ItemCateGoryEnums.WORK, num: 0 },
  { label: '我创建的', value: ItemCateGoryEnums.CREATE, num: 0 },
  { label: '我关注的', value: ItemCateGoryEnums.FOLLOW, num: 0 },
  { label: '全部打开的', value: ItemCateGoryEnums.OPEN_ALL, num: 0 },
];
