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

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 自定义流水线模板阶段表-实体类
 *
 * @author sheep
 * @Date 2022-08-10 14:51:37
 */
@Getter
@Setter
@ApiModel(description = "自定义流水线模板阶段表")
@TableName("devops_custom_pipeline_temp_stage")
public class CustomPipelineTempStageEntity extends CommonEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 流水线模板id */
    @TableField(value = "template_id")
    @ApiModelProperty(notes = "流水线模板id", example = "流水线模板id", position = 1)
    private String templateId;
	/** 阶段名称 */
    @ApiModelProperty(notes = "阶段名称", example = "阶段名称", position = 2)
    private String name;
	/** 阶段编码 */
    @ApiModelProperty(notes = "阶段编码", example = "阶段编码", position = 3)
    @NotBlank(message = "阶段编码不可为空")
    private String code;
	/** 阶段顺序 */
    @TableField(value = "stage_order")
    @ApiModelProperty(notes = "阶段顺序", example = "阶段顺序", position = 4)
    private Integer stageOrder;
	/** 阶段json */
    @ApiModelProperty(notes = "阶段json", example = "阶段json", position = 5)
    private String stage;

}
