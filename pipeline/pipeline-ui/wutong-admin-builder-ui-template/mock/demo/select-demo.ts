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

const demoList = (keyword, count = 20) => {
  const result = {
    list: [] as any[],
  };
  for (let index = 0; index < count; index++) {
    result.list.push({
      name: `${keyword ?? ''}选项${index}`,
      id: `${index}`,
    });
  }
  return result;
};

export default [
  {
    url: '/basic-api/select/getDemoOptions',
    timeout: 1000,
    method: 'get',
    response: ({ query }) => {
      const { keyword, count } = query;
      console.log(keyword);
      return resultSuccess(demoList(keyword, count));
    },
  },
] as MockMethod[];
