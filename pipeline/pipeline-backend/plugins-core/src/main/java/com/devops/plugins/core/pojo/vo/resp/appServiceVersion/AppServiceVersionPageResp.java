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

package com.devops.plugins.core.pojo.vo.resp.appServiceVersion;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author huangtf
 * @Date 2022-01-06 18:22:33
 */
@Getter
@Setter
public class AppServiceVersionPageResp implements Serializable{

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "主键标识", dataType = "String", example = "主键标识")
    private String id;

    @ApiModelProperty(notes = "应用服务标识", dataType = "String", example = "应用服务标识")
    private String appServiceId;
	/** 提交人 */
    @ApiModelProperty(notes = "提交人", dataType = "String", example = "提交人")
    private String committer;
	/** 提交信息 */
    @ApiModelProperty(notes = "提交信息", dataType = "String", example = "提交信息")
    private String commitMsg;
    /** 提交时间 */
    @ApiModelProperty(notes = "提交时间", dataType = "Date", example = "提交时间")
    private Date commitTime;
    /** 镜像版本 */
    @ApiModelProperty(notes = "镜像版本", dataType = "String", example = "镜像版本")
    private String appServiceVersion;
    /** 镜像 */
    @ApiModelProperty(notes = "镜像", dataType = "String", example = "镜像")
    private String image;
}
