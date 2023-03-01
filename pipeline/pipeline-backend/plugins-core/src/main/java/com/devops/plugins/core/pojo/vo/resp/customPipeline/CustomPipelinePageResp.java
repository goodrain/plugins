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

package com.devops.plugins.core.pojo.vo.resp.customPipeline;

import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelineStageResp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 自定义流水线表-实体类
 *
 * @author sheep
 * @Date 2022-08-10 14:50:59
 */
@Getter
@Setter
public class CustomPipelinePageResp  implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 项目id */
    @ApiModelProperty(notes = "主键标识", example = "主键标识")
    private String id;
	/** 流水线名称 */
    @ApiModelProperty(notes = "流水线名称", example = "流水线名称")
    private String name;
    @ApiModelProperty(notes = "应用服务名称", example = "应用服务名称")
    private String appServiceName;
    @ApiModelProperty(notes = "流水线开始时间", example = "流水线开始时间")
    private Date startTime;
    @ApiModelProperty(notes = "流水线阶段", example = "流水线阶段")
    private List<AppServicePipelineStageResp> stages;
    @ApiModelProperty(notes = "创建时间", example = "创建时间")
    private Date createTime;

}
