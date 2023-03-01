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

package com.devops.plugins.core.service.impl;

import com.devops.plugins.core.clients.PaasClient;
import com.devops.plugins.core.pojo.rainbond.RainbondTeamPageResult;
import com.devops.plugins.core.pojo.rainbond.RainbondTeam;
import com.devops.plugins.core.pojo.vo.resp.region.RegionResp;
import com.devops.plugins.core.pojo.vo.resp.team.TeamResp;
import com.devops.plugins.core.service.TeamService;
import com.devops.plugins.core.utils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    @Autowired
    PaasClient paasClient;

    @Override
    public List<TeamResp> list() {
       RainbondTeamPageResult rainbondTeamPageResult =  paasClient.listTeams();
       List<RainbondTeam> teams = rainbondTeamPageResult.getTenants();
       if(CollectionUtils.isEmpty(teams)) {
           return new ArrayList<>();
       }
        return ConvertUtils.convertList(teams, TeamResp.class);
    }

    @Override
    public List<RegionResp> listRegions(String teamId) {
        return ConvertUtils.convertList(paasClient.listTeamRegions(teamId).getRegions(), RegionResp.class);
    }
}
