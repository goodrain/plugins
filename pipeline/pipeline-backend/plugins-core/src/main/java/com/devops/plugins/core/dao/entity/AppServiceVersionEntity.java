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
import java.util.Date;

/**
 * 应用服务版本表-实体类
 *
 * @author huangtf
 * @Date 2022-01-06 18:22:33
 */
@Getter
@Setter
@TableName("devops_app_service_version")
public class AppServiceVersionEntity extends CommonEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 应用服务id */
    @TableField(value = "app_service_id")
    @ApiModelProperty(notes = "应用服务id", dataType = "String", example = "应用服务id")
    private String appServiceId;
	/** 应用服务版本 */
    @TableField(value = "app_service_version")
    @ApiModelProperty(notes = "应用服务版本", dataType = "String", example = "应用服务版本")
    private String appServiceVersion;
	/** 提交人 */
    @ApiModelProperty(notes = "提交人", dataType = "String", example = "提交人")
    private String committer;
	/** 提交信息 */
    @TableField(value = "commit_msg")
    @ApiModelProperty(notes = "提交信息", dataType = "String", example = "提交信息")
    private String commitMsg;
	/** 提交值 */
    @TableField(value = "commit_value")
    @ApiModelProperty(notes = "提交值", dataType = "String", example = "提交值")
    private String commitValue;
    /** 提交时间 */
    @TableField(value = "commit_time")
    @ApiModelProperty(notes = "提交时间", dataType = "Date", example = "提交时间")
    private Date commitTime;
    /** 镜像 */
    @ApiModelProperty(notes = "镜像", dataType = "Date", example = "镜像")
    private String image;

}
