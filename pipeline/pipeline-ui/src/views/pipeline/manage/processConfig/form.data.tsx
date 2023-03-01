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

export const columns: BasicColumn[] = [
  {
    title: '应用服务编码',
    dataIndex: 'code',
    width: 250,
    align: 'left',
  },
  {
    title: '应用服务名称',
    dataIndex: 'name',
    width: 250,
    align: 'left',
  },
  {
    title: 'GitLab仓库地址',
    dataIndex: 'gitlabCodeUrl',
    align: 'left',
    customRender({ text }) {
      return (
        <a href={text} target="_blank">
          {text}
        </a>
      );
    },
  },
  {
    title: '创建人',
    dataIndex: 'createBy',
    width: 130,
    align: 'left',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
    align: 'left',
  },
  {
    title: '状态',
    helpMessage: '状态为关闭时，表示停用该服务，停用的应用服务将不支持部署',
    width: 80,
    dataIndex: 'status',
    align: 'center',
    fixed: 'right',
    slots: { customRender: 'status' },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'name',
    label: '阶段名称',
    component: 'Input',
    required: true,
    componentProps: {
      maxlength: 32,
    },
    helpComponentProps: {
      icon: 'ant-design:question-circle-outlined',
    },
    helpMessage: '该任务阶段的显示名称',
  },
  {
    field: 'code',
    label: '阶段编码',
    component: 'Input',
    required: true,
    componentProps: {
      maxlength: 32,
    },
    helpComponentProps: {
      icon: 'ant-design:question-circle-outlined',
    },
    helpMessage: '该任务阶段的编码',
  },
  {
    field: 'image',
    label: '镜像环境',
    component: 'Input',
    required: true,
    helpComponentProps: {
      icon: 'ant-design:question-circle-outlined',
    },
    helpMessage: '该任务阶段运行所需要的镜像环境',
  },
  /* {
    field: 'codeEditor',
    label: '脚本命令',
    component: 'InputTextArea',
    helpComponentProps: {
      icon: 'ant-design:question-circle-outlined',
    },
    helpMessage: '该任务阶段执行的脚本',
    slot: 'codeEditor',
  }, */
];

export const productSchema: FormSchema[] = [
  {
    field: 'time',
    label: '保存时间',
    component: 'Input',
    componentProps: {
      addonAfter: '秒',
    },
    helpComponentProps: {
      icon: 'ant-design:question-circle-outlined',
    },
    rules: [
      {
        pattern: /^[0-9]+$/,
        message: '时间格式错误！',
      },
    ],
    helpMessage: '该任务阶段制品在生成后的存放时间，默认30天',
  },
];
