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

package com.devops.plugins.core.utils;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.devops.plugins.common.constraint.GitlabCIConstraint;
import com.devops.plugins.core.pojo.vo.req.customPipeline.CustomPipelineDetailReq;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineStageResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.gitlabci.*;
import com.devops.plugins.core.pojo.yaml.FieldOrderPropertyUtil;
import com.devops.plugins.core.pojo.yaml.SkipNullAndUnwrapMapRepresenter;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.util.*;

/**
 * @author sheep
 * @create 2022-01-07 14:50
 */
public class YamlUtil {


    private final static DumperOptions OPTIONS = new DumperOptions();
    private final static Representer REPRESENTER = new SkipNullAndUnwrapMapRepresenter();

    static {
        //设置yaml读取方式为块读取
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        OPTIONS.setAllowReadOnlyProperties(true);
        REPRESENTER.setPropertyUtils(new FieldOrderPropertyUtil());
    }

    public static Yaml getYaml() {
        Yaml snakeYaml = new Yaml(new Constructor(), REPRESENTER, OPTIONS);
        return snakeYaml;
    }

    public static String getGitlabCi(GitlabCi gitlabCi) {
        Yaml snakeYaml = getYaml();
        String yaml = snakeYaml.dump(gitlabCi);
        return yaml;
    }


    public static String getGitlabCi(CustomPipelineDetailReq customPipelineDetailReq) {
        GitlabCi gitlabCi = new GitlabCi();

        //添加变量
//        if (CollectionUtils.isNotEmpty(customPipelineDetailReq.getVariables())) {
//            Map<String, String> variables = new HashMap<>();
//            customPipelineDetailReq.getVariables().stream().forEach(variableVO -> {
//                variables.put(variableVO.getKey(), variableVO.getValue());
//            });
//            gitlabCi.setVariables(variables);
//        }

        //添加阶段
        LinkedList<String> stages = new LinkedList<>();
        LinkedHashMap<String, CiJob> jobs = new LinkedHashMap<>();

        if (CollectionUtils.isNotEmpty(customPipelineDetailReq.getStages())) {
            customPipelineDetailReq.getStages().stream().forEach(customPipelineStageVO -> {
                CiJob ciJob = new CiJob();
                ciJob.setImage(customPipelineStageVO.getImage());
                ciJob.setTags(customPipelineStageVO.getTags());
                //设置ci阶段脚本
                setScript(customPipelineStageVO, ciJob);
                //设置ci阶段制品
                setArtifacts(customPipelineStageVO, ciJob);
//                //设置ci阶段缓存
                setCache(customPipelineStageVO, ciJob);
                //设置ci阶段except策略
                setExceptPolicy(customPipelineStageVO, ciJob);
                //设置ci阶段only策略
                setOnlyPolicy(customPipelineStageVO, ciJob);
                ciJob.setStage(customPipelineStageVO.getCode());
                stages.add(ciJob.getStage());
                jobs.put(ciJob.getStage(), ciJob);
            });
        }

        gitlabCi.setStages(stages);
        gitlabCi.setJobs(jobs);

        Yaml snakeYaml = getYaml();
        String yaml = snakeYaml.dump(gitlabCi);
        return yaml;
    }

    private static void setOnlyPolicy(CustomPipelineStageResp customPipelineStageResp, CiJob ciJob) {
        OnlyExceptPolicy onlyPolicy = customPipelineStageResp.getOnly() == null ? new OnlyExceptPolicy() : customPipelineStageResp.getOnly();
        List<String> variables;
        if (CollectionUtils.isEmpty(onlyPolicy.getVariables())) {
            variables = Arrays.asList("${{auto.build}} == \"true\"");
        } else {
            List<String> existVariables = onlyPolicy.getVariables();
            StringBuilder stringBuilder = new StringBuilder("${{auto.build}} == \"true\"");
            for (String variable : existVariables) {
                if (StringUtils.isBlank(variable)) {
                    continue;
                }
                stringBuilder.append(" && " + variable);
            }
            variables = Arrays.asList(stringBuilder.toString());
        }
        if (CollectionUtils.isEmpty(onlyPolicy.getRefs())) {
            onlyPolicy.setRefs(null);
        } else {
            List<String> newRefs = new ArrayList<>();
            for (String ref : onlyPolicy.getRefs()) {
                if (StringUtils.isBlank(ref)) {
                    continue;
                } else {
                    newRefs.add(ref);
                }
            }
            onlyPolicy.setRefs(CollectionUtils.isEmpty(newRefs) ? null : newRefs);
        }
        onlyPolicy.setVariables(variables);
        ciJob.setOnly(onlyPolicy);
    }

    private static void setExceptPolicy(CustomPipelineStageResp customPipelineStageResp, CiJob ciJob) {
        OnlyExceptPolicy exceptPolicy = customPipelineStageResp.getExcept();
        if (exceptPolicy != null) {
            if (CollectionUtils.isNotEmpty(exceptPolicy.getVariables())) {
                List<String> existVariables = exceptPolicy.getVariables();
                StringBuilder stringBuilder = new StringBuilder("");
                for (String variable : existVariables) {
                    if (StringUtils.isBlank(variable)) {
                        continue;
                    }
                    if (stringBuilder.toString().equals("")) {
                        stringBuilder.append(variable);
                    } else {
                        stringBuilder.append(" && " + variable);
                    }
                }
                if (stringBuilder.toString().equals("")) {
                    exceptPolicy.setVariables(null);
                } else {
                    exceptPolicy.setVariables(Arrays.asList(stringBuilder.toString()));
                }
            } else {
                exceptPolicy.setVariables(null);
            }
            if (CollectionUtils.isEmpty(exceptPolicy.getRefs())) {
                exceptPolicy.setRefs(null);
            } else {
                List<String> newRefs = new ArrayList<>();
                for (String ref : exceptPolicy.getRefs()) {
                    if (StringUtils.isBlank(ref)) {
                        continue;
                    } else {
                        newRefs.add(ref);
                    }
                }
                exceptPolicy.setRefs(CollectionUtils.isEmpty(newRefs) ? null : newRefs);
            }
            if (exceptPolicy.getVariables() == null && exceptPolicy.getRefs() == null) {
                ciJob.setExcept(null);
            } else {
                ciJob.setExcept(exceptPolicy);
            }
        }
    }

    private static void setCache(CustomPipelineStageResp customPipelineStageResp, CiJob ciJob) {
        Cache cache = customPipelineStageResp.getCache();
        if (cache != null) {
            if (CollectionUtils.isEmpty(cache.getPaths())) {
                cache.setPaths(null);
            } else {
                List<String> newPaths = new ArrayList<>();
                for (String path : cache.getPaths()) {
                    if (StringUtils.isBlank(path)) {
                        continue;
                    } else {
                        newPaths.add(path);
                    }
                }
                cache.setPaths(newPaths);
            }
//            cache.setKey("$CI_COMMIT_REF_SLUG");
            ciJob.setCache(cache);
        }
    }

    private static void setArtifacts(CustomPipelineStageResp customPipelineStageResp, CiJob ciJob) {
        Artifacts artifacts = customPipelineStageResp.getArtifacts();
        if (artifacts != null) {
            artifacts.setExpireIn(artifacts.getTime());
            artifacts.setTime(null);
            if (CollectionUtils.isEmpty(artifacts.getPaths())) {
                artifacts.setPaths(null);
            } else {
                List<String> newPaths = new ArrayList<>();
                for (String path : artifacts.getPaths()) {
                    if (StringUtils.isBlank(path)) {
                        continue;
                    } else {
                        newPaths.add(path);
                    }
                }
                artifacts.setPaths(newPaths);
            }
            ciJob.setArtifacts(artifacts);
        }
    }

    private static void setScript(CustomPipelineStageResp customPipelineStageResp, CiJob ciJob) {
        ciJob.setScript(customPipelineStageResp.getScript());
    }
}
