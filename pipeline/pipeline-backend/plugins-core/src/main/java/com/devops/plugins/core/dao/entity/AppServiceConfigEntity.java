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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * -实体类
 *
 * @author sheep
 * @Date 2022-04-08 17:03:21
 */
@ApiModel(description = "应用服务外部配置表")
@TableName("devops_app_service_config")
@Getter
@Setter
public class AppServiceConfigEntity extends CommonEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 应用服务标识*/
    @TableField(value = "app_service_id")
    @ApiModelProperty(notes = "应用服务标识", dataType = "String", example = "应用服务标识）")
    private String appServiceId;
	/**  */
    @ApiModelProperty(notes = "token", dataType = "String", example = "token")
    private String token;
	/**  */
    @ApiModelProperty(notes = "用户名", dataType = "String", example = "用户名")
    private String username;
	/**  */
    @ApiModelProperty(notes = "密码", dataType = "String", example = "密码")
    private String password;


}
