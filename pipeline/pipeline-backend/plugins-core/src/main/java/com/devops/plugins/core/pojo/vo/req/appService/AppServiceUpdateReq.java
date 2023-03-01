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

package com.devops.plugins.core.pojo.vo.req.appService;

import com.devops.plugins.core.pojo.vo.req.appService.subAppService.SubAppServiceDeployEnvVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author sheep
 * @create 2022-01-10 9:37
 */
@Getter
@Setter
public class AppServiceUpdateReq {

    @ApiModelProperty(notes = "主键标识", dataType = "String", example = "主键标识")
    private String id;
    /** 应用服务名称 */
    @ApiModelProperty(notes = "应用服务名称", dataType = "String", example = "应用服务名称")
    private String name;
    /** 是否自动构建 */
    @ApiModelProperty(notes = "是否自动构建", dataType = "Boolean", example = "是否自动构建")
    private Boolean autoBuild;
    /** 是否自动部署 */
    @ApiModelProperty(notes = "是否自动部署", dataType = "Boolean", example = "是否自动部署")
    private Boolean autoDeploy;
    @ApiModelProperty(notes = "环境编码", dataType = "String", example = "环境编码")
    private List<String> regionCodes;
    /** 状态(enable启用,disable禁用) */
    @ApiModelProperty(notes = "状态(enable启用,disable禁用)", dataType = "String", example = "状态(enable启用,disable禁用)")
    private String status;

    @ApiModelProperty(notes = "自定义流水线id", dataType = "String", example = "自定义流水线id")
    private String pipelineId;

    @ApiModelProperty(notes = "自定义流水线类型", dataType = "String", example = "自定义流水线类型")
    private String pipelineType;

    @ApiModelProperty(notes = "应用服务子模块自动部署配置", dataType = "String", example = "应用服务子模块自动部署配置")
    private List<SubAppServiceDeployEnvVO> subAppServiceDeployEnvVOS;

}
