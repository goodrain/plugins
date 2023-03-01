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
import com.devops.plugins.core.pojo.vo.req.appServiceDeployHistory.AppServiceDeployPageReq;
import com.devops.plugins.core.pojo.vo.resp.appServiceDeployHistory.AppServiceDeployPageResp;
import com.devops.plugins.core.service.IAppServiceDeployHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 控制器
 *
 * @author sheep
 * @Date 2022-03-03 17:12:42
 */
@Api(tags="服务部署历史接口")
@RestController
@RequestMapping("/app-service-deploy-history")
public class AppServiceDeployHistoryController  {

    @Resource
    private IAppServiceDeployHistoryService appServiceDeployHistoryService;

    /**
     * 获取列表分页
     */
    @PostMapping(name="获取列表", value = "/page")
    @ApiOperation(value = "获取列表分页", notes = "默认查询第1页，每页20条数据")
    public R<Page<AppServiceDeployPageResp>> findPage(@RequestBody ReqPage<AppServiceDeployPageReq> page) {
         Page<AppServiceDeployPageResp> result = appServiceDeployHistoryService.findPage(page.getQueryVO(), page);
         return R.ok("查询成功",result);
    }

}
