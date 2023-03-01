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

package com.devops.plugins.core.pojo.vo.req.appService.subAppService;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 应用子服务实体
 *
 * @author zzy
 * @Date 2022-01-07 15:34:26
 */

@Getter
@Setter
public class SubAppServiceVO  implements Serializable{

	private static final long serialVersionUID = 1L;

    /** 主键标识 */
    @ApiModelProperty(notes = "主键标识", dataType = "String", example = "主键标识")
    private String id;
	/** 应用服务id */
    @ApiModelProperty(notes = "应用服务id", dataType = "String", example = "应用服务id")
    private String app_service_id;
	/** 应用服务code */
    @ApiModelProperty(notes = "应用服务code", dataType = "String", example = "应用服务code")
    private String code;
    /** paas组件编码 */
    @ApiModelProperty(notes = "paas组件编码", dataType = "Boolean", example = "paas组件编码")
    private String component_code;
    /** 是否自动部署 */
    @ApiModelProperty(notes = "是否自动构建", dataType = "Boolean", example = "是否自动构建")
    private Boolean auto_build;

    @ApiModelProperty(notes = "是否自动部署", dataType = "Boolean", example = "是否自动部署")
    private Boolean auto_deploy;

}
