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

import { MockMethod } from 'vite-plugin-mock';
import { resultSuccess } from '../_util';

const demoTreeList = (keyword) => {
  const result = {
    list: [] as Recordable[],
  };
  for (let index = 0; index < 5; index++) {
    const children: Recordable[] = [];
    for (let j = 0; j < 3; j++) {
      children.push({
        title: `${keyword ?? ''}选项${index}-${j}`,
        value: `${index}-${j}`,
        key: `${index}-${j}`,
      });
    }
    result.list.push({
      title: `${keyword ?? ''}选项${index}`,
      value: `${index}`,
      key: `${index}`,
      children,
    });
  }
  return result;
};

export default [
  {
    url: '/basic-api/tree/getDemoOptions',
    timeout: 1000,
    method: 'get',
    response: ({ query }) => {
      const { keyword } = query;
      console.log(keyword);
      return resultSuccess(demoTreeList(keyword));
    },
  },
] as MockMethod[];
