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
 * 应用服务的CI流水线任务表-实体类
 *
 * @author sheep
 * @Date 2022-01-11 14:39:23
 */
@Getter
@Setter
@TableName("devops_app_service_pipeline_job")
public class AppServicePipelineJobEntity extends CommonEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 应用服务流水线id */
    @TableField(value = "pipeline_id")
    @ApiModelProperty(notes = "应用服务流水线id", dataType = "String", example = "应用服务流水线id")
    private String pipelineId;
    /** 应用服务id */
    @TableField(value = "app_service_id")
    @ApiModelProperty(notes = "应用服务id", dataType = "String", example = "应用服务id")
    private String appServiceId;
	/** gitlab流水线任务id */
    @TableField(value = "job_id")
    @ApiModelProperty(notes = "gitlab流水线任务id", dataType = "String", example = "gitlab流水线任务id")
    private String jobId;
	/** 任务阶段 */
    @ApiModelProperty(notes = "任务阶段", dataType = "String", example = "任务阶段")
    private String stage;
	/** 任务阶段状态 */
    @ApiModelProperty(notes = "任务阶段状态", dataType = "String", example = "任务阶段状态")
    private String status;
	/** 开始时间 */
    @TableField(value = "start_time")
    @ApiModelProperty(notes = "开始时间", dataType = "Date", example = "开始时间")
    private Date startTime;
	/** 结束时间 */
    @TableField(value = "end_time")
    @ApiModelProperty(notes = "结束时间", dataType = "Date", example = "结束时间")
    private Date endTime;
    /** job跳转地址 */
    @ApiModelProperty(notes = "job跳转地址", dataType = "String", example = "job跳转地址")
    private String url;
    /** 任务名称 */
    @ApiModelProperty(notes = "任务名称", dataType = "String", example = "任务名称")
    private String name;

}
