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

import { reactive, ref } from 'vue';
import { getPipelineTempDetail, getPipelineDetail } from '/@@/api/pipelineApi';

export const loading = ref(false);
interface FlowType {
  id: null | string;
  name: null | string;
  description: string;
  stages: any[];
  variables: any[];
}
function defaultFlow(): FlowType {
  return {
    id: null,
    name: '无标题',
    description: '',
    stages: [],
    variables: [],
  };
}
export const flow: FlowType = reactive(defaultFlow());

export function setLoading(b: boolean) {
  loading.value = b;
}
export function clearFlow() {
  const defaultValue = defaultFlow();
  for (const [key, value] of Object.entries(defaultValue)) {
    flow[key] = value;
  }
}
export function getPipelineInfo(query: any) {
  if (query.id) {
    loading.value = true;
    getPipelineTempDetail(query.id)
      .then((res: any) => {
        flow.id = res.id;
        flow.name = res.name || flow.name;
        flow.description = res.description;
        flow.stages = res.stages.map((item, index) => {
          return {
            index,
            ...item,
          };
        });
        flow.variables = res.variables;
      })
      .finally(() => {
        loading.value = false;
      });
  } else if (query.listId) {
    loading.value = true;
    getPipelineDetail(query.listId)
      .then((res: any) => {
        flow.id = res.id;
        flow.name = res.name;
        flow.description = res.description;
        flow.stages = res.stages.map((item, index) => {
          return {
            index,
            ...item,
          };
        });
        flow.variables = res.variables;
      })
      .finally(() => {
        loading.value = false;
      });
  }
}
// 更新阶段
export function updateStages() {
  flow.stages = flow.stages.map((item, index) => {
    return {
      ...item,
      index,
    };
  });
}
