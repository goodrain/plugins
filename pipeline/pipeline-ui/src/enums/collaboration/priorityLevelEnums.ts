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

// 优先级
export enum PriorityLevelEnums {
  LOW = 'low',
  MIDDLE = 'middle',
  HIGH = 'high',
  URGENT = 'urgent',
}

export const priorityLevelOptions = [
  {
    label: '紧急',
    value: PriorityLevelEnums.URGENT,
    icon: 'material-symbols:arrow-circle-up-rounded',
    color: '#f24c3d',
  },
  {
    label: '高',
    value: PriorityLevelEnums.HIGH,
    icon: 'mdi:arrow-up-drop-circle',
    color: '#ff9940',
  },
  {
    label: '中',
    value: PriorityLevelEnums.MIDDLE,
    icon: 'material-symbols:do-not-disturb-on-rounded',
    color: '#ffcc33',
  },
  {
    label: '低',
    icon: 'material-symbols:arrow-drop-down-circle',
    value: PriorityLevelEnums.LOW,
    color: '#73cc33',
  },
];
