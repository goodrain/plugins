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
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.common.result.R;
import com.devops.plugins.core.clients.HarborClient;
import com.devops.plugins.core.clients.PaasClient;
import com.devops.plugins.core.pojo.vo.req.appService.*;
import com.devops.plugins.core.pojo.vo.resp.appService.*;
import com.devops.plugins.core.service.IAppServiceService;
import com.devops.plugins.core.utils.ConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 应用服务表控制器
 *
 * @author sheep
 * @Date 2022-01-06 10:07:19
 */
@Api(tags="应用服务接口")
@RestController
@RequestMapping("/app-service")
public class AppServiceController {

    @Autowired
    private IAppServiceService appServiceService;
    @Autowired
    private PaasClient paasClient;


    /**
     * 获取应用服务表列表分页
     */
    @PostMapping(name = "获取应用服务表列表", value = "/pagelist")
    @ApiOperation(value = "获取应用服务表列表分页", notes = "默认查询第1页，每页20条数据")
    public R<Page<AppServicePageResp>> findPage(@RequestBody ReqPage<AppServicePageReq> page) {
        Page<AppServicePageResp> result = appServiceService.findPage(page.getQueryVO(), page);
        return R.ok("查询成功", result);
    }

    /**
     * 应用服务表详情
     */
    @GetMapping(name = "获取应用服务表详情", value = "/detail/{appServiceId}")
    @ApiOperation(value = "获取应用服务表详情", httpMethod = "GET")
    public R<AppServiceDetailResp> detail(@PathVariable("appServiceId")  String appServiceId) {
        AppServiceDetailResp appServiceDetailResp = appServiceService.selectById(appServiceId);
        return R.ok("查询成功", appServiceDetailResp);
    }


    /**
     * 新增应用服务表
     */
    @PostMapping(name = "新增应用服务表", value = "/add")
    @ApiOperation(value = "新增应用服务表", httpMethod = "POST")
    public R create(@RequestBody AppServiceAddReq appService) {
        appServiceService.create(appService);
        return R.ok("新增成功");
    }

    /**
     * 修改应用服务表
     */
    @PostMapping(name = "修改应用服务表", value = "/update")
    @ApiOperation(value = "修改应用服务表", httpMethod = "POST")
    public R update(@RequestBody AppServiceUpdateReq appServiceUpdateReq) {
        appServiceService.update(appServiceUpdateReq);
        return R.ok("修改成功");
    }

    /**
     * 删除应用服务表
     */
    @PostMapping(name = "删除应用服务表", value = "/delete/{id}")
    @ApiOperation(value = "删除应用服务表", httpMethod = "POST")
    public R delete(@PathVariable(value = "id")  String appServiceId) {
        appServiceService.delete(appServiceId);
        return R.ok("删除成功");
    }


    /**
     * 获取应用服务代码仓库的clone-url
     *
     * @param id
     * @return
     */
    @GetMapping("/url/{id}")
    @ApiOperation(value = "获取应用服务代码仓库的clone-url", httpMethod = "GET")
    public R<AppServiceUrlResp> getAppServiceUrl(@PathVariable(value = "id")  String id) {
        return R.ok(appServiceService.getAppServiceUrl(id));
    }

    /**
     * 获取应用服务代码仓库的介绍
     *
     * @param id
     * @return
     */
    @GetMapping("/desc/{id}")
    @ApiOperation(value = "获取应用服务代码仓库的介绍", httpMethod = "GET")
    public R<String> appServiceDesc(@PathVariable(value = "id")  String id) {
        return R.ok("查询成功", appServiceService.getAppServiceDesc(id));
    }

    /**
     * 根据项目id查询应用服务列表
     *
     * @param
     * @return
     */
    @GetMapping("/list/{teamId}")
    @ApiOperation(value = "根据团队id查询应用服务列表", httpMethod = "GET")
    public R<List<AppServiceListResp>> listAppServiceByTeam(@PathVariable(value = "teamId")  String teamId) {
        return R.ok("查询成功", appServiceService.listAppServiceByTeam(teamId));
    }

    /**
     * 查询应用服务列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询应用服务列表", httpMethod = "GET")
    public R<List<AppServiceListResp>> listAppService() {
        return R.ok("查询成功", ConvertUtils.convertList(appServiceService.list(), AppServiceListResp.class));
    }

    /**
     * 获取应用服务可用的应用服务模板
     *
     * @return
     */
    @GetMapping("/template")
    @ApiOperation(value = "查询应用服务模板", httpMethod = "GET")
    public R<List<String>> listAppServiceTemplate() {
        return R.ok("查询成功", appServiceService.listAppTemplate());
    }

    /**
     * 获取应用服务子模块列表（微服务模块类型的应用服务）
     *
     * @return
     */
    @GetMapping("/{id}/subService")
    @ApiOperation(value = "获取应用服务子模块列表（微服务模块类型的应用服务）", httpMethod = "GET")
    public R<List<String>> listSubAppService(@PathVariable(value = "id")  String id) {
        return R.ok("查询成功", appServiceService.listSubService(id));
    }


    /**
     * 添加应用服务子模块（微服务模块类型的应用服务）
     *
     * @return
     */
    @PostMapping("/{id}/subService")
    @ApiOperation(value = "添加应用服务子模块（微服务模块类型的应用服务）", httpMethod = "POST")
    public R addSubAppService(@PathVariable(value = "id") String id, @RequestBody List<String> module) {
        appServiceService.addSubService(id, module);
        return R.ok();
    }

    /**
     * 删除应用服务子模块（微服务模块类型的应用服务）
     *
     * @return
     */
    @PostMapping("/{id}/subService/delete")
    @ApiOperation(value = "删除应用服务子模块（微服务模块类型的应用服务）", httpMethod = "POST")
    public R deleteSubAppService(@PathVariable(value = "id")  String id, @RequestBody List<String> module) {
        appServiceService.deleteSubService(id, module);
        return R.ok();
    }

    /**
     * 判断应用服务是否可以开启自动部署功能
     *
     * @return
     */
    @GetMapping("/{id}/can-deploy")
    @ApiOperation(value = "判断应用服务是否可以开启自动部署功能", httpMethod = "GET")
    public R<Boolean> canDeploy(@PathVariable(value = "id") String id, @RequestParam(required = false, value = "subId") String subId) {
        return R.ok("查询成功", appServiceService.canDeploy(id, subId));
    }

    /**
     * 修改外部gitlab应用服务类型的仓库地址
     *
     * @return
     */
    @PostMapping("/{id}/modify-externel")
    @ApiOperation(value = "修改外部gitlab应用服务类型的仓库地址", httpMethod = "POST")
    public R modifyExternel(@PathVariable(value = "id") String id, @RequestBody AppServiceUrlModifyReq appServiceUrlModifyVO) {
        appServiceService.modifyExternel(id, appServiceUrlModifyVO);
        return R.ok();
    }


    /**
     * 查询外部gitlab应用服务类型的仓库地址
     *
     * @return
     */
    @GetMapping("/{id}/get-externel")
    @ApiOperation(value = "查询外部gitlab应用服务类型的仓库地址", httpMethod = "GET")
    public R<AppServiceUrlModifyResp> getExternel(@PathVariable(value = "id") String id) {
        return R.ok("查询成功", appServiceService.getExternel(id));
    }


    @GetMapping("/{id}/url")
    @ApiOperation(value = "获取外部gitlab应用服务类型的仓库地址", httpMethod = "GET")
    public R<AppServiceRepositoryUrlResp> getRepositoryUrl(@PathVariable(value = "id") String id) {
        return R.ok("查询成功", appServiceService.getRepositoryUrl(id));
    }


    /**
     * 测试绑定外部gitlab仓库时，参数是否正确
     *
     * @param url
     * @param token
     * @param username
     * @param password
     * @return
     */
    @ApiOperation(value = "测试绑定外部gitlab仓库时，参数是否正确")
    @GetMapping(value = "/check")
    public R<Boolean> checkAppService(@ApiParam(value = "外部仓库地址", required = true)
                                          @RequestParam(value = "url") String url,
                                      @ApiParam(value = "用户token")
                                          @RequestParam(value = "token", required = false) String token,
                                      @ApiParam(value = "用户名")
                                          @RequestParam(value = "username", required = false) String username,
                                      @ApiParam(value = "密码")
                                          @RequestParam(value = "password", required = false) String password) {
        return R.ok(appServiceService.check(url, token, username, password));
    }

}

