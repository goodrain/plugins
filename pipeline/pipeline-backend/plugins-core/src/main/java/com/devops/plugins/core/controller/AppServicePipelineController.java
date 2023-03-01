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
import com.devops.plugins.core.pojo.vo.req.appServicePipeline.AppServicePipelinePageReq;
import com.devops.plugins.core.pojo.vo.req.appServicePipeline.CiReq;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelineEnvsResp;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelinePageResp;
import com.devops.plugins.core.service.IAppServicePipelineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 应用服务的ci流水线表控制器
 *
 * @author sheep
 * @Date 2022-01-11 14:36:10
 */
@Api(tags = "应用服务的ci流水线表服务接口")
@RestController
@RequestMapping("/app-service-pipeline")
public class AppServicePipelineController {

    @Autowired
    private IAppServicePipelineService appServicePipelineService;

    /**
     * 获取应用服务的ci流水线表列表分页
     */
    @PostMapping(name="获取应用服务的ci流水线表列表", value = "/pagelist")
    @ApiOperation(value = "获取应用服务的ci流水线表列表分页", notes = "默认查询第1页，每页20条数据")
    public R<Page<AppServicePipelinePageResp>> findPage(@RequestBody ReqPage<AppServicePipelinePageReq> page) {
        Page<AppServicePipelinePageResp> result = appServicePipelineService.findPage(page.getQueryVO(), page);
        return R.ok("查询成功", result);
    }

    /**
     * 查询Job日志
     *
     * @param pipelineId
     * @param jobId
     * @return
     */
    @GetMapping(value = "/{pipelineId}/job/{jobId}/log")
    @ApiOperation(value = "查询job日志", notes = "查询job日志")
    public R<String> getJobLog(@PathVariable(value = "pipelineId") String pipelineId,
                               @PathVariable(value = "jobId") String jobId) {
        return R.ok(appServicePipelineService.getJobLog(pipelineId, jobId));
    }

    /**
     * 执行指定分支流水线
     * @param appServiceId
     * @param branch
     * @return
     */
    @GetMapping(value = "/pipeline/run")
    @ApiOperation(value = "执行指定分支流水线", notes = "执行指定分支流水线")
    public R runCi(@RequestParam(value = "appServiceId") String appServiceId,
                   @RequestParam(value = "branch") String branch,
                   @RequestParam(value = "module", required = false) String module) {
        appServicePipelineService.runCi(appServiceId, branch, module);
        return R.ok("执行成功");
    }

    /**
     * 执行指定分支流水线-带环境变量
     * @return
     */
    @PostMapping(value = "/pipeline/run-vars")
    @ApiOperation(value = "执行指定分支流水线", notes = "执行指定分支流水线")
    public R runCiWithVars(@RequestBody CiReq ciReq) {
        appServicePipelineService.runCiWithVars(ciReq);
        return R.ok("执行成功");
    }

    /**
     * 查询环境变量
     *
     * @param appServiceId
     * @param branch
     * @param module
     * @return
     */
    @GetMapping(value = "/pipeline/vars")
    @ApiOperation(value = "查询环境变量", notes = "查询环境变量")
    public R<List<AppServicePipelineEnvsResp>> getEnvs(@RequestParam(value = "appServiceId")String appServiceId,
                                                       @RequestParam(value = "branch") String branch,
                                                       @RequestParam(value = "module", required = false) String module) {
        return R.ok(appServicePipelineService.getEnvs(appServiceId, branch, module));
    }


}
