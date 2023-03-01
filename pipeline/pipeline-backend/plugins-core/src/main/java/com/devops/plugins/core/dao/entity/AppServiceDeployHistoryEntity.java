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
import java.sql.Timestamp;
import java.util.Date;

/**
 * -实体类
 *
 * @author sheep
 * @Date 2022-03-03 17:12:42
 */
@Getter
@Setter
@TableName("devops_app_service_deploy_history")
public class AppServiceDeployHistoryEntity extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用服务id
     */
    @TableField(value = "app_service_id")
    @ApiModelProperty(notes = "应用服务id", dataType = "String", example = "应用服务id")
    private String appServiceId;

    /**
     * 子应用服务id
     */
    @TableField(value = "sub_app_service_id")
    @ApiModelProperty(notes = "子应用服务id", dataType = "String", example = "子应用服务id")
    private String subAppServiceId;
    /**
     * 状态
     */
    @ApiModelProperty(notes = "状态", dataType = "String", example = "状态")
    private String status;
    /**
     * 应用服务版本
     */
    @TableField(value = "app_service_version")
    @ApiModelProperty(notes = "应用服务版本", dataType = "String", example = "应用服务版本")
    private String appServiceVersion;
    /**
     * 集群编码
     */
    @TableField(value = "region_code")
    @ApiModelProperty(notes = "paas集群编码", dataType = "String", example = "paas集群编码")
    private String regionCode;
    /**
     * 描述
     */
    @ApiModelProperty(notes = "描述", dataType = "String", example = "描述")
    private String description;


    /**
     * 描述
     */
    @TableField(value = "team_code")
    @ApiModelProperty(notes = "所属团队", dataType = "String", example = "所属团队")
    private  String  teamCode;

    /**
     * 描述
     */
    @TableField(value = "component_code")
    @ApiModelProperty(notes = "所属组件", dataType = "String", example = "所属组件")
    private  String  componentCode;

    /**
     * 部署详情
     */
    @TableField(value = "deploy_info_url")
    @ApiModelProperty(notes = "部署详情", dataType = "String", example = "部署详情")
    private String deployInfoUrl;


    public Timestamp modifyTime(Date create_time) {
        long time = create_time.getTime() / 1000L;
        long daySecond = 60 * 60 * 24;
        long dayTime = time - (time + 8 * 3600) % daySecond;
        return new Timestamp(dayTime * 1000);
    }
}
