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

package com.devops.plugins.core.pojo.gitlab;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author sheep
 * @create 2022-01-11 10:20
 */

@Getter
@Setter
public class PushWebHook {
    private String objectKind;
    private String eventName;
    private String before;
    private String after;
    private String ref;
    private String checkoutSha;
    private Integer userId;
    private String userName;
    private String userUserName;
    private Integer projectId;
    private List<Commit> commits;
    private Integer totalCommitsCount;
    private String token;
    @ApiModelProperty("之前的解析是否有错")
    private Boolean hasErrors;


    @Override
    public String toString() {
        return "PushWebHookVO{" +
                "objectKind='" + objectKind + '\'' +
                ", eventName='" + eventName + '\'' +
                ", before='" + before + '\'' +
                ", after='" + after + '\'' +
                ", ref='" + ref + '\'' +
                ", checkoutSha='" + checkoutSha + '\'' +
                ", userId=" + userId +
                ", userUserName='" + userUserName + '\'' +
                ", projectId=" + projectId +
                ", commits=" + commits +
                ", totalCommitsCount=" + totalCommitsCount +
                ", token='" + token + '\'' +
                ", hasErrors=" + hasErrors +
                '}';
    }
}
