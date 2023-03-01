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

interface User {
  account: string;
  id: string;
  realname: string;
}
interface AppService {
  appId: null | string;
  autoBuild: boolean;
  autoDeploy: null | boolean;
  code: string;
  componentCode: null;
  createBy: string;
  createTime: string;
  delFlag: boolean;
  gitlabCodeUrl: string;
  gitlabProjectId: string;
  id: string;
  name: string;
  productType: null;
  projectId: string;
  projectSetCode: string;
  projectSetName: string;
  regionCodes: null;
  serviceType: string;
  status: string;
  subAppServiceDeployEnvVOS: null;
  teamCode: string;
  type: string;
  updateBy: string;
  update_time: string;
  version: 1;
}
interface Project {
  code: string;
  id: string;
  name: string;
  pm: string;
  status: 'wait' | 'doing' | 'enable';
  teamCode: string;
  userCount: number;
  users: User[];
  appServices: AppService[];
}
