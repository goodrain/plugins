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

package com.devops.plugins.core.pojo.harbor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sheep
 * @create 2022-01-10 11:28
 */
@Getter
@Setter
public class HarborProjectVO {

    @JsonProperty("project_name")
    private String project_name;

    private MetaData metadata;

    public HarborProjectVO(String project_name, String publicLevel) {
        this.project_name = project_name;
        MetaData metaData = new MetaData();
        metaData.setHarborPublic(publicLevel);
        this.metadata = metaData;
    }

    public HarborProjectVO(){}



}
