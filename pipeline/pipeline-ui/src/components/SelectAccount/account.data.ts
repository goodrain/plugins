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

import { BasicColumn, FormSchema } from '/@/components/Table';
export const columns: BasicColumn[] = [
  {
    title: '账户名称',
    dataIndex: 'username',
    align: 'left',
    slots: { customRender: 'username' },
  },
  {
    title: '显示名称',
    dataIndex: 'realName',
    align: 'left',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    align: 'left',
  },
  {
    title: '手机号',
    dataIndex: 'mobile',
    align: 'left',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'username',
    label: '账户名称',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'realName',
    label: '显示名称	',
    component: 'Input',
    colProps: { span: 6 },
  },
];
