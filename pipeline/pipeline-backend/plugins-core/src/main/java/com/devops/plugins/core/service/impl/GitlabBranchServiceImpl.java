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
import com.devops.plugins.core.dao.AppServiceMapper;
import com.devops.plugins.core.dao.GitlabBranchMapper;
import com.devops.plugins.core.dao.GitlabCommitMapper;
import com.devops.plugins.core.dao.entity.AppServiceEntity;
import com.devops.plugins.core.dao.entity.GitlabBranchEntity;
import com.devops.plugins.core.dao.entity.GitlabCommitEntity;
import com.devops.plugins.core.pojo.gitlab.Commit;
import com.devops.plugins.core.pojo.gitlab.PushWebHook;
import com.devops.plugins.core.pojo.vo.req.branch.GitlabBranchAddReq;
import com.devops.plugins.core.pojo.vo.req.branch.GitlabBranchPageReq;
import com.devops.plugins.core.pojo.vo.resp.branch.GitlabBranchPageResp;
import com.devops.plugins.core.service.IGitlabBranchService;
import com.devops.plugins.core.service.IGitlabCommitService;
import com.devops.plugins.core.utils.ConvertUtils;
import com.devops.plugins.gitlab.service.RepositoryService;
import org.apache.commons.collections.CollectionUtils;
import org.gitlab4j.api.models.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * gitlab的commit表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-01-11 14:33:09
 */
@Service
@Transactional
public class GitlabBranchServiceImpl extends ServiceImpl<GitlabBranchMapper, GitlabBranchEntity> implements IGitlabBranchService {


    @Autowired
    private IGitlabCommitService gitlabCommitService;
    @Autowired
    private GitlabBranchMapper gitlabBranchMapper;
    @Autowired
    private AppServiceMapper appServiceMapper;
    @Autowired
    private GitlabCommitMapper gitlabCommitMapper;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public Long findCount(GitlabBranchEntity query) {
        return gitlabBranchMapper.findCount(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GitlabBranchPageResp> findPage(GitlabBranchPageReq query, ReqPage<GitlabBranchPageReq> page) {
        Page<GitlabBranchPageResp> result = new Page<>();
        Page<GitlabBranchEntity> gitlabBranchEntityPage = gitlabBranchMapper.findPage(ConvertUtils.convertObject(query, GitlabBranchEntity.class), ConvertUtils.convertReqPage(page, new Page<>()));

        List<GitlabBranchEntity> gitlabBranchEntities = gitlabBranchEntityPage.getRecords();
        if(CollectionUtils.isEmpty(gitlabBranchEntities)) {
            return result;
        }

        result = ConvertUtils.convertPage(gitlabBranchEntityPage, GitlabBranchPageResp.class);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GitlabBranchEntity> findList(GitlabBranchEntity query) {
        return gitlabBranchMapper.findList(query);
    }

    @Override
    public boolean updateByPrimaryKeySelective(GitlabBranchEntity entity) {
        return SqlHelper.retBool(gitlabBranchMapper.updateByPrimaryKeySelective(entity));
    }

    @Override
    @Transactional
    public void create(GitlabBranchAddReq gitlabBranchAddReq) {
        LambdaQueryWrapper<AppServiceEntity> appServiceWrapper = new LambdaQueryWrapper<>();
        appServiceWrapper.eq(AppServiceEntity::getId, gitlabBranchAddReq.getAppServiceId());
        AppServiceEntity appServiceEntity = appServiceMapper.selectOne(appServiceWrapper);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }

        LambdaQueryWrapper<GitlabBranchEntity> branchWrapper = new LambdaQueryWrapper<>();
        branchWrapper.eq(GitlabBranchEntity::getAppServiceId,  appServiceEntity.getId())
                .eq(GitlabBranchEntity::getBranchName, gitlabBranchAddReq.getBranchName());
        GitlabBranchEntity exist = gitlabBranchMapper.selectOne(branchWrapper);
        if(exist !=null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "分支名已存在");
        }
        GitlabBranchEntity gitlabBranchEntity = ConvertUtils.convertObject(gitlabBranchAddReq, GitlabBranchEntity.class);
        gitlabBranchEntity.setId(UUID.randomUUID().toString().replace("-", ""));
        gitlabBranchMapper.insert(gitlabBranchEntity);
        Branch branchR = repositoryService.queryBranchByName(Integer.parseInt(appServiceEntity.getGitlabProjectId()), gitlabBranchEntity.getBranchName());
        if (branchR != null) {
            repositoryService.createBranch(Integer.parseInt(appServiceEntity.getGitlabProjectId()), gitlabBranchEntity.getBranchName(), gitlabBranchEntity.getOriginBranch(), PlatFormConstraint.GITLAB_ROOT_USER_ID, null ,null, null, null);
        }
    }

    @Override
    public void delete(String branchId) {
        GitlabBranchEntity gitlabBranchEntity = gitlabBranchMapper.selectById(branchId);
        if(gitlabBranchEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "分支不存在");
        }
        LambdaQueryWrapper<AppServiceEntity> appServiceWrapper = new LambdaQueryWrapper<>();
        appServiceWrapper.eq(AppServiceEntity::getId, gitlabBranchEntity.getAppServiceId());
        AppServiceEntity appServiceEntity = appServiceMapper.selectOne(appServiceWrapper);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }

        gitlabBranchMapper.deleteById(branchId);
        repositoryService.deleteBranch(Integer.parseInt(appServiceEntity.getGitlabProjectId()), gitlabBranchEntity.getBranchName(), PlatFormConstraint.GITLAB_ROOT_USER_ID);
    }

    @Override
    @Transactional
    public synchronized void webhookBranchSync(PushWebHook pushWebHook) {
        LambdaQueryWrapper<AppServiceEntity> projectIdWrapper = new  LambdaQueryWrapper<>();
        projectIdWrapper.eq(AppServiceEntity::getGitlabProjectId, pushWebHook.getProjectId());
        AppServiceEntity appServiceEntity = appServiceMapper.selectOne(projectIdWrapper);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }

        String branchName = pushWebHook.getRef().replaceFirst(PlatFormConstraint.REF_HEADS, "");
        LambdaQueryWrapper<GitlabBranchEntity> gitBranchWrapper = new  LambdaQueryWrapper<>();
        gitBranchWrapper.eq(GitlabBranchEntity::getAppServiceId, appServiceEntity.getId()).eq(GitlabBranchEntity::getBranchName, branchName);

        // 创建分支操作
        if (PlatFormConstraint.NO_COMMIT_SHA.equals(pushWebHook.getBefore())) {
            gitlabCommitService.webhookCommitSync(pushWebHook);
            webhookBranchCreate(pushWebHook, appServiceEntity, branchName, gitBranchWrapper);
        } else if (PlatFormConstraint.NO_COMMIT_SHA.equals(pushWebHook.getAfter())) {
            // 删除分支操作
            gitlabBranchMapper.delete(gitBranchWrapper);
        } else {
            // 某一分支提交代码操作
            try {
                gitlabCommitService.webhookCommitSync(pushWebHook);
                GitlabBranchEntity gitlabBranchEntity = gitlabBranchMapper.selectOne(gitBranchWrapper);
                if (gitlabBranchEntity == null) {
                    webhookBranchCreate(pushWebHook, appServiceEntity, branchName, gitBranchWrapper);
                    return;
                }
                String lastCommit = pushWebHook.getAfter();
                Optional<Commit> lastCommitOptional
                        = pushWebHook.getCommits().stream().filter(t -> lastCommit.equals(t.getId())).findFirst();
                Commit lastCommitDTO = new Commit();
                if (lastCommitOptional.isPresent()) {
                    lastCommitDTO = lastCommitOptional.get();
                }
                gitlabBranchEntity.setCommitSha(lastCommit);
                gitlabBranchEntity.setCommitUserName(pushWebHook.getUserUserName());
                gitlabBranchEntity.setCommitDate(lastCommitDTO.getTimestamp());
                gitlabBranchEntity.setCommitMsg(lastCommitDTO.getMessage());
                gitlabBranchEntity.setCommitUserId(pushWebHook.getUserId().toString());
                gitlabBranchMapper.updateByPrimaryKeySelective(gitlabBranchEntity);
            } catch (Exception e) {
                throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
            }

        }
    }

    private void webhookBranchCreate(PushWebHook pushWebHook, AppServiceEntity appServiceEntity, String branchName, LambdaQueryWrapper<GitlabBranchEntity> gitBranchWrapper) {
        try {
            String lastCommit = pushWebHook.getAfter();
            Long userId = pushWebHook.getUserId().longValue();
            LambdaQueryWrapper<GitlabCommitEntity> gitCommitWrapper = new LambdaQueryWrapper<>();
            gitCommitWrapper.eq(GitlabCommitEntity::getAppServiceId,  appServiceEntity.getId()).eq(GitlabCommitEntity::getCommitSha, lastCommit).eq(GitlabCommitEntity::getUserId, userId);
            List<GitlabCommitEntity> gitlabCommitEntities = gitlabCommitMapper.selectList(gitCommitWrapper);
            if (CollectionUtils.isEmpty(gitlabCommitEntities)) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "提交记录commit不存在");
            }
            GitlabBranchEntity gitlabBranchEntity = gitlabBranchMapper.selectOne(gitBranchWrapper);
            GitlabCommitEntity gitlabCommitEntity = gitlabCommitEntities.get(0);
            if (gitlabBranchEntity == null) {
                gitlabBranchEntity = new GitlabBranchEntity();
                gitlabBranchEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                gitlabBranchEntity.setUserId(userId.toString());
                gitlabBranchEntity.setAppServiceId(appServiceEntity.getId());
                gitlabBranchEntity.setBranchName(branchName);
                gitlabBranchEntity.setCommitUserId(userId.toString());
                gitlabBranchEntity.setCommitSha(lastCommit);
                gitlabBranchEntity.setOriginBranch(gitlabCommitEntity.getGitlabRef());
                gitlabBranchEntity.setCommitDate(gitlabCommitEntity.getCommitDate());
                gitlabBranchEntity.setCommitUserName(pushWebHook.getUserUserName());
                gitlabBranchEntity.setCommitMsg(gitlabCommitEntity.getCommitContent());
                gitlabBranchMapper.insert(gitlabBranchEntity);
            }
        } catch (Exception e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }
}
