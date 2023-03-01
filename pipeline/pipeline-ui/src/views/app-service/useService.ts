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

import { ref } from 'vue';
import { template as getTemp } from '/@@/api/applicationServiceApi';

export const template = ref([]);

export function getTemplate() {
  if (template.value.length) return Promise.resolve(template.value);
  return new Promise((resolve, reject) => {
    getTemp()
      .then((res) => {
        template.value = res;
        resolve(res);
      })
      .catch(reject);
  });
}
