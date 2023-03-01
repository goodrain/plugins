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

// table 分页选择时，第一页选择后，还要选择第二页，也能支持

import { ref } from 'vue';
export function useMultipleChoice() {
  const selectedRowKeys: any = ref([]);
  const selectList = ref<string[]>([]); // 当前列表

  function setSelectRowKeys(rows) {
    if (rows.length === 0 && selectedRowKeys.value.length === 0) {
      return;
    } else if (rows.length === selectedRowKeys.value.length) {
      let isEqually = true;
      for (const key of rows) {
        if (!selectedRowKeys.value.includes(key)) {
          isEqually = false;
          break;
        }
      }
      if (isEqually) return;
    }
    const otherSelectKeys = selectedRowKeys.value.filter((key: string) => {
      return !selectList.value.includes(key);
    });
    selectedRowKeys.value = [...rows, ...otherSelectKeys];
  }

  // 每次切换列表时，需更新当前列表
  function setCurrentList(arr: any) {
    selectList.value = arr.map((item) => item.id);
  }
  return {
    selectedRowKeys,
    setCurrentList,
    setSelectRowKeys,
  };
}
