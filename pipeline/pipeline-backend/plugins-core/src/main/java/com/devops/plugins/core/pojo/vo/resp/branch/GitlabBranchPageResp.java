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

package com.devops.plugins.core.pojo.vo.resp.branch;

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
public class GitlabBranchPageResp  implements Serializable{

	private static final long serialVersionUID = 1L;

    /** 分支名 */
    @ApiModelProperty(notes = "应用服务标识", dataType = "String", example = "应用服务标识")
    private String appServiceId;
	/** 分支名 */
    @ApiModelProperty(notes = "分支名", dataType = "String", example = "分支名")
    private String branchName;
	/** 来源分支 */
    @ApiModelProperty(notes = "来源分支", dataType = "String", example = "来源分支")
    private String originBranch;
	/** 禅道问题id */
    @ApiModelProperty(notes = "禅道问题id", dataType = "String", example = "禅道问题id")
    private String issueId;
	/** 最新提交用户 */
    @ApiModelProperty(notes = "最新提交用户", dataType = "String", example = "最新提交用户")
    private String commitUserId;
	/** 最新提交用户名 */
    @ApiModelProperty(notes = "最新提交用户名", dataType = "String", example = "最新提交用户名")
    private String commitUserName;
	/** 最新提交内容 */
    @ApiModelProperty(notes = "最新提交内容", dataType = "String", example = "最新提交内容")
    private String commitMsg;
	/** 最新提交容器 */
    @ApiModelProperty(notes = "最新提交时间", dataType = "Date", example = "最新提交时间")
    private Date commitDate;
	/** 最新提交sha */
    @ApiModelProperty(notes = "最新提交sha", dataType = "String", example = "最新提交sha")
    private String commitSha;

}
