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

import { BasicColumn } from '/@/components/Table/src/types/table';

import { Badge } from 'ant-design-vue';

export const refundTimeTableSchema: BasicColumn[] = [
  {
    title: '时间',
    width: 150,
    dataIndex: 't1',
  },
  {
    title: '当前进度',
    width: 150,
    dataIndex: 't2',
  },
  {
    title: '状态',
    width: 150,
    dataIndex: 't3',
    customRender: ({ record }) => {
      return <Badge status="success" text={record.t3} />;
    },
  },
  {
    title: '操作员ID	',
    width: 150,
    dataIndex: 't4',
  },
  {
    title: '耗时',
    width: 150,
    dataIndex: 't5',
  },
];

export const refundTimeTableData: any[] = [
  {
    t1: '2017-10-01 14:10',
    t2: '联系客户',
    t3: '进行中',
    t4: '取货员 ID1234',
    t5: '5mins',
  },
  {
    t1: '2017-10-01 14:10',
    t2: '取货员出发',
    t3: '成功',
    t4: '取货员 ID1234',
    t5: '5mins',
  },
  {
    t1: '2017-10-01 14:10',
    t2: '取货员接单',
    t3: '成功',
    t4: '系统',
    t5: '5mins',
  },
  {
    t1: '2017-10-01 14:10',
    t2: '申请审批通过',
    t3: '成功',
    t4: '用户',
    t5: '1h',
  },
];
