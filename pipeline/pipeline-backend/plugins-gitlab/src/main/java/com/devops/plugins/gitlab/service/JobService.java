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

import org.gitlab4j.api.models.Job;

import java.util.List;


public interface JobService {

    /**
     * 查询项目下pipeline的jobs
     *
     * @param projectId  项目id
     * @param pipelineId 流水线id
     * @param userId     用户Id
     * @return List
     */
    List<Job> listJobs(Integer projectId, Integer pipelineId, Integer userId);

    /**
     * 查询项目下某个Job的具体信息
     *
     * @param projectId 项目id
     * @param jobId     job id
     * @return Job
     */
    Job queryJob(Integer projectId, Integer jobId, String url , String token ,String username, String password);

    /**
     * 查询job日志
     *
     * @param projectId
     * @param userId
     * @param jobId
     * @return
     */
    String queryTrace(Integer projectId, Integer jobId, Integer userId);


    /**
     * 运行某个job
     *
     * @param projectId
     * @param userId
     * @param jobId
     * @return
     */
    Job play(Integer projectId, Integer userId, Integer jobId);

    /**
     * 重试某个Job
     *
     * @param projectId
     * @param userId
     * @param jobId
     * @return
     */
    Job retry(Integer projectId, Integer userId, Integer jobId);
}
