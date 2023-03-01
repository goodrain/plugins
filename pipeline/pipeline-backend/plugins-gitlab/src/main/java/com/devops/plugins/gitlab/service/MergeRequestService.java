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

package com.devops.plugins.gitlab.service;

import com.devops.plugins.gitlab.pojo.MergeRequestReq;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.MergeRequest;

import java.util.List;


public interface MergeRequestService {

    /**
     * 创建merge请求
     *
     * @param projectId 工程ID
     * @param userId    用户Id Optional
     * @return MergeRequest
     */
    MergeRequest createMergeRequest(Integer projectId, MergeRequestReq mergeRequestParams, Integer userId);


    /**
     * 获取合并请求merge request
     *
     * @param projectId      项目id
     * @param mergeRequestId 合并请求id
     * @param userId         用户Id Optional
     * @return MergeRequest
     */
    MergeRequest queryMergeRequest(Integer projectId, Integer mergeRequestId, Integer userId);

    /**
     * 获取合并请求列表merge request
     *
     * @param projectId 项目id
     * @return MergeRequest
     */
    List<MergeRequest> listMergeRequests(Integer projectId);

    /**
     * 获取合并请求列表merge request
     *
     * @param projectId 项目id
     * @return MergeRequest
     */
    List<Integer> listMergeRequestIds(Integer projectId);


    /**
     * 执行merge请求
     *
     * @param projectId                 项目id
     * @param mergeRequestId            merge请求的id
     * @param mergeCommitMessage        merge的commit信息
     * @param shouldRemoveSourceBranch  merge后是否删除该分支
     * @param mergeWhenPipelineSucceeds pipeline成功后自动合并分支
     * @param userId                    用户Id
     * @return MergeRequest
     */
    MergeRequest acceptMergeRequest(
            Integer projectId, Integer mergeRequestId, String mergeCommitMessage,
            Boolean shouldRemoveSourceBranch, Boolean mergeWhenPipelineSucceeds,
            Integer userId);

    /**
     * 查询合并请求的commits
     *
     * @param projectId      项目id
     * @param mergeRequestId 合并请求ID
     * @param userId         用户Id
     * @return List
     */
    List<Commit> listCommits(Integer projectId, Integer mergeRequestId, Integer userId);

    /**
     * 删除合并请求
     *
     * @param projectId      项目id
     * @param mergeRequestId 合并请求ID
     */
    void deleteMergeRequest(Integer projectId, Integer mergeRequestId);

}

