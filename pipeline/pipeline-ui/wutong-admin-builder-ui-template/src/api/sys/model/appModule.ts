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

import { EntityBase } from '/#/entity';
import { UserIdEnum } from '/@/enums/userEnum';
export interface AppModule extends EntityBase {
  app_host: string;
  app_type_id: string;
  category: string;
  del_flag: false;
  extendattr_falg: null;
  extended_attributes: null;
  icon: null;
  id: string;
  inducer: string;
  internally_app: string;
  login_url: string;
  logout_type: string;
  logout_url: string;
  name: string;
  order_no: number;
  protocol_type: 'OAuth2.0' | string;
  redirect_uri: string;
  remark: string;
  resource_system_id: UserIdEnum.SYSTEM_ID | string;
  secret: string;
  status: 'ENABLE' | string;
  version: number;
  visible: 1 | 0 | number;
}
