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

package com.devops.plugins.core.pojo.vo.resp.customPipeline;

import com.devops.plugins.core.pojo.vo.resp.customPipeline.gitlabci.Artifacts;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.gitlabci.Cache;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.gitlabci.OnlyExceptPolicy;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomPipelineStageResp {


    private String id;
    private String tempId;
    @ApiModelProperty("阶段的镜像")
    private String image;
    @ApiModelProperty("阶段编码")
    private String code;
    @ApiModelProperty("阶段编码")
    private String name;
    @ApiModelProperty("包含的脚本")
    private List<String> script;
    @ApiModelProperty("标签")
    private List<String> tags;
    @ApiModelProperty("匹配条件")
    private OnlyExceptPolicy only;
    @ApiModelProperty("排除条件")
    private OnlyExceptPolicy except;
    @ApiModelProperty("制品")
    private Artifacts artifacts;
    @ApiModelProperty("缓存")
    private Cache cache;



}
