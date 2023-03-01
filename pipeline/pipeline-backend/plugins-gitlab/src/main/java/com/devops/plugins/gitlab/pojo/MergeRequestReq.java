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

package com.devops.plugins.gitlab.pojo;

import lombok.Getter;
import lombok.Setter;
import org.gitlab4j.api.Constants;

import java.util.List;

/**
 * MergeRequest
 *
 */
@Getter
@Setter
public class MergeRequestReq {
    private String sourceBranch;
    private String targetBranch;
    private String title;
    private Integer assigneeId;
    private List<Integer> assigneeIds;
    private Integer milestoneId;
    private List<String> labels;
    private String description;
    private Integer targetProjectId;
    private Constants.StateEvent stateEvent;
    private Boolean removeSourceBranch;
    private Boolean squash;
    private Boolean discussionLocked;
    private Boolean allowCollaboration;
    private Integer approvalsBeforeMerge;

}
