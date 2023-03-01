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

import { FormSchema } from '/@/components/Table';
export const formSchema: FormSchema[] = [
  {
    field: 'fielda',
    component: 'Input',
    label: '变量名',
    colProps: {
      span: 8,
    },
    required: true,
  },
  {
    field: 'fieldb',
    component: 'Input',
    label: '变量值',
    colProps: {
      span: 8,
    },
    required: true,
  },
  {
    field: '0',
    component: 'Input',
    label: ' ',
    colProps: {
      span: 8,
    },
    slot: 'add',
  },
];
