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

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sheep
 * @create 2022-01-06 10:16
 */
@Getter
@Setter
public class AppServiceAddReq {

    @NotNull
    @ApiModelProperty(notes = "团队标识", dataType = "String", example = "团队标识")
    private String teamId;

    @NotNull
    @ApiModelProperty(notes = "团队编码", dataType = "String", example = "团队编码")
    private String teamCode;
    @NotNull
    /** 应用服务名称 */
    @ApiModelProperty(notes = "应用服务名称", dataType = "String", example = "应用服务名称")
    private String name;
    @NotNull
    /** 应用服务编码 */
    @ApiModelProperty(notes = "应用服务编码", dataType = "String", example = "应用服务编码")
    private String code;
    private String type;
    /** 是否自动构建 */
    @ApiModelProperty(notes = "是否自动构建", dataType = "Boolean", example = "是否自动构建")
    private Boolean autoBuild;

    /** 应用服务类型 */
    @ApiModelProperty(notes = "应用服务类型", dataType = "String", example = "应用服务类型")
    private String serviceType;

    /** 外部仓库地址 */
    @ApiModelProperty(notes = "外部仓库地址", dataType = "String", example = "外部仓库地址")
    private String url;

    /** 个人访问token */
    @ApiModelProperty(notes = "个人访问token", dataType = "String", example = "个人访问token")
    private String token;

    /** 用户名 */
    @ApiModelProperty(notes = "用户名", dataType = "String", example = "用户名")
    private String username;

    /** 密码 */
    @ApiModelProperty(notes = "密码", dataType = "String", example = "密码")
    private String password;

    /**
     * war包。jar包
     */
    @ApiModelProperty(notes = "war,jar包类型", dataType = "String", example = "war,jar包类型")
    private String productType;

    /**
     * 流水线id
     *
     */
    @ApiModelProperty(notes = "流水线Id", dataType = "String", example = "流水线Id")
    private String pipelineId;

    /**
     * 流水线类型
     *
     */
    @ApiModelProperty(notes = "流水线类型", dataType = "String", example = "流水线类型")
    private String pipelineType;

    private List<AppServiceAddReq> subAppServices;


}
