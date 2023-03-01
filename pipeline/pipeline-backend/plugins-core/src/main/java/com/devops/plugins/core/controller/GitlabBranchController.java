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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.common.result.R;
import com.devops.plugins.core.pojo.vo.req.branch.GitlabBranchAddReq;
import com.devops.plugins.core.pojo.vo.req.branch.GitlabBranchPageReq;
import com.devops.plugins.core.pojo.vo.resp.branch.GitlabBranchPageResp;
import com.devops.plugins.core.service.IGitlabBranchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sheep
 * @create 2022-01-11 14:41
 */

@Api(tags = "分支表服务接口")
@RestController
@RequestMapping("branch")
public class GitlabBranchController {


    @Autowired
    IGitlabBranchService gitlabBranchService;



    /**
     * 获取gitlab的分支表列表分页
     */
    @PostMapping(name="获取gitlab的branch表列表", value = "/pagelist")
    @ApiOperation(value = "获取gitlab的branch表列表分页", notes = "默认查询第1页，每页20条数据")
    public R<Page<GitlabBranchPageResp>> findPage(@RequestBody ReqPage<GitlabBranchPageReq> page) {
        Page<GitlabBranchPageResp> result = gitlabBranchService.findPage(page.getQueryVO(), page);
        return R.ok(result);
    }


    /**
     * 新增gitlab的branch表
     */
    @PostMapping(name="新增branch表", value = "/add")
    @ApiOperation(value = "新增的branch表", httpMethod = "POST")
    public R add(@RequestBody GitlabBranchAddReq gitlabBranchAddReq) {
        gitlabBranchService.create(gitlabBranchAddReq);
        return R.ok();
    }


    /**
     * 删除gitlab的branch表
     */
    @PostMapping(name="删除gitlab的branch表",value = "/delete/{id}")
    @ApiOperation(value = "删除gitlab的branch表", httpMethod = "POST")
    public R delete(@PathVariable(value = "id")  String gitlabBranchId) {
        gitlabBranchService.delete(gitlabBranchId);
        return R.ok();
    }
}
