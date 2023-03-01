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
import { Switch } from 'ant-design-vue';
export const columns: BasicColumn[] = [
  {
    title: '缓存目录',
    dataIndex: 'name',
    align: 'left',
  },
  {
    title: '描述',
    dataIndex: 'dec',
    align: 'left',
  },
  {
    title: '是否开启',
    dataIndex: 'description',
    align: 'left',
    width: 100,
    customRender() {
      return <Switch disabled={true} />;
    },
  },
];
