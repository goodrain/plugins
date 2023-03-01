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
 * 应用服务组件表-实体类
 *
 * @author sheep
 * @Date 2022-03-01 14:59:17
 */
@TableName("devops_app_service_component")
@Getter
@Setter
public class AppServiceComponentEntity extends CommonEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 应用服务id（可为空） */
    @TableField(value = "app_service_id")
    @ApiModelProperty(notes = "对应idaas的用户id", dataType = "String", example = "对应应用服务id")
    private String appServiceId;
	/** 子应用服务id（可为空） */
    @ApiModelProperty(notes = "子应用服务id（可为空）", dataType = "String", example = "子应用服务id（可为空）")
    @TableField(value = "sub_app_service_id")
    private String subAppServiceId;
	/** 对应梧桐paas的组件id */
    @TableField(value = "paas_component_id")
    @ApiModelProperty(notes = "对应梧桐paas的组件id", dataType = "String", example = "对应梧桐paas的组件id")
    private String paasComponentId;
    /** 对应梧桐paas的组件code */
    @TableField(value = "paas_component_code")
    @ApiModelProperty(notes = "对应梧桐paas的组件编码", dataType = "String", example = "对应梧桐paas的组件编码")
    private String paasComponentCode;
    @TableField(value = "paas_app_id")
    @ApiModelProperty(notes = "对应梧桐paas的应用id", dataType = "String", example = "对应梧桐paas的应用id")
    private String paasAppId;
    @TableField(value = "region_code")
    @ApiModelProperty(notes = "对应梧桐paas的集群编码", dataType = "String", example = "对应梧桐paas的集群编码")
    private String regionCode;
    @TableField(value = "team_code")
    @ApiModelProperty(notes = "对应梧桐paas的团队编码", dataType = "String", example = "对应梧桐paas的团队编码")
    private String teamCode;

}

