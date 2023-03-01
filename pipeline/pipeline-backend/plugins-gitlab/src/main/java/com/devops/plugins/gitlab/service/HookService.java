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

import org.gitlab4j.api.models.ProjectHook;

import java.util.List;


public interface HookService {

    /**
     * 创建ProjectHook对象
     *
     * @param projectId   项目id
     * @param userId      用户Id
     * @param projectHook projectHook对象
     * @return ProjectHook
     */

    ProjectHook createProjectHook(Integer projectId, ProjectHook projectHook, String url, String token ,String username, String password,  Integer userId);


    /**
     * 更新ProjectHook对象
     *
     * @param projectId
     * @param hookId
     * @param userId
     * @return
     */
    ProjectHook updateProjectHook(Integer projectId, Integer hookId, Integer userId);


    /**
     * 查询ProjectHook列表
     *
     * @param projectId
     * @param userId
     * @return
     */
    List<ProjectHook> listProjectHook(Integer projectId, Integer userId, String url , String token , String username, String password);


    /**
     *  删除ProjectHook
     *
     * @param projectid
     * @param hookIds
     * @param url
     * @param token
     * @param username
     * @param password
     * @param userId
     */
    void deleteProjectHook(Integer projectid, List<Integer> hookIds, String url, String token, String username, String password, Integer userId);
}
