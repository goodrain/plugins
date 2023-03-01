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
import com.devops.plugins.gitlab.service.HookService;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.models.ProjectHook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HookServiceImpl implements HookService {

    private Gitlab4jClient gitlab4jclient;


    public HookServiceImpl(Gitlab4jClient gitlab4jclient) {
        this.gitlab4jclient = gitlab4jclient;
    }

    @Override
    public ProjectHook createProjectHook(Integer projectId, ProjectHook projectHook, String url, String token, String username, String password, Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi(url, token, username, password, userId).getProjectApi()
                    .addHook(projectId, projectHook.getUrl(), projectHook, true, projectHook.getToken());
        } catch (Exception e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public ProjectHook updateProjectHook(Integer projectId, Integer hookId, Integer userId) {
        try {
            ProjectHook projectHook = gitlab4jclient.getGitLabApi(userId).getProjectApi().getHook(projectId, hookId);
            projectHook.setPipelineEvents(true);
            projectHook.setJobEvents(true);
            projectHook.setTagPushEvents(true);
            return gitlab4jclient.getGitLabApi(userId).getProjectApi().modifyHook(projectHook);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public void deleteProjectHook(Integer projectid, List<Integer> hookIds, String url, String token, String username, String password, Integer userId) {
        ProjectApi projectApi = gitlab4jclient.getGitLabApi(url, token, username, password, userId).getProjectApi();
        hookIds.stream().forEach(hookId -> {
            try {
                projectApi.deleteHook(projectid, hookId);
            } catch (GitLabApiException e) {
                throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
            }
        });
    }


    @Override
    public List<ProjectHook> listProjectHook(Integer projectId, Integer userId, String url, String token, String username, String password) {

        try {
            return gitlab4jclient.getGitLabApi(url, token, username, password, userId).getProjectApi().getHooks(projectId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }

    }
}
