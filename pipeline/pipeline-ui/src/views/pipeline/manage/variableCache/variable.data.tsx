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
    title: '变量名',
    dataIndex: 'itemKey',
    align: 'left',
    editRow: true,
    editRule: true,
  },

  {
    title: '默认值',
    dataIndex: 'value',
    align: 'left',
    editRow: true,
    editRule: true,
    editValueMap() {
      return '******';
    },
  },
  {
    title: '说明',
    dataIndex: 'desc',
    align: 'left',
    editRow: true,
    editRule: false,
  },
];
