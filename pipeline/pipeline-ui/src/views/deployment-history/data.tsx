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
import { validPhone } from '/@/utils/lib/validate';
import { Tag } from 'ant-design-vue';
export const columns: BasicColumn[] = [
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    align: 'left',
    customRender({ text }) {
      const obj = {
        success: {
          color: 'success',
          text: '成功',
        },
        running: {
          color: 'warning',
          text: '部署中',
        },
        failed: {
          color: 'error',
          text: '失败',
        },
      }[text];
      return <Tag color={obj.color}>{obj.text}</Tag>;
    },
  },
  {
    title: '镜像名称',
    dataIndex: 'appServiceVersion',
    align: 'left',
  },
  {
    title: '开始部署时间',
    dataIndex: 'createTime',
    width: 200,
    align: 'left',
  },
  {
    title: '执行人',
    dataIndex: 'createBy',
    align: 'left',
  },
  {
    title: '说明',
    dataIndex: 'description',
    align: 'left',
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'username',
    label: '账户名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'realName',
    label: '显示名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'password',
    label: '密码',
    component: 'Input',
    rules: [
      {
        min: 6,
        required: true,
        whitespace: true,
        trigger: ['change', 'blur'],
        message: '密码不能小于6位！',
      },
    ],
  },
  {
    field: 'email',
    label: '邮箱',
    component: 'Input',
    required: true,
    rules: [{ type: 'email', whitespace: true, trigger: 'blur', message: '请输入正确的邮箱！' }],
  },
  {
    field: 'mobile',
    label: '手机',
    component: 'Input',
    rules: [
      {
        required: true,
        validator: function (_, value) {
          if (!value) {
            return Promise.reject('请输入手机号');
          } else if (!validPhone(value)) {
            return Promise.reject('请输入正确的手机号');
          } else {
            return Promise.resolve();
          }
        },
      },
    ],
  },
];
