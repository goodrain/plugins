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

package com.devops.plugins.core.pojo.vo.resp.appServicePipeline;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author sheep
 * @create 2022-01-13 10:58
 */

@Getter
@Setter
public class AppServicePipelineStageResp {

    public String id;
    public String name;
    public String status;
    public Date startTime;
    public List<AppServicePipelineJobResp> jobs;


    public static int compare(AppServicePipelineStageResp o1, AppServicePipelineStageResp o2) {
        if (o1.getStartTime() == null || o2.getStartTime() == null) {
            return -1;
        }
        if (o1.getStartTime().compareTo(o2.getStartTime()) > 0) {
            return 1;
        }
        if (o1.getStartTime() == o2.getStartTime()) {
            return 0;
        }
        return 0;
    }

}
