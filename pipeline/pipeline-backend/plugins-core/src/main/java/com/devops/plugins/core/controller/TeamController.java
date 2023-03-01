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

package com.devops.plugins.core.controller;

import com.devops.plugins.common.result.R;
import com.devops.plugins.core.pojo.vo.resp.region.RegionResp;
import com.devops.plugins.core.pojo.vo.resp.team.TeamResp;
import com.devops.plugins.core.service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "团队接口")
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    TeamService teamService;

    /**
     * 获取团队列表
     */
    @GetMapping(name = "获取团队列表", value = "/list")
    @ApiOperation(value = "获取团队列表", notes = "获取团队列表")
    public R<List<TeamResp>> findList() {
        return R.ok("查询成功", teamService.list());
    }



    /**
     * 获取团队绑定额集群列表
     *
     * @param teamId
     * @return
     */
    @GetMapping(name = "获取团队绑定的集群列表", value = "/regions")
    @ApiOperation(value = "获取团队绑定的集群列表", notes = "获取团队绑定的集群列表")
    public R<List<RegionResp>> listRegions(@RequestParam(value = "teamId")  String teamId) {
        return R.ok("查询成功", teamService.listRegions(teamId));
    }


}
