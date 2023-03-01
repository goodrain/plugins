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
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineDetailResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineTempResp;
import com.devops.plugins.core.service.ICustomPipelineTempService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 自定义流水线模板表控制器
 *
 * @author sheep
 * @Date 2022-08-10 14:51:43
 */
@Api(tags = "自定义流水线模板接口")
@RestController
@RequestMapping("/custom-pipeline-temp")
public class CustomPipelineTempController {

    @Resource
    private ICustomPipelineTempService customPipelineTempService;

    /**
     * 获取自定义流水线模板表列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询自定义流水线模板列表", notes = "查询自定义流水线模板列表")
    public R<List<CustomPipelineTempResp>> findList() {
        return R.ok("查询成功", customPipelineTempService.listTemps());
    }


    @GetMapping("/detail/{id}")
    @ApiOperation(value = "查询自定义流水线模板详情", notes = "查询自定义流水线模板详情")
    public R<CustomPipelineDetailResp> detail(@PathVariable(value = "id") String id) {
        return R.ok("查询成功", customPipelineTempService.getTemp(id));
    }

}
