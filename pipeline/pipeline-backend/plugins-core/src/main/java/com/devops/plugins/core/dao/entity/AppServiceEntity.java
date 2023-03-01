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

package com.devops.plugins.core.dao.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.devops.plugins.common.pojo.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 应用服务表-实体类
 *
 * @author sheep
 * @Date 2022-01-06 10:07:19
 */
@Getter
@Setter
@TableName("devops_app_service")
public class AppServiceEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用服务对应的gitlab项目id
     */
    @TableField(value = "gitlab_project_id")
    @ApiModelProperty(notes = "应用服务对应的gitlab项目id", dataType = "String", example = "应用服务对应的gitlab项目id")
    private String gitlabProjectId;
    /**
     * 应用服务名称
     */
    @ApiModelProperty(notes = "应用服务名称", dataType = "String", example = "应用服务名称")
    private String name;
    /**
     * 应用服务编码
     */
    @ApiModelProperty(notes = "应用服务编码", dataType = "String", example = "应用服务编码")
    private String code;
    /**
     * gitlab仓库地址
     */
    @TableField(value = "gitlab_code_url")
    @ApiModelProperty(notes = "gitlab仓库地址", dataType = "String", example = "gitlab仓库地址")
    private String gitlabCodeUrl;
    /**
     * 状态(enable启用,disable禁用)
     */
    @ApiModelProperty(notes = "状态(enable启用,disable禁用)", dataType = "String", example = "状态(enable启用,disable禁用)")
    private String status;


    /**
     * 是否自动构建
     */
    @TableField(value = "auto_build")
    @ApiModelProperty(notes = "是否自动构建", dataType = "Boolean", example = "是否自动构建")
    private Boolean autoBuild;

    /**
     * paas团队编码
     */
    @TableField(value = "team_code")
    @ApiModelProperty(notes = "paas团队编码", dataType = "Boolean", example = "paas团队编码")
    private String teamCode;


    /**
     * paas团队编码
     */
    @TableField(value = "type")
    @ApiModelProperty(notes = "类型", dataType = "String", example = "类型")
    private String type;

    /**
     * paas团队标识
     */
    @TableField(value = "team_id")
    @ApiModelProperty(notes = "paas团队标识", dataType = "Boolean", example = "paas团队标识")
    private String teamId;

    /**
     * 服务类型
     */
    @TableField(value = "service_type")
    @ApiModelProperty(notes = "服务类型", dataType = "String", example = "服务类型")
    private String serviceType;

    /**
     * 流水线类型
     */
    @TableField(value = "pipeline_type")
    @ApiModelProperty(notes = "流水线类型", dataType = "String", example = "流水线类型")
    private String pipelineType;



}
