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

import org.gitlab4j.api.models.DeployKey;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.Variable;

import java.util.List;


public interface ProjectService {

    /**
     * 通过项目名称创建项目
     *
     * @param groupId     组 Id
     * @param projectName 项目名
     * @param userId      用户Id
     * @return Project
     */
    Project createProject(Integer groupId, String projectName, Integer userId, boolean visibility);

    /**
     * 删除项目
     *
     * @param projectId 项目 id
     * @param userId    用户名
     */
    void deleteProject(Integer projectId, Integer userId);


    /**
     * 通过group名和项目名删除项目
     *
     * @param groupName   项目 id
     * @param userId      用户名
     * @param projectName 项目名
     */
    void deleteProjectByName(String groupName, String projectName, Integer userId);

    /**
     * 增加项目ci环境变量
     *
     * @param projectId  项目Id
     * @param key        变量key
     * @param value      变量值
     * @param protecteds 变量是否保护
     * @param userId     用户Id
     * @return Map
     */
    Variable createVariable(Integer projectId,
                                       String key,
                                       String value,
                                       boolean protecteds,
                                       Integer userId,
                                        String url,
                                        String token,
                                        String username,
                                        String password);

    /**
     * 删除项目ci环境变量
     *
     * @param projectId 项目id
     * @param key       变量key
     * @param userId    用户id
     */
    void deleteVariable(Integer projectId,
                        String key,
                        Integer userId,
                        String url,
                        String token,
                        String username,
                        String password);

    /**
     * 批量删除项目ci环境变量
     *
     * @param projectId 项目id
     * @param keys      变量keys
     * @param userId    用户id
     */
    void batchDeleteVariable(Integer projectId,
                             List<String> keys,
                             Integer userId,
                             String url,
                             String token,
                             String username,
                             String password);

    /**
     * 批量增加项目ci环境变量
     *
     * @param projectId 项目Id
     * @param list      变量信息
     * @param userId    用户id
     * @return
     */
    List<Variable> batchCreateVariable(Integer projectId,
                                                  List<Variable> list,
                                                  Integer userId,
                                       String url,
                                       String token,
                                       String username,
                                       String password);


    /**
     * 更新项目
     *
     * @param project 项目Id
     * @param userId  用户Id
     * @return Project
     */
    Project updateProject(Project project, Integer userId);



    /**
     * 通过项目名称创建项目
     *
     * @param projectId 项目Id
     * @param title     标题
     * @param key       ssh key
     * @param canPush   canPush
     * @param userId    用户Id
     */
    void createDeployKey(Integer projectId, String title, String key, boolean canPush, Integer userId);

    /**
     * 通过项目id查询项目
     *
     * @param projectId 项目id
     * @return Project
     */
    Project getProjectById(Integer projectId, String url, String token, String username, String password);

    /**
     * 通过组名项目名查询项目
     *
     * @param userId      项目Id
     * @param groupCode   组名
     * @param projectCode 项目名
     * @param statistics
     * @return Project
     */
    Project getProject(Integer userId, String groupCode, String projectCode, Boolean statistics);


    /**
     * 查询项目Variable
     *
     * @param projectId 项目id
     * @param userId    用户id
     * @return list
     */
    List<Variable> getProjectVariable(Integer projectId, Integer userId, String url, String token , String username, String passwor);

    /**
     * 添加项目成员
     *
     * @param projectId 项目id
     * @param member    成员信息
     * @return Member
     */
    Member createMember(Integer projectId, Member member);

    /**
     * 添加项目成员
     *
     * @param projectId 项目id
     * @param list      成员信息
     * @return Member
     */
    List<Member> updateMembers(Integer projectId, List<Member> list);

    /**
     * 移除项目成员
     *
     * @param projectId 项目id
     * @param userId    用户id
     */
    void deleteMember(Integer projectId, Integer userId);

    /**
     * 查询deployKeys
     *
     * @param projectId 项目Id
     * @param userId    用户Id
     * @Return List
     */
    List<DeployKey> getDeployKeys(Integer projectId, Integer userId);

    /**
     * 查询项目角色
     *
     * @param projectId 项目id
     * @param userId    用户Id
     * @return Member
     */
    Member getMember(Integer projectId, Integer userId);

    /**
     * 获取项目下所有成员
     *
     * @param projectId 项目id
     * @return List
     */
    List<Member> getAllMemberByProjectId(Integer projectId);


    /**
     * 获取用户的gitlab项目列表
     *
     * @param userId 用户id
     * @return List
     */
    List<Project> getMemberProjects(Integer userId);


    /**
     * 校验用户是否有某代码仓库的maintainer及以上权限
     *
     * @param url
     * @param token
     * @param username
     * @param password
     */
    Integer  check(String url , String token , String username, String password);

}
