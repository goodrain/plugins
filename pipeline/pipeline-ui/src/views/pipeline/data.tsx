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
import { FormSchema } from '/@/components/Table';
import Stage from '/@@/views/common/Stage.vue';

export const columns: BasicColumn[] = [
  {
    title: '流水线名称',
    dataIndex: 'name',
    width: 240,
    align: 'left',
  },
  {
    title: '关联应用服务',
    dataIndex: 'appServiceName',
    width: 150,
    align: 'left',
  },
  {
    title: '最近运行开始时间',
    dataIndex: 'startTime',
    width: 220,
    align: 'left',
  },
  {
    title: '最新运行阶段',
    dataIndex: 'stages',
    align: 'left',
    customRender: ({ text }) => <Stage stages={text} />,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入流水线名称',
    },
  },
];
