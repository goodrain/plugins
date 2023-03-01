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
 * 应用服务自动部署表-实体类
 *
 * @author sheep
 * @Date 2022-03-01 14:59:17
 */
@TableName("devops_app_service_auto_deploy")
@Getter
@Setter
public class AppServiceAutoDeployEntity   extends CommonEntity implements Serializable {

    /** 应用服务id（可为空） */
    @TableField(value = "app_service_id")
    @ApiModelProperty(notes = "对应idaas的用户id", dataType = "String", example = "对应应用服务id")
    private String appServiceId;
    /** 子应用服务id（可为空） */
    @TableField(value = "sub_app_service_id")
    @ApiModelProperty(notes = "子应用服务id（可为空）", dataType = "String", example = "子应用服务id（可为空）")
    private String subAppServiceId;
    /** paas环境 */
    @TableField(value = "region_code")
    private String regionCode;

}
