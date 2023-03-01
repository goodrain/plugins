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

export enum ItemsTypeEnums {
  ALL = -1,
  NEEDS = 'requirements',
  TASK = 'task',
  BUG = 'bug',
  SUB_ITEM = 'subitem',
  SPRINT = 'iteration',
  PLAN = 'plan',
}

export const itemsTypeOptions = [
  { label: '需求', value: ItemsTypeEnums.NEEDS },
  { label: '任务', value: ItemsTypeEnums.TASK },
  { label: '缺陷', value: ItemsTypeEnums.BUG },
  { label: '子项', value: ItemsTypeEnums.SUB_ITEM },
];

export const itemsNoChidTypeOptions = [
  { label: '需求', value: ItemsTypeEnums.NEEDS },
  { label: '任务', value: ItemsTypeEnums.TASK },
  { label: '缺陷', value: ItemsTypeEnums.BUG },
];

export const itemsAllNoChidTypeOptions = [
  { label: '全部', value: ItemsTypeEnums.ALL },
  { label: '需求', value: ItemsTypeEnums.NEEDS },
  { label: '任务', value: ItemsTypeEnums.TASK },
  { label: '缺陷', value: ItemsTypeEnums.BUG },
];

export function getItemsTypeValue(text) {
  const obj = itemsTypeOptions.find((item) => item.value === text);
  if (obj) return obj.label;
  return '';
}
