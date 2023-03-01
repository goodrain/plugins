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

package com.devops.plugins.gitlab.service.impl;

import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.gitlab.config.Gitlab4jClient;
import com.devops.plugins.gitlab.service.JobService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Job;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobServiceImpl implements JobService {

    private Gitlab4jClient gitlab4jclient;

    public JobServiceImpl(Gitlab4jClient gitlab4jclient) {
        this.gitlab4jclient = gitlab4jclient;
    }

    @Override
    public List<Job> listJobs(Integer projectId, Integer pipelineId, Integer userId) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);

        try {
            return gitLabApi.getJobApi().getJobsForPipeline(projectId, pipelineId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Job queryJob(Integer projectId, Integer jobId, String url, String token, String username, String password) {
        try {
            return gitlab4jclient.getGitLabApi(url , token, username, password, null)
                    .getJobApi().getJob(projectId, jobId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public String queryTrace(Integer projectId, Integer jobId, Integer userId) {
        try {
            GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);

            return gitLabApi
                    .getJobApi().getTrace(projectId, jobId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Job retry(Integer projectId, Integer userId, Integer jobId) {
        try {
            GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);

            return gitLabApi
                    .getJobApi().retryJob(projectId, jobId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL,e.getMessage(), e);
        }
    }

    @Override
    public Job play(Integer projectId, Integer userId, Integer jobId) {
        try {
            return gitlab4jclient.getGitLabApi(userId)
                    .getJobApi().playJob(projectId, jobId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }
}
