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

package com.devops.plugins.core.pojo.vo.req.customPipeline;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 自定义流水线表-实体类
 *
 * @author sheep
 * @Date 2022-08-10 14:50:59
 */
@Getter
@Setter
public class CustomPipelinePageReq implements Serializable{

	private static final long serialVersionUID = 1L;


    /** 流水线名称 */
    @ApiModelProperty(notes = "团队标识", example = "团队标识")
    private String teamId;


    /** 流水线名称 */
    @ApiModelProperty(notes = "流水线名称", example = "流水线名称")
    private String name;

}
