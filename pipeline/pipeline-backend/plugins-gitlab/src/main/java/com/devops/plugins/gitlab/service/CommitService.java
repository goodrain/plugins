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

import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.CommitPayload;

import java.util.List;

/**
 * Created by zzy on 2021/12/21.
 */
public interface CommitService {

    /**
     * 获取commit详情
     *
     * @param projectId
     * @param sha
     * @param userId
     * @return
     */
    Commit getCommit(Integer projectId, String sha, Integer userId);

    /**
     * 获取某分支阶段提交
     *
     * @param gitLabProjectId
     * @param ref
     * @param since
     * @return
     */
    List<Commit> getCommits(Integer gitLabProjectId, String ref, String since);


    /**
     * 获取提交列表
     *
     * @param gilabProjectId
     * @param page
     * @param size
     * @param userId
     * @return
     */
    List<Commit> listCommits(Integer gilabProjectId, int page, int size, Integer userId);

    /**
     * 创建commit，可以批量操作文件
     *
     * @param projectId     gitlab项目id
     * @param userId        gitlab用户id
     * @param commitPayload 操作文件相关的信息
     */
    void createCommit(Integer projectId, Integer userId, CommitPayload commitPayload);
}
