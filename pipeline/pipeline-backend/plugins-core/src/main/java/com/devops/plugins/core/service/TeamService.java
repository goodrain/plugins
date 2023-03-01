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

package com.devops.plugins.core.service;

import com.devops.plugins.core.pojo.vo.resp.region.RegionResp;
import com.devops.plugins.core.pojo.vo.resp.team.TeamResp;

import java.util.List;

public interface TeamService {


    /**
     * 查询团队列表
     *
     * @return
     */
    List<TeamResp> list();


    /**
     * 获取团队绑定的集群列表
     *
     * @param teamId
     * @return
     */
    List<RegionResp> listRegions(String teamId);

}
