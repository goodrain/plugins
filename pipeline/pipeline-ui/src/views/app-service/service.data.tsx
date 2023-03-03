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
import { MavenEnum } from '/@@/enums/mavenEnum';

// 服务选项
export const serviceTypOptions = [
  // { label: '内置GitLab仓库', value: 'internal' },
  { label: '关联GitLab仓库', value: 'external' },
];

// 服务来源选项
export const serviceSourcesOptions = [
  { label: '源代码', value: 'origin' },
  // { label: 'jar包', value: 'jar' },
  // { label: 'war包', value: 'war' },
];

// 认证配置
export const authConfigOptions = [
  {
    title: '用户名与密码',
    content: '需至少输入该仓库内Maintainer角色的用户名和密码',
  },
  {
    title: '私有Token',
    content: '需至少使用Maintainer角色在GitLab中创建含有api权限的Access Token',
  },
].map((item, index) => ({ ...item, id: index, value: index }));

// 流水线类型
export const pipeLineTypeOptions = [
  { label: '自定义流水线', value: 'custom' },
  // { label: '内置', value: 'internal' },
];

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

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入应用服务名称/服务编码',
    },
  },
];

export const hubFormSchema: FormSchema[] = [
  {
    field: 'url',
    label: '仓库配置',
    component: 'Input',
    componentProps: {
      placeholder: '请输入gitlab仓库地址',
    },
    rules: [
      {
        required: true,
        whitespace: true,
        type: 'string',
        message: '请输入正确的仓库地址！',
      },
    ],
  },
  {
    field: 'username',
    label: '用户名',
    component: 'Input',
    // rules: [
    //   {
    //     required: true,
    //     whitespace: true,
    //     type: 'string',
    //     message: '用户名为必填项！',
    //   },
    // ],
  },
  {
    field: 'password',
    label: '密码',
    component: 'InputPassword',
    // rules: [
    //   {
    //     required: true,
    //     whitespace: true,
    //     type: 'string',
    //     message: '用户名为必填项！',
    //   },
    // ],
  },
  {
    field: 'token',
    label: '私有token',
    component: 'Input',
    // rules: [
    //   {
    //     required: true,
    //     whitespace: true,
    //     type: 'string',
    //     message: 'token为必填项！',
    //   },
    // ],
  },
];

export const formSchema = ({ productTypeChange }): FormSchema[] => [
  {
    field: 'code',
    label: '服务编码',
    component: 'Input',
    required: true,
    componentProps: {
      maxlength: 32,
    },
    itemProps: {
      extra:
        '应用该服务的自定义编码，GitLab仓库的地址将会使用服务编码作为仓库地址的一段路径，同时此编码在团队下唯一且不可修改。',
    },
    rules: [
      {
        required: true,
        whitespace: true,
        type: 'string',
        message: '服务编码为必填项！',
      },
      {
        validator: (_, value) => {
          if (value && value.trim()) {
            if (/[\u4e00-\u9fa5]/.test(value)) {
              return Promise.reject('服务编码不能是中文！');
            } else if (!/^[a-z][a-z0-9_-]*$/.test(value)) {
              return Promise.reject(
                '应用服务编码以小写字母开头，由小写字母数字以及下划线_和中划线-组成！',
              );
            }
          }
          return Promise.resolve();
        },
      },
    ],
  },
  {
    field: 'name',
    label: '服务名称',
    component: 'Input',
    componentProps: {
      maxlength: 32,
    },
    required: true,
    rules: [
      {
        required: true,
        whitespace: true,
        type: 'string',
        message: '服务名称为必填项！',
      },
    ],
  },
  {
    field: 'productType',
    label: '服务来源',
    component: 'RadioButtonGroup',
    defaultValue: serviceSourcesOptions[0].value,
    required: true,
    show: false,
    componentProps: {
      options: serviceSourcesOptions,
      onChange: (e) => {
        if (typeof e === 'string') {
          productTypeChange(e);
        }
      },
    },
  },
  {
    field: 'type',
    label: '多模板',
    component: 'Switch',
    itemProps: {
      //  extra: '您可以基于以下来源的应用服务模板创建一个新的应用服务',
    },
    componentProps: {
      placeholder: '请选择服务模板',
    },

    // ifShow: ({ values }) => {
    //   return values.productType === serviceSourcesOptions[0].value;
    // },
  },
  {
    field: 'serviceChildren',
    label: '子服务编码',
    component: 'Input',
    slot: 'serviceChildren',
    rules: [
      {
        required: true,
      },
      {
        validator: (_, value) => {
          const array = JSON.parse(value);
          if (value && array.length) {
            let isTrime = false;
            let isFiled = false; // 大小字母数字验证
            const arr = array.map((item) => {
              if (item.code === '' || item.code.trim() === '') {
                isTrime = true;
              }
              return item.code;
            });
            array.forEach((item) => {
              if (!/^[a-z][a-z0-9_-]*$/.test(item.code)) {
                isFiled = true;
              }
            });
            if (isTrime) {
              return Promise.reject('请输入子服务编码！');
            } else if (new Set(arr).size != arr.length) {
              return Promise.reject('子服务编码重复！');
            } else if (isFiled) {
              return Promise.reject(
                '子服务编码以小写字母开头，由小写字母数字以及下划线_和中划线-组成！',
              );
            }
          }
          return Promise.resolve();
        },
      },
    ],
    ifShow: ({ values }) => {
      return values.type === true && values.productType === serviceSourcesOptions[0].value;
    },
  },
  {
    field: 'serviceType',
    label: '代码仓库',
    component: 'RadioButtonGroup',
    defaultValue: serviceTypOptions[0].value,
    required: true,
    componentProps: {
      options: serviceTypOptions,
    },
    show: false,
  },
  {
    field: 'pipelineType',
    label: '流水线',
    component: 'RadioButtonGroup',
    defaultValue: pipeLineTypeOptions[0].value,
    required: true,
    ifShow: false,
    componentProps: {
      options: pipeLineTypeOptions,
    },
  },
  {
    field: 'pipelineId',
    label: '流水线',
    component: 'Select',
    slot: 'pipeline',
    required: true,
    ifShow: ({ values }) => {
      return values.pipelineType === pipeLineTypeOptions[0].value;
    },
  },
  {
    field: 'url',
    label: '仓库配置',
    component: 'Input',
    componentProps: {
      placeholder: '请输入gitlab仓库地址',
    },
    rules: [
      {
        required: true,
        whitespace: true,
        type: 'string',
        message: '请输入正确的仓库地址！',
      },
    ],
    ifShow: ({ values }) => {
      // 关联gitlab才可以显示，
      return values.serviceType === serviceTypOptions[0].value;
    },
  },
  {
    field: 'oauthConfig',
    label: '认证配置',
    component: 'RadioButtonGroup',
    defaultValue: authConfigOptions[0],
    slot: 'oauthConfig',
    required: true,
    ifShow: ({ values }) => {
      // 关联gitlab才可以显示，
      return values.serviceType === serviceTypOptions[0].value;
    },
  },
  {
    field: 'username',
    label: '用户名',
    component: 'Input',
    rules: [
      {
        required: true,
        whitespace: true,
        type: 'string',
        message: '用户名为必填项！',
      },
    ],
    ifShow: ({ values }) => {
      // 关联gitlab才可以显示，且用户名与密码选项
      return (
        values.serviceType === serviceTypOptions[0].value &&
        values.oauthConfig &&
        values.oauthConfig.id === 0
      );
    },
  },
  {
    field: 'password',
    label: '密码',
    component: 'InputPassword',
    rules: [
      {
        required: true,
        whitespace: true,
        type: 'string',
        message: '用户名为必填项！',
      },
    ],
    ifShow: ({ values }) => {
      // 关联gitlab才可以显示，且用户名与密码选项
      return (
        values.serviceType === serviceTypOptions[0].value &&
        values.oauthConfig &&
        values.oauthConfig.id === 0
      );
    },
  },
  {
    field: 'token',
    label: '私有token',
    component: 'Input',
    rules: [
      {
        required: true,
        whitespace: true,
        type: 'string',
        message: 'token为必填项！',
      },
    ],
    ifShow: ({ values }) => {
      // 关联gitlab才可以显示，且token选项
      return (
        values.serviceType === serviceTypOptions[0].value &&
        values.oauthConfig &&
        values.oauthConfig.id === 1
      );
    },
  },
  {
    field: 'test',
    label: '测试连通',
    component: 'Input',
    slot: 'test',
    ifShow: ({ values }) => {
      // 关联gitlab才可以显示，
      return values.serviceType === serviceTypOptions[0].value;
    },
  },
  {
    field: 'autoBuild',
    label: '自动构建',
    defaultValue: false,
    component: 'Switch',
    ifShow: false,
    itemProps: {
      extra: '开启时，表示每次提交代码时，会触发自动触发CI',
    },
  },
  {
    field: 'autoDeploy',
    label: '自动部署',
    defaultValue: false,
    component: 'Switch',
    ifShow: false,
    itemProps: {
      extra: '开启时，表示每次生成镜像时，会触发自动触发CD。第一次须进行手动部署方可开启自动部署',
    },
  },
  {
    field: 'regionCodes',
    label: '选择环境',
    component: 'CheckboxGroup',
    // slot: 'regionCodes',
    required: true,
    ifShow: ({ values }) => {
      //自动部署 && maven不能是子服务
      return values.autoDeploy === true && values.type !== true;
    },
  },
  {
    field: 'subAppServiceDeployEnvVOS',
    label: '选择自动部署的子服务',
    component: 'CheckboxGroup',
    slot: 'subAppService',
    ifShow: ({ values }) => {
      //自动部署 && maven是子服务
      return values.autoDeploy === true && values.type === true;
    },
  },
];
