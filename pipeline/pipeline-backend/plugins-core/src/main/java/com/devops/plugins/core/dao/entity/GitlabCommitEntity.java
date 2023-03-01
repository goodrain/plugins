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
 * gitlab的commit表-实体类
 *
 * @author sheep
 * @Date 2022-01-11 14:30:34
 */
@Getter
@Setter
@TableName("devops_gitlab_commit")
public class GitlabCommitEntity extends CommonEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 应用服务用id */
    @TableField(value = "app_service_id")
    @ApiModelProperty(notes = "应用服务用id", dataType = "String", example = "应用服务用id")
    private String appServiceId;
	/** gitlab用户id */
    @TableField(value = "user_id")
    @ApiModelProperty(notes = "gitlab用户id", dataType = "String", example = "gitlab用户id")
    private String userId;
	/** commit_sha */
    @TableField(value = "commit_sha")
    @ApiModelProperty(notes = "commit_sha", dataType = "String", example = "commit_sha")
    private String commitSha;
	/** 提交内容 */
    @TableField(value = "commit_content")
    @ApiModelProperty(notes = "提交内容", dataType = "String", example = "提交内容")
    private String commitContent;
	/** gitlab分支 */
    @TableField(value = "gitlab_ref")
    @ApiModelProperty(notes = "gitlab分支", dataType = "String", example = "gitlab分支")
    private String gitlabRef;
	/** 提交日期 */
    @TableField(value = "commit_date")
    @ApiModelProperty(notes = "提交日期", dataType = "Date", example = "提交日期")
    private Date commitDate;


    public Timestamp modifyTime(Date create_time){
        long time = create_time.getTime() / 1000L;
        long daySecond = 60 * 60 * 24;
        long dayTime = time - (time + 8 * 3600) % daySecond;
        return new Timestamp(dayTime * 1000);
    }
}
