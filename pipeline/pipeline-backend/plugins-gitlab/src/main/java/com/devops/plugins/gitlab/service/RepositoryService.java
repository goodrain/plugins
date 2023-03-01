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

import com.devops.plugins.gitlab.pojo.FileCreationReq;
import com.devops.plugins.gitlab.pojo.FileDeleteReq;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.CompareResults;
import org.gitlab4j.api.models.RepositoryFile;

import java.io.InputStream;
import java.util.List;


public interface RepositoryService {

    /**
     * 创建新分支
     *
     * @param projectId  项目id
     * @param branchName 分支名
     * @param source     源分支名
     * @param userId     用户Id
     * @return Branch
     */
    Branch createBranch(Integer projectId, String branchName, String source, Integer userId, String url, String token, String username, String password);



    /**
     * 根据分支名删除分支
     *
     * @param projectId  项目id
     * @param branchName 分支名
     * @param userId     用户Id
     */
    void deleteBranch(Integer projectId, String branchName, Integer userId);

    /**
     * 根据分支名查询分支
     *
     * @param projectId  项目id
     * @param branchName 分支名
     * @return Branch
     */
    Branch queryBranchByName(Integer projectId, String branchName);


    /**
     * 获取项目下所有分支
     *
     * @param projectId 项目id
     * @param userId    用户Id
     * @return List
     */
    List<Branch> listBranches(Integer projectId, Integer userId, String url, String token, String username, String password);


//    /**
//     * 项目下创建readme
//     *
//     * @param projectId 项目id
//     * @param userId    用户Id
//     */
//    boolean createFile(Integer projectId, Integer userId);


    /**
     * 项目下获取file
     *
     * @param projectId 项目id
     * @param commit    the commit SHA or branch name
     * @param filePath  file path
     * @return file
     */
    RepositoryFile getFile(Integer projectId, String commit, String filePath);


    /**
     * 项目下获取diffs
     *
     * @param projectId 项目id
     * @param from      the commit SHA or branch name
     * @param to        the commit SHA or branch name
     * @return CompareResults
     */
    CompareResults getDiffs(Integer projectId, String from, String to);

    /**
     * 创建文件
     *
     * @param projectId
     * @param path
     * @param content
     * @param commitMessage
     * @param userId
     * @param branchName
     * @return
     */
    RepositoryFile createFile(Integer projectId,  Integer userId,   String url, String token, String username, String password, FileCreationReq fileCreationReq);

    /**
     * 更新文件
     *
     * @param projectId
     * @param path
     * @param content
     * @param commitMessage
     * @param userId
     * @param branchName
     * @return
     */
    RepositoryFile updateFile(Integer projectId, Integer userId,  String url, String token, String username, String password, FileCreationReq fileCreationReq);

    /**
     * 删除文件
     *
     * @param projectId
     * @param path
     * @param commitMessage
     * @param userId
     * @param branchName
     */
    void deleteFile(Integer projectId, Integer userId, String url, String token, String username, String password, FileDeleteReq fileDeleteReq);

    /**
     * 下载压缩包
     *
     * @param projectId gitlab项目id
     * @param userId    gitlab用户id
     * @param commitSha 要下载的commit
     * @return tgz压缩包的字节数组
     */
    byte[] downloadArchive(Integer projectId, Integer userId, String commitSha);


    /**
     * 下载压缩包
     *
     * @param projectId gitlab项目id
     * @param userId    gitlab用户id
     * @param commitSha 要下载的commit或者分支
     * @param format    文件类型
     * @return
     */
    InputStream downloadArchiveByFormat(Integer projectId, Integer userId, String commitSha, String format);
}
