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

import { BasicColumn } from '/@/components/Table';
export const columns: BasicColumn[] = [
  {
    title: '镜像版本',
    dataIndex: 'appServiceVersion',
    align: 'left',
  },
  {
    title: '提交信息',
    dataIndex: 'commitMsg',
    align: 'left',
  },
  {
    title: '提交时间',
    dataIndex: 'commitTime',
    align: 'left',
    width: 200,
  },
  {
    title: '提交人',
    dataIndex: 'committer',
    align: 'left',
    width: 150,
  },
];
