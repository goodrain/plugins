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
import com.devops.plugins.gitlab.pojo.MergeRequestReq;
import com.devops.plugins.gitlab.service.MergeRequestService;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.MergeRequestParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MergeRequestServiceImpl implements MergeRequestService {


    private Gitlab4jClient gitlab4jclient;

    public MergeRequestServiceImpl(Gitlab4jClient gitlab4jclient) {
        this.gitlab4jclient = gitlab4jclient;
    }

    @Override
    public MergeRequest createMergeRequest(Integer projectId, MergeRequestReq mergeRequest, Integer userId) {
        try {

            MergeRequestParams mergeRequestParams = new MergeRequestParams();
            mergeRequestParams.withSourceBranch(mergeRequest.getSourceBranch());
            mergeRequestParams.withTargetBranch(mergeRequest.getTargetBranch());
            mergeRequestParams.withTitle(mergeRequest.getTitle());
            mergeRequestParams.withDescription(mergeRequest.getDescription());
            mergeRequestParams.withRemoveSourceBranch(mergeRequest.getRemoveSourceBranch());
            mergeRequestParams.withApprovalsBeforeMerge(mergeRequest.getApprovalsBeforeMerge());
            mergeRequestParams.withAssigneeId(mergeRequest.getAssigneeId());
            mergeRequestParams.withAssigneeIds(mergeRequest.getAssigneeIds());


            return gitlab4jclient.getGitLabApi(userId).getMergeRequestApi()
                    .createMergeRequest(projectId, mergeRequestParams);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }


    @Override
    public MergeRequest queryMergeRequest(Integer projectId, Integer mergeRequestId, Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi(userId).getMergeRequestApi()
                    .getMergeRequest(projectId, mergeRequestId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<MergeRequest> listMergeRequests(Integer projectId) {
        try {
            return gitlab4jclient.getGitLabApi(null).getMergeRequestApi()
                    .getMergeRequests(projectId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<Integer> listMergeRequestIds(Integer projectId) {
        List<MergeRequest> mergeRequestList = listMergeRequests(projectId);
        return mergeRequestList.stream().map(MergeRequest::getIid).collect(Collectors.toList());
    }

    @Override
    public MergeRequest acceptMergeRequest(Integer projectId, Integer mergeRequestId,
                                           String mergeCommitMessage, Boolean shouldRemoveSourceBranch,
                                           Boolean mergeWhenPipelineSucceeds, Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi(userId)
                    .getMergeRequestApi()
                    .acceptMergeRequest(projectId,
                            mergeRequestId,
                            mergeCommitMessage,
                            shouldRemoveSourceBranch,
                            mergeWhenPipelineSucceeds);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL,e.getMessage(), e);
        }
    }

    @Override
    public List<Commit> listCommits(Integer projectId, Integer mergeRequestId, Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi(userId)
                    .getMergeRequestApi().getCommits(projectId, mergeRequestId);
        } catch (GitLabApiException e) {
            if (e.getMessage().equals("404 Not found")) {
                throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage());
            }
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public void deleteMergeRequest(Integer projectId, Integer mergeRequestId) {
        try {
            gitlab4jclient.getGitLabApi()
                    .getMergeRequestApi().deleteMergeRequest(projectId, mergeRequestId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }
}
