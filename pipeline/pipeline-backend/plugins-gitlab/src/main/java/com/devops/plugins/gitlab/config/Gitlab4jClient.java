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

package com.devops.plugins.gitlab.config;

import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.gitlab.utils.ProjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by zzy on 2021/12/20.
 */
@Component
public class Gitlab4jClient {
    private static final Integer ROOT_USER_ID = 1;

    private ConcurrentHashMap<String, GitLabApi> clientMap = new ConcurrentHashMap<>();

    private GitLabApi createGitLabApi(GitLabApi.ApiVersion apiVersion, Integer userId) {
        try {
            GitLabApi newGitLabApi = new GitLabApi(apiVersion, "", "");
            if (userId != null) {
                newGitLabApi.setSudoAsId(userId);
            }
            return newGitLabApi;
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    public GitLabApi getGitLabApiUser(Integer userId) {
        return getGitLabApi(GitLabApi.ApiVersion.V4, userId);
    }

    public GitLabApi getGitLabApiVersion(GitLabApi.ApiVersion apiVersion) {
        return getGitLabApi(apiVersion, ROOT_USER_ID);
    }

    public GitLabApi getGitLabApi() {
        return getGitLabApiUser(ROOT_USER_ID);
    }

    public GitLabApi getGitLabApi(Integer userId) {
        return getGitLabApi(GitLabApi.ApiVersion.V4, userId);
    }

    /**
     * 创建gitLabApi
     *
     * @param apiVersion api版本
     * @param userId     用户ID
     * @return GitLabApi
     */
    public GitLabApi getGitLabApi(GitLabApi.ApiVersion apiVersion, Integer userId) {
        String key = apiVersion.getApiNamespace() + "-" + userId;
        GitLabApi gitLabApi = clientMap.get(key);
        if (gitLabApi != null) {
            return gitLabApi;
        } else {
            gitLabApi = createGitLabApi(apiVersion, userId);
            clientMap.put(key, gitLabApi);
            return gitLabApi;
        }
    }

    /**
     *
     * @param url
     * @param token
     * @param username
     * @param password
     * @param userId
     * @return
     */
    public GitLabApi getGitLabApi(String url, String token, String username, String password, Integer userId) {
        try {
            GitLabApi newGitLabApi;
            if(StringUtils.isNotBlank(url))
            {
                url = ProjectUtils.parseUrl(url).getLeft();
            }
            if (StringUtils.isNotBlank(token)) {
                newGitLabApi = new GitLabApi(GitLabApi.ApiVersion.V4, url, token);
            } else if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                newGitLabApi = GitLabApi.oauth2Login(url, username, password);
            } else if (userId != null) {
                newGitLabApi = getGitLabApi(userId);
            } else {
                newGitLabApi = getGitLabApi();
            }
            return newGitLabApi;
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "用户名不正确或密码错误");
        }
    }


}
