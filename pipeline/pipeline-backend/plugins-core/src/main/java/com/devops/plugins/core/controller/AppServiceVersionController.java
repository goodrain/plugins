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
import com.devops.plugins.core.pojo.vo.req.appServiceVersion.AppServiceVersionPageReq;
import com.devops.plugins.core.pojo.vo.req.appServiceVersion.DeployReq;
import com.devops.plugins.core.pojo.vo.resp.app.AppResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.AppServiceVersionPageResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.PaasDependencyResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.PaasVariableResp;
import com.devops.plugins.core.service.IAppServiceVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 应用服务版本表控制器
 *
 * @author huangtf
 * @Date 2022-01-06 18:25:34
 */
@Api(tags = "应用服务版本表服务接口")
@RestController
@RequestMapping("/app-service-version")
public class AppServiceVersionController  {

    @Resource
    private IAppServiceVersionService appServiceVersionService;

    /**
     * 获取应用服务版本表列表分页
     */
    @PostMapping(name="获取应用服务版本表列表", value = "/page")
    @ApiOperation(value = "获取应用服务版本表列表分页", notes = "默认查询第1页，每页20条数据")
    public R<Page<AppServiceVersionPageResp>> findPage(@RequestBody ReqPage<AppServiceVersionPageReq> page) {
         Page<AppServiceVersionPageResp> result = appServiceVersionService.findPage(page.getQueryVO(), page);
         return R.ok("查询成功",result);
    }

    /**
     * 查询团队下可部署的应用
     *
     */
    @GetMapping(name="查询团队下可部署的应用",value = "/list-apps")
    @ApiOperation(value = "查询团队下可部署的应用", httpMethod = "GET")
    public R<List<AppResp>> listApps(
                                    @RequestParam(value = "appServiceId") String appServiceId,
                                    @RequestParam (value = "teamId")String teamId,
                                    @RequestParam(value = "regionCode")String regionCode,
                                    @RequestParam(value = "version")String version) {
        return R.ok("查询成功", appServiceVersionService.listApps(appServiceId, teamId, regionCode, version));
    }


    /**
     *
     * 部署应用服务
     *
     * @return
     */
    @PostMapping(name="部署应用服务",value = "/deploy")
    @ApiOperation(value = "部署应用服务", httpMethod = "POST")
    public R deploy(@RequestBody DeployReq deployReq) {
        appServiceVersionService.deploy(deployReq);
        return R.ok("部署成功");
    }


}
