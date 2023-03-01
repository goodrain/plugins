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
import com.devops.plugins.gitlab.service.PipelineService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Pipeline;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Service
public class PipelineServiceImpl implements PipelineService {

    private Gitlab4jClient gitlab4jclient;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public PipelineServiceImpl(Gitlab4jClient gitlab4jclient) {
        this.gitlab4jclient = gitlab4jclient;
    }

    @Override
    public List<Pipeline> listPipelinesByPage(Integer projectId, Integer page, Integer size, Integer userId) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);
        try {
            return gitLabApi.getPipelineApi().getPipelines(projectId, page, size == 0 ? 40 : size);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<Pipeline> listPipelines(Integer projectId, Integer userId) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);
        try {
            return gitLabApi.getPipelineApi().getPipelines(projectId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);

        }
    }

    @Override
    public Pipeline queryPipeline(Integer projectId, Integer pipelineId, Integer userId) {
        try {
            GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);
            Pipeline pipeline = gitLabApi
                    .getPipelineApi().getPipeline(projectId, pipelineId);
            return pipeline;
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Pipeline retryPipeline(Integer projectId, Integer pipelineId, Integer userId) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);
        try {
            return gitLabApi
                    .getPipelineApi().retryPipelineJob(projectId, pipelineId);
        } catch (GitLabApiException e) {
            return new Pipeline();
        }
    }

    @Override
    public Pipeline cancelPipeline(Integer projectId, Integer pipelineId, Integer userId) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);

        try {
            return gitLabApi
                    .getPipelineApi().cancelPipelineJobs(projectId, pipelineId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Pipeline createPipeline(Integer projectId, Integer userId, String url, String token ,String username, String password,  String ref,  Map<String, String> variables) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(url, token, username, password, userId);
        try {
            return gitLabApi
                    .getPipelineApi().createPipeline(projectId, ref, variables);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }
}
