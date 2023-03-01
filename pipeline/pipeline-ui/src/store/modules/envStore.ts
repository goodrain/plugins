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

// 项目列表
import { defineStore } from 'pinia';
import { store } from '/@/store';
import { useProjectStore } from './projectStore';
import { getRegions } from '/@@/api/envApi';

interface EnvState {
  loading: boolean;
  list: any;
  current: any;
  regionCode: string; // 很多接口调用时都需要这个做为传参，所以直接放在外面了
}

export const useEnvStore = defineStore({
  id: 'env-info',
  state: (): EnvState => ({
    loading: false,
    list: [],
    current: {},
    regionCode: '',
  }),
  getters: {
    getLoading(): boolean {
      return this.loading;
    },
    getCurrent(): any {
      return this.current;
    },
    getList(): any[] {
      return this.list;
    },
  },
  actions: {
    setLoading(bool: boolean) {
      this.loading = bool;
    },
    setCurrent(payload: any) {
      this.current = payload || {};
      this.regionCode = payload?.regionName;
    },
    queryName(code) {
      const obj = this.list.find((item) => item?.regionName === code);
      if (obj) {
        return obj.regionAlias;
      }
      return null;
    },
    // 获取列表
    setList(update = false): Promise<[]> {
      const projectStore = useProjectStore();
      if ((update || !this.current?.id) && projectStore.getCurrent?.id) {
        return new Promise((reslove, reject) => {
          // 获取租户列表
          getRegions(projectStore.getCurrent.id)
            .then((res: any) => {
              reslove(res);
              this.list = res;
              this.current = res[0];
            })
            .catch(reject)
            .finally(() => {
              this.setLoading(false);
            });
        });
      }
      return Promise.resolve(this.list);
    },
  },
});

// Need to be used outside the setup
export function useEnvStoreWithOut() {
  return useEnvStore(store);
}
