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

package com.devops.plugins.core.pojo.vo.resp.appServiceDeployHistory;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class AppServiceDeployPageResp implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键标识
     */
    @ApiModelProperty(notes = "主键标识", dataType = "String", example = "主键标识")
    private String id;

    /**
     * 状态
     */
    @ApiModelProperty(notes = "状态", dataType = "String", example = "状态")
    private String status;
    /**
     * 应用服务版本
     */
    @ApiModelProperty(notes = "应用服务版本", dataType = "String", example = "应用服务版本")
    private String appServiceVersion;
    /**
     * 描述
     */
    @ApiModelProperty(notes = "描述", dataType = "String", example = "描述")
    private String description;

    /**
     * 部署详情
     */
    @ApiModelProperty(notes = "部署详情", dataType = "String", example = "部署详情")
    private String deployInfoUrl;


    /**
     * 集群编码
     */
    @TableField(value = "region_code")
    @ApiModelProperty(notes = "paas集群编码", dataType = "String", example = "paas集群编码")
    private String regionCode;


    /**
     * 所属团队
     */
    @TableField(value = "team_code")
    @ApiModelProperty(notes = "所属团队", dataType = "String", example = "所属团队")
    private  String  teamCode;



    /**
     * 所属组件
     */
    @TableField(value = "component_code")
    @ApiModelProperty(notes = "所属组件", dataType = "String", example = "所属组件")
    private  String  componentCode;


    /**
     * 子模块名称
     */
    @ApiModelProperty(notes = "子模块名称", dataType = "String", example = "子模块名称")
    private String subService;


    /**
     * 部署时间
     */
    @ApiModelProperty(notes = "部署时间", dataType = "String", example = "部署时间")
    private Date createTime;

    /**
     * 部署人
     */
    @ApiModelProperty(notes = "部署人", dataType = "String", example = "部署人")
    private String createBy;

    public Timestamp modifyTime(Date create_time) {
        long time = create_time.getTime() / 1000l;
        long daySecond = 60 * 60 * 24;
        long dayTime = time - (time + 8 * 3600) % daySecond;
        return new Timestamp(dayTime * 1000);
    }
}
