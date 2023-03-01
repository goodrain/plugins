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

// Used to configure the general configuration of some components without modifying the components
import { merge } from 'lodash-es'; // [修改]-新增
import settings from '/@/config/settings'; // [修改]-新增

import type { SorterResult } from '../components/Table';

// [修改]-新增merge
export default merge(
  {
    // basic-table setting
    table: {
      // Form interface request general configuration
      // support xxx.xxx.xxx
      fetchSetting: {
        // The field name of the current page passed to the background
        pageField: 'page',
        // The number field name of each page displayed in the background
        sizeField: 'pageSize',
        // Field name of the form data returned by the interface
        listField: 'items',
        // Total number of tables returned by the interface field name
        totalField: 'total',
      },
      // Number of pages that can be selected
      pageSizeOptions: ['10', '50', '80', '100'],
      // Default display quantity on one page
      defaultPageSize: 10,
      // Default Size
      defaultSize: 'middle',
      // Custom general sort function
      defaultSortFn: (sortInfo: SorterResult) => {
        const { field, order } = sortInfo;
        if (field && order) {
          return {
            // The sort field passed to the backend you
            field,
            // Sorting method passed to the background asc/desc
            order,
          };
        } else {
          return {};
        }
      },
      // Custom general filter function
      defaultFilterFn: (data: Partial<Recordable<string[]>>) => {
        return data;
      },
    },
    // scrollbar setting
    scrollbar: {
      // Whether to use native scroll bar
      // After opening, the menu, modal, drawer will change the pop-up scroll bar to native
      native: false,
    },
  },
  settings.componentSetting, // [修改]-新增setting
);
