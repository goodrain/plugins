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

import { TreeItem } from '/@/components/Tree/index';

export const treeData: TreeItem[] = [
  {
    title: 'parent ',
    key: '0-0',
    children: [
      { title: 'leaf', key: '0-0-0' },
      {
        title: 'leaf',
        key: '0-0-1',
        children: [
          { title: 'leaf', key: '0-0-0-0', children: [{ title: 'leaf', key: '0-0-0-0-1' }] },
          { title: 'leaf', key: '0-0-0-1' },
        ],
      },
    ],
  },
  {
    title: 'parent 2',
    key: '1-1',
    children: [
      { title: 'leaf', key: '1-1-0' },
      { title: 'leaf', key: '1-1-1' },
    ],
  },
  {
    title: 'parent 3',
    key: '2-2',
    children: [
      { title: 'leaf', key: '2-2-0' },
      { title: 'leaf', key: '2-2-1' },
    ],
  },
];

export const treeData2: any[] = [
  {
    name: 'parent ',
    id: '0-0',

    children: [
      { name: 'leaf', id: '0-0-0' },
      {
        name: 'leaf',
        id: '0-0-1',

        children: [
          {
            name: 'leaf',

            id: '0-0-0-0',
            children: [{ name: 'leaf', id: '0-0-0-0-1' }],
          },
          { name: 'leaf', id: '0-0-0-1' },
        ],
      },
    ],
  },
  {
    name: 'parent 2',
    id: '1-1',

    children: [
      { name: 'leaf', id: '1-1-0' },
      { name: 'leaf', id: '1-1-1' },
    ],
  },
  {
    name: 'parent 3',
    id: '2-2',

    children: [
      { name: 'leaf', id: '2-2-0' },
      { name: 'leaf', id: '2-2-1' },
    ],
  },
];

export const treeData3: any[] = [
  {
    name: 'parent ',
    key: '0-0',
    children: [
      { name: 'leaf', key: '0-0-0' },
      {
        name: 'leaf',
        key: '0-0-1',
        children: [
          {
            name: 'leaf',
            key: '0-0-0-0',
            children: [{ name: 'leaf', key: '0-0-0-0-1' }],
          },
          { name: 'leaf', key: '0-0-0-1' },
        ],
      },
    ],
  },
  {
    name: 'parent 2',
    key: '1-1',

    children: [
      { name: 'leaf', key: '1-1-0' },
      { name: 'leaf', key: '1-1-1' },
    ],
  },
  {
    name: 'parent 3',
    key: '2-2',

    children: [
      { name: 'leaf', key: '2-2-0' },
      { name: 'leaf', key: '2-2-1' },
    ],
  },
];
