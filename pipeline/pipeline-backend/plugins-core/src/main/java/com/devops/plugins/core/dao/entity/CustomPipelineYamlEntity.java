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
 * 自定义流水线表-实体类
 *
 * @author sheep
 * @Date 2022-08-17 09:17:05
 */
@Getter
@Setter
@ApiModel(description = "自定义流水线表")
@TableName("devops_custom_pipeline_yaml")
public class CustomPipelineYamlEntity extends CommonEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 流水线id */
    @TableField(value = "pipeline_id")
    @ApiModelProperty(notes = "流水线id", example = "流水线id", position = 1)
    private String pipelineId;
	/** yaml */
    @ApiModelProperty(notes = "yaml", example = "yaml", position = 2)
    private String yaml;


}
