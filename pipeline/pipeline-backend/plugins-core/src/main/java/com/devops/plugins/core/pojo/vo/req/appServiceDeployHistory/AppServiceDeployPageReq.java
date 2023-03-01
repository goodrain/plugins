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

package com.devops.plugins.core.pojo.vo.req.appServiceDeployHistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * -实体类
 *
 * @author sheep
 * @Date 2022-03-03 17:12:42
 */
@Getter
@Setter
public class AppServiceDeployPageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 子模块名称
     *
     */
    @ApiModelProperty(notes = "应用服务标识", dataType = "String", example = "应用服务标识")
    private String appServiceId;


    /**
     * 子模块名称
     *
     */
    @ApiModelProperty(notes = "环境编码", dataType = "String", example = "环境编码")
    private String regionCode;



    /**
     * 子模块名称
     *
     */
    @ApiModelProperty(notes = "子模块名称", dataType = "String", example = "子模块名称")
    private String subService;

}
