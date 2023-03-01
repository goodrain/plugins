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
 * gitlab的commit表-实体类
 *
 * @author sheep
 * @Date 2022-01-11 15:54:46
 */
@Getter
@Setter
@TableName("devops_gitlab_branch")
public class GitlabBranchEntity extends CommonEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 应用服务用id */
    @TableField(value = "app_service_id")
    @ApiModelProperty(notes = "应用服务用id", dataType = "String", example = "应用服务用id")
    private String appServiceId;
	/** 用户id */
    @TableField(value = "user_id")
    @ApiModelProperty(notes = "用户id", dataType = "String", example = "用户id")
    private String userId;
	/** 分支名 */
    @TableField(value = "branch_name")
    @ApiModelProperty(notes = "分支名", dataType = "String", example = "分支名")
    private String branchName;
	/** 来源分支 */
    @TableField(value = "origin_branch")
    @ApiModelProperty(notes = "来源分支", dataType = "String", example = "来源分支")
    private String originBranch;
	/** 禅道问题id */
    @TableField(value = "issue_id")
    @ApiModelProperty(notes = "禅道问题id", dataType = "String", example = "禅道问题id")
    private String issueId;
	/** 最新提交用户 */
    @TableField(value = "commit_user_id")
    @ApiModelProperty(notes = "最新提交用户", dataType = "String", example = "最新提交用户")
    private String commitUserId;
	/** 最新提交用户名 */
    @TableField(value = "commit_user_name")
    @ApiModelProperty(notes = "最新提交用户名", dataType = "String", example = "最新提交用户名")
    private String commitUserName;
	/** 最新提交内容 */
    @TableField(value = "commit_msg")
    @ApiModelProperty(notes = "最新提交内容", dataType = "String", example = "最新提交内容")
    private String commitMsg;
	/** 最新提交容器 */
    @TableField(value = "commit_date")
    @ApiModelProperty(notes = "最新提交容器", dataType = "Date", example = "最新提交容器")
    private Date commitDate;
	/** 最新提交sha */
    @TableField(value = "commit_sha")
    @ApiModelProperty(notes = "最新提交sha", dataType = "String", example = "最新提交sha")
    private String commitSha;
}
