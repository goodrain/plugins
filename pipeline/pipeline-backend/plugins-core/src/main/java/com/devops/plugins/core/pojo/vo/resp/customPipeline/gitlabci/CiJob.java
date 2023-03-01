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

package com.devops.plugins.core.pojo.vo.resp.customPipeline.gitlabci;

import com.devops.plugins.core.pojo.yaml.annotation.YamlProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 */
@Getter
@Setter
public class CiJob {
    @ApiModelProperty("job的镜像")
    private String image;
    @ApiModelProperty("所属stage")
    private String stage;
    @ApiModelProperty("job的并发数")
    private Integer parallel;
    @ApiModelProperty("ci里面的services")
    private List<CiJobServices> services;
    @YamlProperty(value = "after_script")
    @JsonProperty("after_script")
    @ApiModelProperty("after_script")
    private List<String> afterScript;
    @ApiModelProperty("变量")
    private Map<String, String> variables;
    @ApiModelProperty("包含的脚本")
    private List<String> script;
    @ApiModelProperty("标签")
    private List<String> tags;
    @ApiModelProperty("匹配条件")
    private OnlyExceptPolicy only;
    @ApiModelProperty("排除条件")
    private OnlyExceptPolicy except;
    @ApiModelProperty("缓存配置")
    private Cache cache;
    @ApiModelProperty("制品")
    private Artifacts artifacts;
}
