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

package com.devops.plugins.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.devops.plugins.common.constraint.PlatFormConstraint;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.core.dao.*;
import com.devops.plugins.core.dao.entity.*;
import com.devops.plugins.core.pojo.gitlab.CiJobWebHook;
import com.devops.plugins.core.pojo.gitlab.PipelineWebHook;
import com.devops.plugins.core.pojo.vo.req.appServicePipeline.AppServicePipelinePageReq;
import com.devops.plugins.core.pojo.vo.req.appServicePipeline.CiReq;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelineEnvsResp;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelineJobResp;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelinePageResp;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelineStageResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.PaasVariableResp;
import com.devops.plugins.core.service.*;
import com.devops.plugins.core.utils.Base64Util;
import com.devops.plugins.core.utils.ConvertUtils;
import com.devops.plugins.gitlab.service.JobService;
import com.devops.plugins.gitlab.service.PipelineService;
import com.devops.plugins.gitlab.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.gitlab4j.api.models.Pipeline;
import org.gitlab4j.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用服务的ci流水线表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-01-11 14:36:10
 */
@Service
@Transactional
public class AppServicePipelineServiceImpl extends ServiceImpl<AppServicePipelineMapper, AppServicePipelineEntity> implements IAppServicePipelineService {

    @Resource
    private AppServicePipelineMapper appServicePipelineMapper;
    @Autowired
    private AppServiceMapper appServiceMapper;
    @Autowired
    private GitlabCommitMapper gitlabCommitMapper;
    @Autowired
    private IAppServicePipelineJobService appServicePipelineJobService;
    @Autowired
    private AppServicePipelineJobMapper appServicePipelineJobMapper;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private SubAppServiceMapper subAppServiceMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private AppServiceConfigMapper appServiceConfigMapper;
    @Autowired
    private AppServicePipelineEnvsMapper appServicePipelineEnvsMapper;
    @Autowired
    private IAppServicePipelineEnvsService appServicePipelineEnvsService;
    @Autowired
    private ICustomPipelineTempService customPipelineTempService;


    @Override
    public Long findCount(AppServicePipelineEntity query) {
        return appServicePipelineMapper.findCount(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppServicePipelinePageResp> findPage(AppServicePipelinePageReq query, ReqPage<AppServicePipelinePageReq> page) {
        Page<AppServicePipelinePageResp> result = new Page<>();

        Page<AppServicePipelineEntity> appServicePipelineEntityPage = appServicePipelineMapper.findPage(ConvertUtils.convertObject(query, AppServicePipelineEntity.class), ConvertUtils.convertReqPage(page, new Page<>()));
        List<AppServicePipelineEntity> appServicePipelineEntities = appServicePipelineEntityPage.getRecords();
        if (CollectionUtils.isNotEmpty(appServicePipelineEntities)) {
            result = ConvertUtils.convertPage(appServicePipelineEntityPage, AppServicePipelinePageResp.class);
            List<AppServicePipelinePageResp> appServicePipelinePageResps = result.getRecords();

            List<String> pipelineIds = appServicePipelinePageResps.stream().map(AppServicePipelinePageResp::getPipelineId).collect(Collectors.toList());
            Map<String, AppServicePipelinePageResp> appServicePipelineEntityMap = appServicePipelinePageResps.stream().collect(Collectors.toMap(AppServicePipelinePageResp::getPipelineId, i -> i));
            LambdaQueryWrapper<AppServicePipelineJobEntity> appServicePipelineJobWrapper = new LambdaQueryWrapper<>();
            appServicePipelineJobWrapper.in(AppServicePipelineJobEntity::getPipelineId, pipelineIds);
            Map<String, List<AppServicePipelineJobEntity>> appServicePipelineJobMap = appServicePipelineJobMapper.selectList(appServicePipelineJobWrapper).stream().collect(Collectors.groupingBy(AppServicePipelineJobEntity::getPipelineId));
            appServicePipelineJobMap.forEach((pipelineId, jobs) -> {
                Map<String, List<AppServicePipelineJobEntity>> appServicePipelineJobMapByStage = jobs.stream().collect(Collectors.groupingBy(AppServicePipelineJobEntity::getStage));
                List<AppServicePipelineStageResp> stageVOS = new ArrayList<>();
                appServicePipelineJobMapByStage.forEach((stage, jobsByStage) -> {
                    List<AppServicePipelineJobEntity> realJobsByStage = handleStageJob(jobsByStage);
                    AppServicePipelineStageResp stageVO = new AppServicePipelineStageResp();
                    stageVO.setId(realJobsByStage.get(0).getJobId());
                    stageVO.setName(stage);
                    stageVO.setJobs(ConvertUtils.convertList(realJobsByStage, AppServicePipelineJobResp.class));
                    stageVO.setStartTime(realJobsByStage.get(0).getStartTime());
                    stageVO.setStatus(getStatus(realJobsByStage));
                    stageVOS.add(stageVO);
                });
                appServicePipelineEntityMap.get(pipelineId).setStages(stageVOS.stream().sorted(Comparator.comparing(AppServicePipelineStageResp::getId)).collect(Collectors.toList()));
            });
            result.setRecords(new ArrayList(appServicePipelineEntityMap.values().stream().sorted(Comparator.comparing(AppServicePipelinePageResp::getCreateTime).reversed()).collect(Collectors.toList())));
        }
        return result;
    }


    @Override
    public Map<String, AppServicePipelinePageResp> findAppServiceLatestJobs(List<String> appServiceIds) {
        LambdaQueryWrapper<AppServicePipelineEntity> appServicePipelineEntityQueryWrapper = new LambdaQueryWrapper<>();
        appServicePipelineEntityQueryWrapper.in(AppServicePipelineEntity::getAppServiceId, appServiceIds);
        List<AppServicePipelineEntity> appServicePipelineEntities = appServicePipelineMapper.selectList(appServicePipelineEntityQueryWrapper);
        Map<String, AppServicePipelinePageResp> appServiceStage = new HashMap<>();
        if (CollectionUtils.isNotEmpty(appServicePipelineEntities)) {
            List<AppServicePipelinePageResp> appServicePipelinePageResps = ConvertUtils.convertList(appServicePipelineEntities, AppServicePipelinePageResp.class);
            Map<String, List<AppServicePipelinePageResp>> appServicePipelinesMap = appServicePipelinePageResps.stream().collect(Collectors.groupingBy(AppServicePipelinePageResp::getAppServiceId));
            List<String> pipelineIds = appServicePipelinePageResps.stream().map(AppServicePipelinePageResp::getPipelineId).collect(Collectors.toList());
            LambdaQueryWrapper<AppServicePipelineJobEntity> appServicePipelineJobWrapper = new LambdaQueryWrapper<>();
            appServicePipelineJobWrapper.in(AppServicePipelineJobEntity::getPipelineId, pipelineIds);
            Map<String, List<AppServicePipelineJobEntity>> appServicePipelineJobMap = appServicePipelineJobMapper.selectList(appServicePipelineJobWrapper).stream().collect(Collectors.groupingBy(AppServicePipelineJobEntity::getPipelineId));
            appServicePipelinesMap.forEach((appSeviceId, pipelines) -> {
                AppServicePipelinePageResp appServicePipelinePageResp = pipelines.stream().sorted(Comparator.comparing(AppServicePipelinePageResp::getCreateTime).reversed()).collect(Collectors.toList()).get(0);
                List<AppServicePipelineJobEntity> appServicePipelineJobEntities = appServicePipelineJobMap.get(appServicePipelinePageResp.getPipelineId());
                if (CollectionUtils.isNotEmpty(appServicePipelineJobEntities)) {
                    Map<String, List<AppServicePipelineJobEntity>> appServicePipelineJobMapByStage = appServicePipelineJobEntities.stream().collect(Collectors.groupingBy(AppServicePipelineJobEntity::getStage));
                    List<AppServicePipelineStageResp> stageVOS = new ArrayList<>();
                    appServicePipelineJobMapByStage.forEach((stage, jobsByStage) -> {
                        List<AppServicePipelineJobEntity> realJobsByStage = handleStageJob(jobsByStage);
                        AppServicePipelineStageResp stageVO = new AppServicePipelineStageResp();
                        stageVO.setId(realJobsByStage.get(0).getJobId());
                        stageVO.setName(stage);
                        stageVO.setJobs(ConvertUtils.convertList(realJobsByStage, AppServicePipelineJobResp.class));
                        stageVO.setStartTime(realJobsByStage.get(0).getStartTime());
                        stageVO.setStatus(getStatus(realJobsByStage));
                        stageVOS.add(stageVO);
                    });
                    appServicePipelinePageResp.setStages(stageVOS.stream().sorted(Comparator.comparing(AppServicePipelineStageResp::getId)).collect(Collectors.toList()));
                    appServiceStage.put(appSeviceId, appServicePipelinePageResp);
                }
            });
        }
        return appServiceStage;
    }

    private List<AppServicePipelineJobEntity> handleStageJob(List<AppServicePipelineJobEntity> appServicePipelineJobEntities) {
        List<AppServicePipelineJobEntity> realList = new ArrayList<>();
        Map<String, List<AppServicePipelineJobEntity>> stringListMap = appServicePipelineJobEntities.stream().collect(Collectors.groupingBy(AppServicePipelineJobEntity::getName));
        stringListMap.forEach((key, list) -> {
            if (list.size() > 1) {
                AppServicePipelineJobEntity real = list.stream().sorted(Comparator.comparing(AppServicePipelineJobEntity::getJobId).reversed()).findFirst().get();
                real.setJobId(list.stream().sorted(Comparator.comparing(AppServicePipelineJobEntity::getJobId)).findFirst().get().getJobId());
                realList.add(real);
            } else {
                realList.add(list.get(0));
            }
        });
        return realList;
    }

    private String getStatus(List<AppServicePipelineJobEntity> jobs) {
        if (jobs.stream().anyMatch(appServicePipelineJobEntity -> appServicePipelineJobEntity.getStatus().equals(PlatFormConstraint.Job_Failed))) {
            return PlatFormConstraint.Job_Failed;
        }
        if (jobs.stream().anyMatch(appServicePipelineJobEntity -> appServicePipelineJobEntity.getStatus().equals(PlatFormConstraint.Job_Skipped))) {
            return PlatFormConstraint.Job_Skipped;
        }
        if (jobs.stream().allMatch(appServicePipelineJobEntity -> appServicePipelineJobEntity.getStatus().equals(PlatFormConstraint.Job_Created))) {
            return PlatFormConstraint.Job_Created;
        }
        if (jobs.stream().allMatch(appServicePipelineJobEntity -> appServicePipelineJobEntity.getStatus().equals(PlatFormConstraint.Job_Successed))) {
            return PlatFormConstraint.Job_Successed;
        }
        if (jobs.stream().anyMatch(appServicePipelineJobEntity -> appServicePipelineJobEntity.getStatus().equals(PlatFormConstraint.Job_Running))) {
            return PlatFormConstraint.Job_Running;
        }
        if (jobs.stream().allMatch(appServicePipelineJobEntity -> appServicePipelineJobEntity.getStatus().equals(PlatFormConstraint.Job_Canceled))) {
            return PlatFormConstraint.Job_Canceled;
        }
        return PlatFormConstraint.Job_Pending;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppServicePipelineEntity> findList(AppServicePipelineEntity query) {
        return appServicePipelineMapper.findList(query);
    }

    @Override
    public boolean updateByPrimaryKeySelective(AppServicePipelineEntity entity) {
        return SqlHelper.retBool(appServicePipelineMapper.updateByPrimaryKeySelective(entity));
    }

    @Override
    @Transactional
    public synchronized void webhookPipelineSync(PipelineWebHook pipelineWebHook) {
        if(CollectionUtils.isEmpty(pipelineWebHook.getBuilds())) {
            return;
        }


        LambdaQueryWrapper<AppServiceEntity> projectIdWrapper = new LambdaQueryWrapper<>();
        projectIdWrapper.eq(AppServiceEntity::getGitlabProjectId, pipelineWebHook.getProject().getId());
        projectIdWrapper.eq(AppServiceEntity::getGitlabCodeUrl, pipelineWebHook.getProject().getGit_http_url());
        AppServiceEntity appServiceEntity = appServiceMapper.selectOne(projectIdWrapper);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }


        LambdaQueryWrapper<GitlabCommitEntity> gitCommitWrapper = new LambdaQueryWrapper<>();
        gitCommitWrapper.eq(GitlabCommitEntity::getCommitSha, pipelineWebHook.getObjectAttributes().getSha()).eq(GitlabCommitEntity::getGitlabRef, pipelineWebHook.getObjectAttributes().getRef());
        GitlabCommitEntity gitlabCommitEntity = gitlabCommitMapper.selectOne(gitCommitWrapper);

        LambdaQueryWrapper<AppServicePipelineEntity> pipeLineWrapper = new LambdaQueryWrapper<>();
        pipeLineWrapper.eq(AppServicePipelineEntity::getPipelineId, pipelineWebHook.getObjectAttributes().getId());
        AppServicePipelineEntity appServicePipelineEntity = appServicePipelineMapper.selectOne(pipeLineWrapper);

        //查询pipeline最新阶段信息
        List<AppServicePipelineJobEntity> appServicePipelineJobEntities = new ArrayList<>();
        for (CiJobWebHook ciJobWebHook : pipelineWebHook.getBuilds()) {
            AppServicePipelineJobEntity appServicePipelineJobEntity = new AppServicePipelineJobEntity();
            appServicePipelineJobEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            appServicePipelineJobEntity.setAppServiceId(appServiceEntity.getId());
            appServicePipelineJobEntity.setJobId(ciJobWebHook.getId().toString());
            appServicePipelineJobEntity.setName(ciJobWebHook.getName());
            appServicePipelineJobEntity.setStatus(ciJobWebHook.getStatus());
            appServicePipelineJobEntity.setPipelineId(pipelineWebHook.getObjectAttributes().getId().toString());
            appServicePipelineJobEntity.setStage(ciJobWebHook.getStage());
            appServicePipelineJobEntity.setEndTime(ciJobWebHook.getFinishedAt());
            appServicePipelineJobEntity.setCreateTime(ciJobWebHook.getCreatedAt());
            appServicePipelineJobEntity.setUrl(String.format("%s/-/jobs/%s", appServiceEntity.getGitlabCodeUrl().split("\\.git")[0], ciJobWebHook.getId().toString()));
            appServicePipelineJobEntities.add(appServicePipelineJobEntity);
        }

        //pipeline创建逻辑,存在则更新状态和阶段信息
        if (appServicePipelineEntity == null) {
            appServicePipelineEntity = new AppServicePipelineEntity();
            appServicePipelineEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            appServicePipelineEntity.setPipelineId(pipelineWebHook.getObjectAttributes().getId().toString());
            appServicePipelineEntity.setAppServiceId(appServiceEntity.getId());
            appServicePipelineEntity.setStatus(pipelineWebHook.getObjectAttributes().getStatus());
            appServicePipelineEntity.setStartTime(pipelineWebHook.getObjectAttributes().getCreatedAt());
            appServicePipelineEntity.setEndTime(pipelineWebHook.getObjectAttributes().getFinishedAt());
            appServicePipelineEntity.setGitlabRef(pipelineWebHook.getObjectAttributes().getRef());
            appServicePipelineEntity.setDuration(pipelineWebHook.getObjectAttributes().getDuration() == null ? null : pipelineWebHook.getObjectAttributes().getDuration().toString());
            if (gitlabCommitEntity != null) {
                // 因为这个commit应该是由push事件产生的, hook推送顺序问题
                appServicePipelineEntity.setCommitContent(gitlabCommitEntity.getCommitContent());
                appServicePipelineEntity.setSha(gitlabCommitEntity.getCommitSha());
                if (appServiceEntity.getServiceType().equals(PlatFormConstraint.SERVICE_TYPE_EXTERNAL)) {
                    LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
                    appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, appServiceEntity.getId());
                    List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
                    if (CollectionUtils.isNotEmpty(appServiceConfigEntities)) {
                        User userR = userService.queryUserByUserId(Integer.parseInt(gitlabCommitEntity.getUserId()), appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
                        if (userR != null) {
                            appServicePipelineEntity.setCommiter(userR.getName());
                        }
                    }
                }
            }
            appServicePipelineMapper.insert(appServicePipelineEntity);
            appServicePipelineJobService.saveBatch(appServicePipelineJobEntities);
        } else {
            // 流水线更新逻辑
            appServicePipelineEntity.setStatus(pipelineWebHook.getObjectAttributes().getStatus());
            appServicePipelineEntity.setEndTime(pipelineWebHook.getObjectAttributes().getFinishedAt());
            appServicePipelineEntity.setDuration(pipelineWebHook.getObjectAttributes().getDuration() == null ? null : pipelineWebHook.getObjectAttributes().getDuration().toString());
            LambdaQueryWrapper<AppServicePipelineJobEntity> appServicePipelineJobWrapper = new LambdaQueryWrapper<>();
            appServicePipelineJobWrapper.eq(AppServicePipelineJobEntity::getPipelineId, appServicePipelineEntity.getPipelineId());
            appServicePipelineJobWrapper.eq(AppServicePipelineJobEntity::getAppServiceId, appServiceEntity.getId());
            List<AppServicePipelineJobEntity> originAppServicePipelineJobEntities = appServicePipelineJobMapper.selectList(appServicePipelineJobWrapper);
            List<AppServicePipelineJobEntity> newAppServicePipelineJobEntities = new ArrayList<>();
            Map<String, AppServicePipelineJobEntity> appServicePipelineJobEntityMap = originAppServicePipelineJobEntities.stream().collect(Collectors.toMap(AppServicePipelineJobEntity::getJobId, a -> a));
            for (CiJobWebHook ciJobWebHook : pipelineWebHook.getBuilds()) {
                if (appServicePipelineJobEntityMap.containsKey(ciJobWebHook.getId().toString())) {
                    AppServicePipelineJobEntity appServicePipelineJobEntity = appServicePipelineJobEntityMap.get(ciJobWebHook.getId().toString());
                    appServicePipelineJobEntity.setStatus(ciJobWebHook.getStatus());
                    appServicePipelineJobEntity.setEndTime(ciJobWebHook.getFinishedAt());
                    newAppServicePipelineJobEntities.add(appServicePipelineJobEntity);
                } else {
                    AppServicePipelineJobEntity appServicePipelineJobEntity = new AppServicePipelineJobEntity();
                    appServicePipelineJobEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                    appServicePipelineJobEntity.setJobId(ciJobWebHook.getId().toString());
                    appServicePipelineJobEntity.setName(ciJobWebHook.getName());
                    appServicePipelineJobEntity.setAppServiceId(appServiceEntity.getId());
                    appServicePipelineJobEntity.setStatus(ciJobWebHook.getStatus());
                    appServicePipelineJobEntity.setPipelineId(pipelineWebHook.getObjectAttributes().getId().toString());
                    appServicePipelineJobEntity.setStage(ciJobWebHook.getStage());
                    appServicePipelineJobEntity.setStartTime(ciJobWebHook.getStartedAt());
                    appServicePipelineJobEntity.setEndTime(ciJobWebHook.getFinishedAt());
                    appServicePipelineJobEntity.setCreateTime(ciJobWebHook.getCreatedAt());
                    newAppServicePipelineJobEntities.add(appServicePipelineJobEntity);
                }
            }


            if (gitlabCommitEntity != null) {
                appServicePipelineEntity.setCommitContent(gitlabCommitEntity.getCommitContent());
                appServicePipelineEntity.setSha(gitlabCommitEntity.getCommitSha());
                if (appServiceEntity.getServiceType().equals(PlatFormConstraint.SERVICE_TYPE_EXTERNAL)) {
                    LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
                    appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, appServiceEntity.getId());
                    List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
                    if (CollectionUtils.isNotEmpty(appServiceConfigEntities)) {
                        User userR = userService.queryUserByUserId(Integer.parseInt(gitlabCommitEntity.getUserId()), appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
                        if (userR != null) {
                            appServicePipelineEntity.setCommiter(userR.getName());
                        }
                    }
                }
            }
            appServicePipelineMapper.updateByPrimaryKeySelective(appServicePipelineEntity);
            appServicePipelineJobService.saveOrUpdateBatch(newAppServicePipelineJobEntities);
        }
    }

    @Override
    public String getJobLog(String pipelineId, String jobId) {
        LambdaQueryWrapper<AppServicePipelineEntity> pipeLineWrapper = new LambdaQueryWrapper<>();
        pipeLineWrapper.eq(AppServicePipelineEntity::getPipelineId, pipelineId);
        AppServicePipelineEntity appServicePipelineEntity = appServicePipelineMapper.selectOne(pipeLineWrapper);
        if (appServicePipelineEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "流水线不存在");
        }

        AppServiceEntity appServiceEntity = appServiceMapper.selectById(appServicePipelineEntity.getAppServiceId());
        if (appServicePipelineEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }
        return "";
    }

    @Override
    public void runCi(String appServiceId, String branch, String module) {
        CiReq ciReq = new CiReq();
        ciReq.setAppServiceId(appServiceId);
        ciReq.setBranch(branch);
        ciReq.setModule(module);
        ciReq.setVariables(new ArrayList<>());
        runCiWithVars(ciReq);
    }

    @Override
    public void runCiWithVars(CiReq ciReq) {
        //微服务类型应用服务可以控制某一子服务执行ci任务
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(ciReq.getAppServiceId());
        Map<String, String> variables = new HashMap<>();
        //添加自定义的环境变量
        if (CollectionUtils.isNotEmpty(ciReq.getVariables())) {
            ciReq.getVariables().stream().forEach(variableVO -> variables.put(variableVO.getKey(), variableVO.getValue()));
        }


        String type = customPipelineTempService.getPiPelineTempName(appServiceEntity.getId(), null);
        if (type != null && type.equals(PlatFormConstraint.AppService_Template_Microservice)) {
            LambdaQueryWrapper<SubAppServiceEntity> appServiceIdWrapper = new LambdaQueryWrapper<>();
            appServiceIdWrapper.eq(SubAppServiceEntity::getAppServiceId, appServiceEntity.getId());
            List<SubAppServiceEntity> subAppServiceEntities = subAppServiceMapper.selectList(appServiceIdWrapper);

            //初始化部分变量
            variables.put(appServiceEntity.getCode().replace("-", "_"), "true");
            variables.put("MODULE", ciReq.getModule().toLowerCase());
            variables.put("SUB_SERVICE", ciReq.getModule());

            if (appServiceEntity.getPipelineType().equals(PlatFormConstraint.PIPELINE_TYPE_INTENAL)) {
                subAppServiceEntities.forEach(subAppServiceEntity -> {
                    if (subAppServiceEntity.getCode().equals(ciReq.getModule())) {
                        variables.put(subAppServiceEntity.getCode().replace("-", "_"), "true");
                    } else {
                        variables.put(subAppServiceEntity.getCode().replace("-", "_"), "false");
                    }
                });
            }
        } else {
            variables.put(appServiceEntity.getCode().replace("-", "_"), "true");
            variables.put("MODULE", appServiceEntity.getCode().toLowerCase());
        }
        //添加版本的环境变量
        LambdaQueryWrapper<GitlabCommitEntity> gitlabCommitEntityQueryWrapper = new  LambdaQueryWrapper<>();
        gitlabCommitEntityQueryWrapper.eq(GitlabCommitEntity::getAppServiceId, appServiceEntity.getId());
        gitlabCommitEntityQueryWrapper.eq(GitlabCommitEntity::getGitlabRef, ciReq.getBranch());
        List<GitlabCommitEntity> gitlabCommitEntities = gitlabCommitMapper.selectList(gitlabCommitEntityQueryWrapper);
        if (CollectionUtils.isNotEmpty(gitlabCommitEntities)) {
            GitlabCommitEntity latest = gitlabCommitEntities.stream().sorted(Comparator.comparing(GitlabCommitEntity::getCommitDate).reversed()).collect(Collectors.toList()).get(0);
            variables.put(PlatFormConstraint.DEVOPS_VERSION, String.format("%s.%s.%s-%s%s%s-%s", latest.getCommitDate().getYear() + 1900, latest.getCommitDate().getMonth() + 1, latest.getCommitDate().getDate(), latest.getCommitDate().getHours() < 10 ? "0" + latest.getCommitDate().getHours() : latest.getCommitDate().getHours(), latest.getCommitDate()
                    .getMinutes() < 10 ? "0" + latest.getCommitDate().getMinutes() : latest.getCommitDate().getMinutes(), latest.getCommitDate().getSeconds() < 10 ? "0" + latest.getCommitDate().getSeconds() : latest.getCommitDate().getSeconds(), ciReq.getBranch().replace("/","-")));
            variables.put(PlatFormConstraint.DEVOPS_BRANCH, ciReq.getBranch());
        }
        //执行流水线
        LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
        appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, ciReq.getAppServiceId());
        List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
        Pipeline pipelineR;
        if (CollectionUtils.isNotEmpty(appServiceConfigEntities)) {
            pipelineR = pipelineService.createPipeline(Integer.parseInt(appServiceEntity.getGitlabProjectId()), PlatFormConstraint.GITLAB_ROOT_USER_ID, appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() != null ? Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()) : null, ciReq.getBranch(), variables);
        } else {
            pipelineR = pipelineService.createPipeline(Integer.parseInt(appServiceEntity.getGitlabProjectId()), PlatFormConstraint.GITLAB_ROOT_USER_ID, null, null, null, null, ciReq.getBranch(), variables);

        }
        if (pipelineR == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "流水线触发失败");

        }

        //处理环境变量
        envsToDatabase(ciReq);
    }

    private void envsToDatabase(CiReq ciReq) {
        LambdaQueryWrapper<AppServicePipelineEnvsEntity> envsEntityQueryWrapper = new LambdaQueryWrapper<>();
        envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getAppServiceId, ciReq.getAppServiceId());
        envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getBranch, ciReq.getBranch());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(ciReq.getModule())) {
            envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getModule, ciReq.getModule());
        }
        List<AppServicePipelineEnvsEntity> exists = appServicePipelineEnvsMapper.selectList(envsEntityQueryWrapper);
        List<AppServicePipelineEnvsEntity> adds = new ArrayList<>();
        List<AppServicePipelineEnvsEntity> updates = new ArrayList<>();
        List<String> deletes = new ArrayList<>();
        if (CollectionUtils.isEmpty(exists) && CollectionUtils.isNotEmpty(ciReq.getVariables())) {
            ciReq.getVariables().stream().forEach(variableVO -> {
                AppServicePipelineEnvsEntity appServicePipelineEnvsEntity = new AppServicePipelineEnvsEntity();
                appServicePipelineEnvsEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                appServicePipelineEnvsEntity.setEnvKey(variableVO.getKey());
                appServicePipelineEnvsEntity.setEnvValue(variableVO.getValue());
                appServicePipelineEnvsEntity.setDescription(variableVO.getDesc());
                appServicePipelineEnvsEntity.setAppServiceId(ciReq.getAppServiceId());
                appServicePipelineEnvsEntity.setBranch(ciReq.getBranch());
                appServicePipelineEnvsEntity.setModule(ciReq.getModule());
                adds.add(appServicePipelineEnvsEntity);
            });
        }
        if (CollectionUtils.isEmpty(ciReq.getVariables()) && CollectionUtils.isNotEmpty(exists)) {
            deletes.addAll(exists.stream().map(AppServicePipelineEnvsEntity::getId).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(ciReq.getVariables()) && CollectionUtils.isNotEmpty(exists)) {
            List<String> existKeys = exists.stream().map(AppServicePipelineEnvsEntity::getEnvKey).collect(Collectors.toList());
            List<String> currentKeys = ciReq.getVariables().stream().map(PaasVariableResp::getKey).collect(Collectors.toList());
            Map<String, PaasVariableResp> variableMap = ciReq.getVariables().stream().collect(Collectors.toMap(PaasVariableResp::getKey, Function.identity()));
            exists.stream().forEach(appServicePipelineEnvsEntity -> {
                if (!currentKeys.contains(appServicePipelineEnvsEntity.getEnvKey())) {
                    deletes.add(appServicePipelineEnvsEntity.getId());
                } else {
                    appServicePipelineEnvsEntity.setEnvValue(variableMap.get(appServicePipelineEnvsEntity.getEnvKey()).getValue());
                    appServicePipelineEnvsEntity.setDescription(variableMap.get(appServicePipelineEnvsEntity.getEnvKey()).getDesc());
                    updates.add(appServicePipelineEnvsEntity);
                }
            });
            ciReq.getVariables().stream().forEach(variableVO -> {
                if (!existKeys.contains(variableVO.getKey())) {
                    AppServicePipelineEnvsEntity appServicePipelineEnvsEntity = new AppServicePipelineEnvsEntity();
                    appServicePipelineEnvsEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                    appServicePipelineEnvsEntity.setEnvKey(variableVO.getKey());
                    appServicePipelineEnvsEntity.setEnvValue(variableVO.getValue());
                    appServicePipelineEnvsEntity.setDescription(variableVO.getDesc());
                    appServicePipelineEnvsEntity.setAppServiceId(ciReq.getAppServiceId());
                    appServicePipelineEnvsEntity.setBranch(ciReq.getBranch());
                    appServicePipelineEnvsEntity.setModule(ciReq.getModule());
                    adds.add(appServicePipelineEnvsEntity);
                }
            });
        }
        if (CollectionUtils.isNotEmpty(adds)) {
            appServicePipelineEnvsService.saveBatch(adds);
        }
        if (CollectionUtils.isNotEmpty(updates)) {
            appServicePipelineEnvsService.saveOrUpdateBatch(updates);
        }
        if (CollectionUtils.isNotEmpty(deletes)) {
            appServicePipelineEnvsMapper.deleteBatchIds(deletes);
        }
    }

    @Override
    public List<AppServicePipelineEnvsResp> getEnvs(String appServiceId, String branch, String module) {

        LambdaQueryWrapper<AppServicePipelineEnvsEntity> envsEntityQueryWrapper = new LambdaQueryWrapper<>();
        envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getAppServiceId, appServiceId);
        envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getBranch, branch);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(module)) {
            envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getModule, module);
        }
        List<AppServicePipelineEnvsEntity> envs = appServicePipelineEnvsMapper.selectList(envsEntityQueryWrapper);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(module)) {
            if (envs.stream().noneMatch(appServicePipelineEnvsEntity -> appServicePipelineEnvsEntity.getEnvKey().equals("package_path"))) {
                AppServicePipelineEnvsEntity appServicePipelineEnvsEntity = new AppServicePipelineEnvsEntity();
                appServicePipelineEnvsEntity.setEnvKey("package_path");
                envs.add(appServicePipelineEnvsEntity);
            }
            if (envs.stream().noneMatch(appServicePipelineEnvsEntity -> appServicePipelineEnvsEntity.getEnvKey().equals("jar_name"))) {
                AppServicePipelineEnvsEntity appServicePipelineEnvsEntity = new AppServicePipelineEnvsEntity();
                appServicePipelineEnvsEntity.setEnvKey("jar_name");
                envs.add(appServicePipelineEnvsEntity);
            }
            return ConvertUtils.convertList(envs, AppServicePipelineEnvsResp.class);
        } else {
            return ConvertUtils.convertList(envs, AppServicePipelineEnvsResp.class);
        }
    }

}
