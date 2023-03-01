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
import com.devops.plugins.core.pojo.vo.req.customPipeline.CustomPipelineDetailReq;
import com.devops.plugins.core.pojo.vo.req.customPipeline.CustomPipelinePageReq;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineDetailResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineListResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelinePageResp;
import com.devops.plugins.core.service.ICustomPipelineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 自定义流水线表控制器
 *
 * @author sheep
 * @Date 2022-08-10 14:50:59
 */
@RestController
@Api(tags = "自定义流水线接口")
@RequestMapping("/custom-pipeline")
public class CustomPipelineController {

    @Resource
    private ICustomPipelineService customPipelineService;

    /**
     * 获取项目下自定义流水线表分页
     */
    @PostMapping("/page")
    @ApiOperation(value = "自定义流水线列表-分页", notes = "自定义流水线列表-分页")
    public R<Page<CustomPipelinePageResp>> findPage(@RequestBody ReqPage<CustomPipelinePageReq> page) {
        CustomPipelinePageReq customPipelinePageReq = page.getQueryVO();
        if (customPipelinePageReq.getName()!=null&&customPipelinePageReq.getName().equals("")) {
            customPipelinePageReq.setName(null);
            page.setQueryVO(customPipelinePageReq);
        }
        Page<CustomPipelinePageResp> result = customPipelineService.findPage(page.getQueryVO(), page);
        return R.ok("查询成功", result);
    }

    /**
     * 获取项目下自定义流水线表列表
     */
    @GetMapping("/list/{teamId}")
    @ApiOperation(value = "自定义流水线列表", notes = "自定义流水线列表")
    public R<List<CustomPipelineListResp>> findList(@PathVariable(value = "teamId") String teamId) {
        List<CustomPipelineListResp> result = customPipelineService.list(teamId);
        return R.ok("查询成功", result);
    }

    /**
     * 自定义流水线表详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "查询自定义流水线详情", notes = "查询自定义流水线详情")
    public R<CustomPipelineDetailResp> detail(@PathVariable(value = "id") String id) {
        CustomPipelineDetailResp result = customPipelineService.getPipeline(id);
        return R.ok("查询成功", result);
    }

    /**
     * 复制自定义流水线表
     */
    @GetMapping("/copy/{id}")
    @ApiOperation(value = "复制自定义流水线", notes = "复制自定义流水线")
    public R<CustomPipelineDetailResp> copy(@PathVariable(value = "id") String id) {
        CustomPipelineDetailResp result = customPipelineService.copyPipeline(id);
        return R.ok("查询成功", result);
    }

    /**
     * 新增自定义流水线表
     */
    @PostMapping("/add")
    @ApiOperation(value = "创建自定义流水线", notes = "创建自定义流水线")
    public R<String> add(@Validated @RequestBody CustomPipelineDetailReq customPipelineDetailReq) {
        customPipelineService.createPipeline(customPipelineDetailReq);
        return R.ok("新增成功");
    }

    /**
     * 修改自定义流水线表
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新自定义流水线", notes = "更新自定义流水线")
    public R<String> update(@Validated @RequestBody CustomPipelineDetailReq customPipelineDetailReq) {
        customPipelineService.updatePipeline(customPipelineDetailReq);
        return R.ok("修改成功");
    }

    /**
     * 删除自定义流水线表
     */
    @GetMapping("/delete/{id}")
    @ApiOperation(value = "删除自定义流水线", notes = "删除自定义流水线")
    public R<String> delete(@PathVariable(value = "id") String id) {
        customPipelineService.deletePipeline(id);
        return R.ok("删除成功");
    }


    /**
     * 删除自定义流水线表
     */
    @GetMapping("/use/{id}")
    @ApiOperation(value = "查询自定义流水线是否被应用服务绑定", notes = "查询自定义流水线是否被应用服务绑定")
    public R<Boolean> use(@PathVariable(value = "id") String id) {
        customPipelineService.use(id);
        return R.ok("查询成功", customPipelineService.use(id));
    }

}
