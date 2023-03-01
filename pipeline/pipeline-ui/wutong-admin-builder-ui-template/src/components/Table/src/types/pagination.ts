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

import Pagination from 'ant-design-vue/lib/pagination';
import { VNodeChild } from 'vue';

interface PaginationRenderProps {
  page: number;
  type: 'page' | 'prev' | 'next';
  originalElement: any;
}

type PaginationPositon =
  | 'topLeft'
  | 'topCenter'
  | 'topRight'
  | 'bottomLeft'
  | 'bottomCenter'
  | 'bottomRight';

export declare class PaginationConfig extends Pagination {
  position?: PaginationPositon[];
}

export interface PaginationProps {
  /**
   * total number of data items
   * @default 0
   * @type number
   */
  total?: number;

  /**
   * default initial page number
   * @default 1
   * @type number
   */
  defaultCurrent?: number;

  /**
   * current page number
   * @type number
   */
  current?: number;

  /**
   * default number of data items per page
   * @default 10
   * @type number
   */
  defaultPageSize?: number;

  /**
   * number of data items per page
   * @type number
   */
  pageSize?: number;

  /**
   * Whether to hide pager on single page
   * @default false
   * @type boolean
   */
  hideOnSinglePage?: boolean;

  /**
   * determine whether pageSize can be changed
   * @default false
   * @type boolean
   */
  showSizeChanger?: boolean;

  /**
   * specify the sizeChanger options
   * @default ['10', '20', '30', '40']
   * @type string[]
   */
  pageSizeOptions?: string[];

  /**
   * determine whether you can jump to pages directly
   * @default false
   * @type boolean
   */
  showQuickJumper?: boolean | object;

  /**
   * to display the total number and range
   * @type Function
   */
  showTotal?: (total: number, range: [number, number]) => any;

  /**
   * specify the size of Pagination, can be set to small
   * @default ''
   * @type string
   */
  size?: string;

  /**
   * whether to setting simple mode
   * @type boolean
   */
  simple?: boolean;

  /**
   * to customize item innerHTML
   * @type Function
   */
  itemRender?: (props: PaginationRenderProps) => VNodeChild | JSX.Element;

  /**
   * specify the position of Pagination
   * @default ['bottomRight']
   * @type string[]
   */
  position?: PaginationPositon[];
}
