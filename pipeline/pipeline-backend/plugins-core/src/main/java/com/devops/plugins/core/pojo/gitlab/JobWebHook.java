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

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JobWebHook {

    private String sha;
    private String ref;
    private String project_id;
    private Long buildId;
    private String buildName;
    private String buildStage;
    private String buildStatus;
    private Date buildStartedAt;
    private Date buildFinishedAt;
    private Long buildDuration;

    private JobCommit commit;
    private Repository repository;


    @Override
    public String toString() {
        return "JobWebHookVO{" +
                "sha='" + sha + '\'' +
                ", ref='" + ref + '\'' +
                ", buildName='" + buildName + '\'' +
                ", buildStage='" + buildStage + '\'' +
                ", buildStatus='" + buildStatus + '\'' +
                ", commit=" + commit +
                '}';
    }
}
