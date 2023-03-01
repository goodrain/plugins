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
import { cloneDeep } from 'lodash-es';
import { defineStore } from 'pinia';
import { useAppServiceStore } from './appServiceStore';
import { store } from '/@/store';
import { getAuthCache, setAuthCache } from '/@/utils/auth';
//import { projectList, recent } from '/@@/api/projectApi';
import { getTeamList } from '/@@/api/projectSetApi';
import { CURRENT_PROJECT } from '/@@/enums/cacheEnum';

interface ProjectState {
  loading: boolean;
  current: Project | any;
  list: any;
  recentlyeList: any[]; // 最近访问的项目
  listSet: any; // 项目集
}

export const useProjectStore = defineStore({
  id: 'project-info',
  state: (): ProjectState => ({
    loading: false,
    current: {},
    list: {},
    recentlyeList: [], // 最近访问的项目
    listSet: {}, // 项目集
  }),
  getters: {
    getLoading(): boolean {
      return this.loading;
    },
    getCurrent(): any {
      // return this.current;
      return this.current.id ? this.current : this.getTeamInfo;
    },
    getList(): Project[] {
      return this.list;
    },
    getRecentlyeList(): Project[] {
      return this.recentlyeList;
    },
    getProjectSet(): any {
      return this.listSet as any;
    },
    getTeamInfo(): any {
      let team: any = {};
      if (localStorage.getItem('TEAMID') && localStorage.getItem('TEAMNAME')) {
        team.id = localStorage.getItem('TEAMID');
        team.teamName = localStorage.getItem('TEAMNAME');
        localStorage.removeItem('TEAMID');
        localStorage.removeItem('TEAMNAME');
      }
      return team;
    },
  },
  actions: {
    setLoading(bool: boolean) {
      this.loading = bool;
    },
    setData() {
      this.getTeamList();
    },

    // 获取团队数据
    getTeamList() {
      this.setLoading(true);
      return new Promise((reslove, reject) => {
        getTeamList()
          .then((res: any) => {
            if (res?.length > 0) {
              res = res.map((item) => {
                item.id = item?.teamId;
                return item;
              });
            }
            this.list = res;
            reslove(res);
          })
          .catch(reject)
          .finally(() => {
            this.setLoading(false);
          });
      });
    },

    // 获取项目集列表
    findProjectSetList(params?: any): Promise<[]> {
      this.setLoading(true);
      return new Promise((reslove, reject) => {
        this.setLoading(false);
        reslove([]);
      });
    },
    // 获取项目列表
    findProjectList(params?: any): Promise<[]> {
      this.setLoading(true);
      return new Promise((reslove, reject) => {
        this.setLoading(false);
        reslove([]);
      });
    },
    // 新增最近访问的项目
    addRecentlye(item: Project) {
      const project: any = cloneDeep(item);
      delete project.parent;
      this.current = project;
      setAuthCache(CURRENT_PROJECT, project);
      // 更新应用服务列表
      const appServiceStore = useAppServiceStore();
      appServiceStore.setServiceList(true);
    },
  },
});

// Need to be used outside the setup
export function useProjectStoreWithOut() {
  return useProjectStore(store);
}
