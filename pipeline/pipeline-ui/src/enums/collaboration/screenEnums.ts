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

export enum ScreenEnums {
  PRIORITY = 'priority',
  CREATE_BY = 'createBy',
  STORY_POINT = 'storyPoint',
  END_TIME = 'endTime',
  CREATE_TIME = 'createTime',
  START_TIME = 'startTime',
  MODULE = 'module',
  TAG = 'tag',
  SEVERITY = 'severity',
  SPRINT = 'sprint',
  REOPEN = 'reopen',
}
const multiple = '多选';
const date = '日期选择';
const single = '单选';
export const screenOptions = [
  { label: '优先级', value: ScreenEnums.PRIORITY, attribute: multiple },
  { label: '创建人', value: ScreenEnums.CREATE_BY, attribute: multiple },
  { label: '故事点', value: ScreenEnums.STORY_POINT, attribute: multiple },
  { label: '截止日期', value: ScreenEnums.END_TIME, attribute: date },
  { label: '创建日期', value: ScreenEnums.CREATE_TIME, attribute: date },
  { label: '开始日期', value: ScreenEnums.START_TIME, attribute: date },
  { label: '模块', value: ScreenEnums.MODULE, attribute: multiple },
  { label: '标签', value: ScreenEnums.TAG, attribute: multiple },
  { label: '迭代', value: ScreenEnums.SPRINT, attribute: multiple },
  { label: '严重程度', value: ScreenEnums.SEVERITY, attribute: multiple },
  { label: '缺陷打开次数', value: ScreenEnums.REOPEN, attribute: single },
];
