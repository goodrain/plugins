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
import com.devops.plugins.core.pojo.yaml.annotation.YamlUnwrapped;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档链接  https://docs.gitlab.com/ee/ci/yaml/README.html
 */
@Getter
@Setter
public class GitlabCi {
    @ApiModelProperty("Url to include external yaml from")
    private String include;

    @ApiModelProperty("The image for jobs")
    private String image;

    @ApiModelProperty("stage is defined per-job and relies on stages which is defined globally. It allows to group jobs into different stages, and jobs of the same stage are executed in parallel (subject to certain conditions).")
    private List<String> stages;

    private Map<String, String> variables;

    // 用linkedHashMap的原因是保证job的遍历顺序
    @YamlUnwrapped
    @JsonUnwrapped
    @ApiModelProperty("Job")
    private LinkedHashMap<String, CiJob> jobs;

    @YamlProperty(value = "before_script")
    @JsonProperty("before_script")
    @ApiModelProperty("before_script")
    private List<String> beforeScript;


    @JsonAnySetter
    public void addJob(String name, CiJob ciJob) {
        if (jobs == null) {
            jobs = new LinkedHashMap<>();
        }
        jobs.put(name, ciJob);
    }
}
