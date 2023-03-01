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
import { Icon } from '/@/components/Icon';
import { Tag } from 'ant-design-vue';
import { Popover } from 'ant-design-vue';
import Stage from '/@@/views/common/Stage.vue';
import { stageStatus } from '/@@/utils/lib/util';

export const columns: BasicColumn[] = [
  {
    title: '状态/触发时间',
    dataIndex: 'status',
    width: 180,
    align: 'left',
    customRender: ({ text, record }) => {
      const obj = stageStatus[text];
      return (
        <div class="flex flex-col">
          <div>
            <Tag color={obj.color} style={{ 'border-radius': '4px' }}>
              {obj.name}
            </Tag>
          </div>
          <div class="pt-2">{record.startTime}</div>
        </div>
      );
    },
  },
  {
    title: '提交分支',
    dataIndex: 'commit',
    align: 'left',
    width: 150,
    customRender: ({ record }) => {
      return (
        <div>
          <div>
            <Icon icon="ant-design:branches-outlined" class="!text-gray-400" />
            <span class="pl-1" style="font-size: 14px;font-weight: 500;color: #000000;">
              {record.gitlabRef}
            </span>
          </div>
          <Popover content={<div class="text-white">{record.sha}</div>} color="#08153a">
            <span class="cursor-pointer">
              <Icon icon="ant-design:node-expand-outlined" class="!text-gray-400" />
              <span class="pl-1 text-blue-600">
                {record.sha ? record.sha.substring(0, 8) : '-'}
              </span>
            </span>
          </Popover>
        </div>
      );
    },
  },
  {
    title: '触发者',
    width: 150,
    dataIndex: 'commiter',
    align: 'center',
  },
  {
    title: '更新说明',
    dataIndex: 'commitContent',
    align: 'left',
    customRender: ({ text }) => {
      return <div>{text}</div>;
    },
  },
  {
    title: '阶段',
    dataIndex: 'stages',
    align: 'left',
    helpMessage: '持续集成的阶段根据应用服务所选择的服务模板进行定义',
    customRender: ({ text }) => <Stage stages={text} />,
  },
  {
    title: '时长',
    dataIndex: 'duration',
    width: 120,
    align: 'left',
    customRender: ({ text }) => {
      let t = '-';
      if (text) {
        if (text > 60) {
          const value = Math.floor(text / 60);
          t = value + '分 ' + (text - value * 60) + '秒';
        } else {
          return text + '秒';
        }
      }

      return t;
    },
  },
];
