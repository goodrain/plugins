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
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.core.dao.AppServiceMapper;
import com.devops.plugins.core.dao.AppServicePipelineEnvsMapper;
import com.devops.plugins.core.dao.GitlabCommitMapper;
import com.devops.plugins.core.dao.SubAppServiceMapper;
import com.devops.plugins.core.dao.entity.AppServiceEntity;
import com.devops.plugins.core.dao.entity.AppServicePipelineEnvsEntity;
import com.devops.plugins.core.dao.entity.GitlabCommitEntity;
import com.devops.plugins.core.dao.entity.SubAppServiceEntity;
import com.devops.plugins.core.pojo.gitlab.Commit;
import com.devops.plugins.core.pojo.gitlab.PushWebHook;
import com.devops.plugins.core.pojo.vo.req.appServicePipeline.CiReq;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.PaasVariableResp;
import com.devops.plugins.core.service.IAppServicePipelineService;
import com.devops.plugins.core.service.IGitlabCommitService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * gitlab的commit表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-01-11 14:30:34
 */
@Service
@Transactional
public class GitlabCommitServiceImpl extends ServiceImpl<GitlabCommitMapper, GitlabCommitEntity> implements IGitlabCommitService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource
    private GitlabCommitMapper gitlabCommitMapper;
    @Autowired
    private AppServiceMapper appServiceMapper;
    @Autowired
    private IAppServicePipelineService iAppServicePipelineService;
    @Autowired
    private AppServicePipelineEnvsMapper appServicePipelineEnvsMapper;
    @Autowired
    private SubAppServiceMapper subAppServiceMapper;

    @Override
    public Long findCount(GitlabCommitEntity query) {
        return gitlabCommitMapper.findCount(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GitlabCommitEntity> findPage(GitlabCommitEntity query, Page<GitlabCommitEntity> page) {
        return gitlabCommitMapper.findPage(query, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GitlabCommitEntity> findList(GitlabCommitEntity query) {
        return gitlabCommitMapper.findList(query);
    }

    @Override
    public boolean updateByPrimaryKeySelective(GitlabCommitEntity entity) {
        return SqlHelper.retBool(gitlabCommitMapper.updateByPrimaryKeySelective(entity));
    }

    @Override
    public void webhookCommitSync(PushWebHook pushWebHook) {

        LambdaQueryWrapper<AppServiceEntity> projectIdWrapper = new LambdaQueryWrapper<>();
        projectIdWrapper.eq(AppServiceEntity::getGitlabProjectId, pushWebHook.getProjectId());
        AppServiceEntity appServiceEntity = appServiceMapper.selectOne(projectIdWrapper);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }


        String ref = pushWebHook.getRef().replaceFirst(PlatFormConstraint.REF_HEADS, "");
        if (!pushWebHook.getCommits().isEmpty()) {
            for (Commit commitVO : pushWebHook.getCommits()) {
                LambdaQueryWrapper<GitlabCommitEntity> gitCommitWrapper = new LambdaQueryWrapper<>();
                gitCommitWrapper.eq(GitlabCommitEntity::getCommitSha,  commitVO.getId()).eq(GitlabCommitEntity::getGitlabRef, ref);
                GitlabCommitEntity gitlabCommitEntity = gitlabCommitMapper.selectOne(gitCommitWrapper);

                if (gitlabCommitEntity == null) {
                    gitlabCommitEntity = new GitlabCommitEntity();
                    gitlabCommitEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                    gitlabCommitEntity.setAppServiceId(appServiceEntity.getId());
                    gitlabCommitEntity.setCommitContent(commitVO.getMessage());
                    gitlabCommitEntity.setCommitSha(commitVO.getId());
                    gitlabCommitEntity.setGitlabRef(ref);
                    gitlabCommitEntity.setUserId(pushWebHook.getUserId().toString());
                    gitlabCommitEntity.setCommitDate(commitVO.getTimestamp());
                    gitlabCommitMapper.insert(gitlabCommitEntity);




                    //自动构建逻辑
                    if (appServiceEntity.getAutoBuild() != null && appServiceEntity.getAutoBuild()) {
                        List<String> modules;
                        LambdaQueryWrapper<SubAppServiceEntity> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(SubAppServiceEntity::getAppServiceId,  appServiceEntity.getId());
                        List<SubAppServiceEntity> results = subAppServiceMapper.selectList(queryWrapper);
                        if (CollectionUtils.isEmpty(results)) {
                            modules = new ArrayList<>();
                        } else {
                            modules = results.stream().map(SubAppServiceEntity::getCode).collect(Collectors.toList());
                        }
                        if (CollectionUtils.isNotEmpty(modules)) {
                            for (String module : modules) {
                                LambdaQueryWrapper<AppServicePipelineEnvsEntity> envsEntityQueryWrapper = new LambdaQueryWrapper<>();
                                envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getAppServiceId, appServiceEntity.getId());
                                envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getBranch, ref);
                                envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getModule, module);
                                List<AppServicePipelineEnvsEntity> envs = appServicePipelineEnvsMapper.selectList(envsEntityQueryWrapper);
                                try {
                                    CiReq ciReq = new CiReq();
                                    ciReq.setAppServiceId(appServiceEntity.getId());
                                    ciReq.setBranch(ref);
                                    ciReq.setModule(module);
                                    ciReq.setVariables(CollectionUtils.isNotEmpty(envs) ? envs.stream().map(this::envEntityToVo).collect(Collectors.toList()) : new ArrayList<>());
                                    iAppServicePipelineService.runCiWithVars(ciReq);
                                } catch (Exception e) {
                                    logger.error("运行ci失败,原因:%s", e.getMessage());
                                }
                            }
                        } else {
                            LambdaQueryWrapper<AppServicePipelineEnvsEntity> envsEntityQueryWrapper = new LambdaQueryWrapper<>();
                            envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getAppServiceId, appServiceEntity.getId());
                            envsEntityQueryWrapper.eq(AppServicePipelineEnvsEntity::getBranch, ref);
                            List<AppServicePipelineEnvsEntity> envs = appServicePipelineEnvsMapper.selectList(envsEntityQueryWrapper);
                            try {
                                CiReq ciReq = new CiReq();
                                ciReq.setAppServiceId(appServiceEntity.getId());
                                ciReq.setBranch(ref);
                                ciReq.setVariables(CollectionUtils.isNotEmpty(envs) ? envs.stream().map(this::envEntityToVo).collect(Collectors.toList()) : new ArrayList<>());
                                iAppServicePipelineService.runCiWithVars(ciReq);
                            } catch (Exception e) {
                                logger.error("运行ci失败,原因:%s", e.getMessage());
                            }
                        }
                    }
                }
            }

        }
    }

    private PaasVariableResp envEntityToVo(AppServicePipelineEnvsEntity appServicePipelineEnvsEntity) {
        PaasVariableResp variableVO = new PaasVariableResp();
        variableVO.setKey(appServicePipelineEnvsEntity.getEnvKey());
        variableVO.setValue(appServicePipelineEnvsEntity.getEnvValue());
        return variableVO;
    }

}
