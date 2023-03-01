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

import { Tag } from 'ant-design-vue';
import { BasicColumn } from '/@/components/Table/index';
import { ErrorTypeEnum } from '/@/enums/exceptionEnum';
import { useI18n } from '/@/hooks/web/useI18n';

const { t } = useI18n();

export function getColumns(): BasicColumn[] {
  return [
    {
      dataIndex: 'type',
      title: t('sys.errorLog.tableColumnType'),
      width: 80,
      customRender: ({ text }) => {
        const color =
          text === ErrorTypeEnum.VUE
            ? 'green'
            : text === ErrorTypeEnum.RESOURCE
            ? 'cyan'
            : text === ErrorTypeEnum.PROMISE
            ? 'blue'
            : ErrorTypeEnum.AJAX
            ? 'red'
            : 'purple';
        return <Tag color={color}>{() => text}</Tag>;
      },
    },
    {
      dataIndex: 'url',
      title: 'URL',
      width: 200,
    },
    {
      dataIndex: 'time',
      title: t('sys.errorLog.tableColumnDate'),
      width: 160,
    },
    {
      dataIndex: 'file',
      title: t('sys.errorLog.tableColumnFile'),
      width: 200,
    },
    {
      dataIndex: 'name',
      title: 'Name',
      width: 200,
    },
    {
      dataIndex: 'message',
      title: t('sys.errorLog.tableColumnMsg'),
      width: 300,
    },
    {
      dataIndex: 'stack',
      title: t('sys.errorLog.tableColumnStackMsg'),
    },
  ];
}

export function getDescSchema(): any {
  return getColumns().map((column) => {
    return {
      field: column.dataIndex!,
      label: column.title,
    };
  });
}
