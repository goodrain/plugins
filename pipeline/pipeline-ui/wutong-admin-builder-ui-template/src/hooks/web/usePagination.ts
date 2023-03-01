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

import type { Ref } from 'vue';
import { ref, unref, computed } from 'vue';

function pagination<T = any>(list: T[], pageNo: number, pageSize: number): T[] {
  const offset = (pageNo - 1) * Number(pageSize);
  const ret =
    offset + Number(pageSize) >= list.length
      ? list.slice(offset, list.length)
      : list.slice(offset, offset + Number(pageSize));
  return ret;
}

export function usePagination<T = any>(list: Ref<T[]>, pageSize: number) {
  const currentPage = ref(1);
  const pageSizeRef = ref(pageSize);

  const getPaginationList = computed(() => {
    return pagination(unref(list), unref(currentPage), unref(pageSizeRef));
  });

  const getTotal = computed(() => {
    return unref(list).length;
  });

  function setCurrentPage(page: number) {
    currentPage.value = page;
  }

  function setPageSize(pageSize: number) {
    pageSizeRef.value = pageSize;
  }

  return { setCurrentPage, getTotal, setPageSize, getPaginationList };
}
