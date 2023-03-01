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
export const columns: BasicColumn[] = [
  {
    title: '#',
    dataIndex: '',
    width: 80,
    align: 'left',
    customRender({ record }) {
      if (record.branchName === 'master')
        return (
          <div
            style={`width: 30px;
      height: 30px;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #eef2ff;
      color: #315aea;
      border-radius: 100%;
      font-size: 14px;`}
          >
            M
          </div>
        );
    },
  },
  {
    title: '分支名称',
    width: 120,
    dataIndex: 'branchName',
    align: 'left',
  },

  {
    title: '最近提交信息',
    dataIndex: 'commitMsg',
    align: 'left',
    customRender({ text }) {
      return <div>{text}</div>;
    },
  },
  {
    title: '提交时间',
    dataIndex: 'commitDate',
    width: 150,
    align: 'left',
  },
  {
    title: '最近更新人',
    dataIndex: 'commitUserName',
    width: 180,
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
