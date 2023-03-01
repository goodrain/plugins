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

export enum ItemsStatusEnums {
  NOTSTARTED = 'NOTSTARTED', //未开始
  PROCESSING = 'PROCESSING', //处理中
  FINISHED = 'FINISHED', //已完成
}
export const ItemsStatusOptions = [
  {
    label: '未开始',
    value: ItemsStatusEnums.NOTSTARTED,
    css: { color: '#055fe6', 'background-color': '#ddebff' },
  },
  {
    label: '处理中',
    value: ItemsStatusEnums.PROCESSING,
    css: { color: '#ec8205', 'background-color': '#ffe9cf' },
  },
  {
    label: '已完成',
    value: ItemsStatusEnums.FINISHED,
    css: { color: '#198061', 'background-color': '#c3f3cb' },
  },
];

export function getItemsStatus(val: ItemsStatusEnums) {
  const obj = ItemsStatusOptions.find((item) => item.value === val);
  return obj || { label: '' };
}
