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

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 *
 *
 * @author sheep
 * @Date 2022-08-10 14:51:44
 */
@Getter
@Setter
public class CustomPipelineTempResp implements Serializable{

	private static final long serialVersionUID = 1L;

    /** 主键标识 */
    @ApiModelProperty(notes = "主键标识", example = "主键标识")
    private String id;

    /** 模板名称 */
    @ApiModelProperty(notes = "模板名称", example = "模板名称")
    private String name;

	/** 模板描述 */
    @ApiModelProperty(notes = "模板描述", example = "模板描述")
    private String description;

    @ApiModelProperty(notes = "模板阶段", example = "模板阶段")
    private List<String> stages;

}
