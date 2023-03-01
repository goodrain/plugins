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

package com.devops.plugins.core.pojo.vo.resp.appService;

import com.devops.plugins.core.pojo.vo.req.appService.subAppService.SubAppServiceDeployEnvVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 应用服务表-实体类
 *
 * @author sheep
 * @Date 2022-01-06 10:07:19
 */
@Getter
@Setter
public class AppServiceDetailResp  implements Serializable{

	private static final long serialVersionUID = 1L;


    /** 主键标识 */
    @ApiModelProperty(notes = "主键标识", dataType = "String", example = "主键标识")
    private String id;
    /** 禅道项目id */
    @ApiModelProperty(notes = "团队标识", dataType = "String", example = "团队标识")
    private String teamId;
	/** 应用服务对应的gitlab项目id */
    @ApiModelProperty(notes = "应用服务对应的gitlab项目id", dataType = "String", example = "应用服务对应的gitlab项目id")
    private String gitlabProjectId;
	/** 应用服务名称 */
    @ApiModelProperty(notes = "应用服务名称", dataType = "String", example = "应用服务名称")
    private String name;
	/** 应用服务编码 */
    @ApiModelProperty(notes = "应用服务编码", dataType = "String", example = "应用服务编码")
    private String code;
	/** gitlab仓库地址 */
    @ApiModelProperty(notes = "gitlab仓库地址", dataType = "String", example = "gitlab仓库地址")
    private String gitlabCodeUrl;
	/** 状态(enable启用,disable禁用) */
    @ApiModelProperty(notes = "状态(enable启用,disable禁用)", dataType = "String", example = "状态(enable启用,disable禁用)")
    private String status;
	/** 模板类型 */
    @ApiModelProperty(notes = "模板类型", dataType = "String", example = "模板类型")
    private String type;
	/** 是否自动构建 */
    @ApiModelProperty(notes = "是否自动构建", dataType = "Boolean", example = "是否自动构建")
    private Boolean autoBuild;

    /** paas应用组标识 */
    @ApiModelProperty(notes = "paas应用组标识", dataType = "Boolean", example = "paas应用组标识")
    private String appId;

    /** paas团队标识 */
    @ApiModelProperty(notes = "paas团队标识", dataType = "Boolean", example = "paas团队标识")
    private String teamCode;

    /** paas组件编码 */
    @ApiModelProperty(notes = "paas组件编码", dataType = "Boolean", example = "paas组件编码")
    private String componentCode;

    /** 服务类型 */
    @ApiModelProperty(notes = "服务类型", dataType = "String", example = "服务类型")
    private String serviceType;

    /** 流水线类型 */
    @ApiModelProperty(notes = "流水线类型", dataType = "String", example = "流水线类型")
    private String pipelineType;

    /** 制品类型 */
    @ApiModelProperty(notes = "制品类型", dataType = "String", example = "制品类型")
    private String productType;

    @ApiModelProperty(notes = "自动部署", dataType = "String", example = "制品类型")
    private Boolean autoDeploy;

    @ApiModelProperty(notes = "流水线id", dataType = "String", example = "制品类型")
    private String pipelineId;

    @ApiModelProperty(notes = "流水线名称", dataType = "String", example = "制品类型")
    private String pipelineName;

    @ApiModelProperty(notes = "子模块自动部署配置", dataType = "String", example = "制品类型")
    private List<SubAppServiceDeployEnvVO> subAppServiceDeployEnvVOS;

    @ApiModelProperty(notes = "自动部署环境", dataType = "String", example = "制品类型")
    private List<String> regionCodes;


}
