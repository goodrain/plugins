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
import { getServiceList, getCloneUrl, getSubService } from '/@@/api/appServiceApi';
import { useProjectStore } from './projectStore';

interface AppState {
  loading: boolean;
  // 应用服务列表
  list: any;
  // 当前选择的应用服务
  currentService: any;
  // 克隆仓库
  cloneUrl: any;
}

export const useAppServiceStore = defineStore({
  id: 'app-service-info',
  state: (): AppState => ({
    loading: false,
    // 应用服务列表
    list: [],
    currentService: {},
    // 克隆仓库
    cloneUrl: {},
  }),
  getters: {
    getLoading(): boolean {
      return this.loading;
    },
    getCurrentService(): any {
      return this.currentService;
    },
    getCloneUrl(): any[] {
      return this.cloneUrl;
    },
    getServiceList(): any[] {
      return this.list;
    },
  },
  actions: {
    setLoading(bool: boolean) {
      this.loading = bool;
    },
    setServiceCurrent(payload) {
      if (typeof payload === 'string') {
        const service = this.list.find((item: any) => item.id === payload);
        this.currentService = service || {};
      } else {
        this.currentService = payload || {};
      }
      if (this.currentService.id) {
        this.setCloneList();
      }
    },
    setCloneUrl(payload) {
      this.cloneUrl = payload;
    },
    // 获取列表 每次选择项目后都会调用一下
    setServiceList(update?: boolean): Promise<[]> {
      if (this.list.length && !update) return Promise.resolve(this.list);
      this.setLoading(true);
      const projectStore = useProjectStore();
      return new Promise((resolve, reject) => {
        // 获取服务列表
        getServiceList(projectStore.getCurrent?.id)
          .then((res: any) => {
            this.list = res;
            this.setServiceCurrent(res[0]);
          })
          .catch(reject)
          .finally(() => {
            this.setLoading(false);
          });
      });
    },
    // 获取clone地址
    setCloneList(): Promise<[]> {
      this.setLoading(true);
      return new Promise((resolve, reject) => {
        // 获取租户列表
        getCloneUrl(this.currentService.id)
          .then((res: any) => {
            this.cloneUrl = res;
            resolve(res);
          })
          .catch(reject)
          .finally(() => {
            this.setLoading(false);
          });
      });
    },
  },
});

// Need to be used outside the setup
export function useAppServiceStoreWithOut() {
  return useAppServiceStore(store);
}
