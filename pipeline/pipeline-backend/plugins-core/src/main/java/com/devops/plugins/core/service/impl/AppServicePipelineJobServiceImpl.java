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
import com.devops.plugins.core.dao.AppServicePipelineJobMapper;
import com.devops.plugins.core.dao.AppServicePipelineMapper;
import com.devops.plugins.core.dao.GitlabCommitMapper;
import com.devops.plugins.core.dao.entity.AppServiceEntity;
import com.devops.plugins.core.dao.entity.AppServicePipelineEntity;
import com.devops.plugins.core.dao.entity.AppServicePipelineJobEntity;
import com.devops.plugins.core.dao.entity.GitlabCommitEntity;
import com.devops.plugins.core.pojo.gitlab.JobWebHook;
import com.devops.plugins.core.service.IAppServicePipelineJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 应用服务的CI流水线任务表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-01-11 14:39:23
 */
@Service
@Transactional
public class AppServicePipelineJobServiceImpl extends ServiceImpl<AppServicePipelineJobMapper, AppServicePipelineJobEntity> implements IAppServicePipelineJobService {

	@Resource
	private AppServicePipelineJobMapper appServicePipelineJobMapper;
	@Autowired
	private GitlabCommitMapper gitlabCommitMapper;
	@Autowired
	private AppServicePipelineMapper appServicePipelineMapper;
	@Autowired
	private AppServiceMapper appServiceMapper;


	@Override
	public Long findCount(AppServicePipelineJobEntity query) {
		return appServicePipelineJobMapper.findCount(query);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AppServicePipelineJobEntity> findPage(AppServicePipelineJobEntity query, Page<AppServicePipelineJobEntity> page) {
		return appServicePipelineJobMapper.findPage(query, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppServicePipelineJobEntity> findList(AppServicePipelineJobEntity query) {
		return appServicePipelineJobMapper.findList(query);
	}

	@Override
	public boolean updateByPrimaryKeySelective(AppServicePipelineJobEntity entity){
	    return SqlHelper.retBool(appServicePipelineJobMapper.updateByPrimaryKeySelective(entity));
	}

	@Override
	@Transactional
	public synchronized void webhookPipelineJobSync(JobWebHook jobWebHook) {
		//按照job的状态实时更新pipeline阶段的状态
		LambdaQueryWrapper<AppServiceEntity> projectIdWrapper = new LambdaQueryWrapper<>();
		projectIdWrapper.eq(AppServiceEntity::getGitlabProjectId, jobWebHook.getProject_id());
		projectIdWrapper.eq(AppServiceEntity::getGitlabCodeUrl, jobWebHook.getRepository().getGit_http_url());
		AppServiceEntity appServiceEntity = appServiceMapper.selectOne(projectIdWrapper);
		if (appServiceEntity == null) {
			throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
		}

		LambdaQueryWrapper<GitlabCommitEntity> gitCommitWrapper = new LambdaQueryWrapper<>();
		gitCommitWrapper.eq(GitlabCommitEntity::getCommitSha, jobWebHook.getSha()).eq(GitlabCommitEntity::getGitlabRef, jobWebHook.getRef());
		GitlabCommitEntity gitlabCommitEntity = gitlabCommitMapper.selectOne(gitCommitWrapper);
		if (jobWebHook.getCommit() == null || jobWebHook.getCommit().getId() == null) {
			return;
		}

//		String url = null;
//		String code = null;
//		String username = null;
//		String password = null;
//		if (appServiceEntity.getService_type().equals(PlatFormConstraint.SERVICE_TYPE_EXTERNAL)) {
//			QueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new QueryWrapper<>();
//			appServiceConfigEntityQueryWrapper.eq("app_service_id", appServiceEntity.getId());
//			List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
//			if (CollectionUtils.isNotEmpty(appServiceConfigEntities)) {
//				url = appServiceEntity.getGitlab_code_url();
//				code = appServiceConfigEntities.get(0).getToken();
//				username = appServiceConfigEntities.get(0).getUsername();
//				password = appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword());
//			}
//		} else {
//			url = appServiceEntity.getGitlab_code_url();
//		}

		if (gitlabCommitEntity != null && !PlatFormConstraint.Job_Created.equals(jobWebHook.getBuildStatus())) {
			LambdaQueryWrapper<AppServicePipelineEntity> pipeLineWrapper = new LambdaQueryWrapper<>();
			pipeLineWrapper.eq(AppServicePipelineEntity::getPipelineId, jobWebHook.getCommit().getId());
			pipeLineWrapper.eq(AppServicePipelineEntity::getAppServiceId,  appServiceEntity.getId());
			AppServicePipelineEntity appServicePipelineEntity = appServicePipelineMapper.selectOne(pipeLineWrapper);
			if (appServicePipelineEntity != null) {
				LambdaQueryWrapper<AppServicePipelineJobEntity> appServicePipelineJobWrapper = new LambdaQueryWrapper<>();
				appServicePipelineJobWrapper.eq(AppServicePipelineJobEntity::getJobId, jobWebHook.getBuildId());
				appServicePipelineJobWrapper.eq(AppServicePipelineJobEntity::getAppServiceId, appServiceEntity.getId());
				AppServicePipelineJobEntity appServicePipelineJobEntity = appServicePipelineJobMapper.selectOne(appServicePipelineJobWrapper);
				if(appServicePipelineJobEntity != null&&!appServicePipelineJobEntity.getStatus().equals(PlatFormConstraint.Job_Successed)) {
//					R<Job> jobResult = jobApi.query(Integer.parseInt(appServiceEntity.getGitlab_project_id()), jobWebHookVO.getBuildId().intValue(), url, code, username, password);
//					if (jobResult.isOK()) {
//						if (CollectionUtils.isNotEmpty(jobResult.getData().getArtifacts())) {
//							if (StringUtils.isNotBlank(code)) {
//								MutablePair<String, String> urls = ProjectUtils.parseUrl(appServiceEntity.getGitlab_code_url());
//								appServicePipelineJobEntity.setArchive(String.format("%s/api/v4/projects/%s/jobs/%s/artifacts?private_token=%s", urls.getLeft(), appServiceEntity.getGitlab_project_id(), appServicePipelineJobEntity.getJob_id(), code));
//							} else {
//								MutablePair<String, String> urls = ProjectUtils.parseUrl(appServiceEntity.getGitlab_code_url(), username, password);
//								appServicePipelineJobEntity.setArchive(String.format("%s/api/v4/projects/%s/jobs/%s/artifacts", urls.getLeft(), appServiceEntity.getGitlab_project_id(), appServicePipelineJobEntity.getJob_id()));
//							}
//						}
//					}
					appServicePipelineJobEntity.setStatus(jobWebHook.getBuildStatus());
					appServicePipelineJobEntity.setStartTime(jobWebHook.getBuildStartedAt());
					appServicePipelineJobEntity.setEndTime(jobWebHook.getBuildFinishedAt());
					appServicePipelineJobMapper.updateByPrimaryKeySelective(appServicePipelineJobEntity);
				}
			} else {
				return;
			}
		}
	}
}
