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

package com.devops.plugins.core.pojo.vo.req.appService;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 应用服务表-实体类
 *
 * @author sheep
 * @Date 2022-01-06 10:07:19
 */
@Getter
@Setter
public class AppServicePageReq implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 应用服务名称
     */
    @ApiModelProperty(notes = "团队标识", dataType = "String", example = "团队标识")
    private String teamId;

    /**
     * 应用服务名称
     */
    @ApiModelProperty(notes = "应用服务名称", dataType = "String", example = "应用服务名称")
    private String name;
    /**
     * 应用服务编码
     */
    @ApiModelProperty(notes = "应用服务编码", dataType = "String", example = "应用服务编码")
    private String code;
}
