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
import com.devops.plugins.gitlab.service.CommitService;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.CommitPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zzy on 2021/12/21.
 */
@Service
public class CommitServiceImpl implements CommitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommitServiceImpl.class);
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private Gitlab4jClient gitlab4jclient;

    @Override
    public Commit getCommit(Integer projectId, String sha, Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi(userId).getCommitsApi().getCommit(projectId, sha);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }


    @Override
    public List<Commit> getCommits(Integer gitLabProjectId, String ref, String since) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        Date sinceDate = null;
        try {
            sinceDate = simpleDateFormat.parse(since);
        } catch (ParseException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
        try {
            return gitlab4jclient.getGitLabApi()
                    .getCommitsApi().getCommits(gitLabProjectId, ref, sinceDate, new Date(), null);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<Commit> listCommits(Integer gilabProjectId, int page, int size, Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi(userId).getCommitsApi().getCommits(gilabProjectId, page, size);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public void createCommit(Integer projectId, Integer userId, CommitPayload commitPayload) {
        try {
            gitlab4jclient.getGitLabApi(userId).getCommitsApi().createCommit(projectId, commitPayload);
        } catch (GitLabApiException e) {
            LOGGER.info("error occurred while creating commit. the projectId is {}, the user id is {}, the payload is: {}.", projectId, userId, commitPayload);
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }
}
