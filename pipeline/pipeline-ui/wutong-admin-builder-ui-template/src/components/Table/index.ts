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

export { default as BasicTable } from './src/BasicTable.vue';
export { default as TableAction } from './src/components/TableAction.vue';
export { default as EditTableHeaderIcon } from './src/components/EditTableHeaderIcon.vue';
export { default as TableImg } from './src/components/TableImg.vue';

export * from './src/types/table';
export * from './src/types/pagination';
export * from './src/types/tableAction';
export { useTable } from './src/hooks/useTable';
export type { FormSchema, FormProps } from '/@/components/Form/src/types/form';
export type { EditRecordRow } from './src/components/editable';
