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

import org.gitlab4j.api.models.Pipeline;

import java.util.List;
import java.util.Map;


/**
 * Created by zzy on 2021/12/21.
 */
public interface PipelineService {

    /**
     * 分页查询项目下的pipelines
     *
     * @param projectId 项目 Id
     * @param page      页码
     * @param size      每页大小
     * @param userId    用户Id
     * @return List
     */
    List<Pipeline> listPipelinesByPage(Integer projectId, Integer page, Integer size, Integer userId);

    /**
     * 查询项目下的pipelines
     *
     * @param projectId 项目 Id
     * @param userId    用户Id
     * @return List
     */
    List<Pipeline> listPipelines(Integer projectId, Integer userId);

    /**
     * 查询某个pipelines的具体信息
     *
     * @param projectId  项目 Id
     * @param pipelineId 流水线 Id
     * @param userId     用户Id
     * @return Pipeline
     */
    Pipeline queryPipeline(Integer projectId, Integer pipelineId, Integer userId);

    /**
     * Retry jobs in a pipeline
     *
     * @param projectId  项目 Id
     * @param pipelineId 流水线 Id
     * @param userId     用户Id
     * @return
     */
    Pipeline retryPipeline(Integer projectId, Integer pipelineId, Integer userId);

    /**
     * Cancel a pipelines jobs
     *
     * @param projectId  项目 Id
     * @param pipelineId 流水线 Id
     * @param userId     用户Id
     * @return
     */
    Pipeline cancelPipeline(Integer projectId, Integer pipelineId, Integer userId);

    /**
     * Create a new pipeline
     *
     * @param projectId
     * @param userId
     * @param ref
     * @param variables
     * @return
     */
    Pipeline createPipeline(Integer projectId, Integer userId, String url, String token , String username, String password, String ref, Map<String, String> variables);
}
