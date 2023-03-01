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
import com.devops.plugins.gitlab.pojo.FileCreationReq;
import com.devops.plugins.gitlab.pojo.FileDeleteReq;
import com.devops.plugins.gitlab.service.RepositoryService;
import org.apache.commons.io.IOUtils;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.CompareResults;
import org.gitlab4j.api.models.RepositoryFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;


@Service
public class RepositoryServiceImpl implements RepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceImpl.class);

    private Gitlab4jClient gitlab4jclient;

    public RepositoryServiceImpl(Gitlab4jClient gitlab4jclient) {
        this.gitlab4jclient = gitlab4jclient;
    }

    @Override
    public Branch createBranch(Integer projectId, String branchName, String source, Integer userId, String url, String token, String username, String password) {
        try {
            return this.gitlab4jclient.getGitLabApi(url, token, username, password, userId).getRepositoryApi()
                    .createBranch(projectId, branchName, source);
        } catch (GitLabApiException e) {
            if (e.getMessage().equals("Branch already exists")) {
                Branch branch = new Branch();
                branch.setName("create branch message:Branch already exists");
                return branch;
            }
            if (e.getMessage().equals("403 Forbidden")) {
                throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);

            }
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }


    @Override
    public void deleteBranch(Integer projectId, String branchName, Integer userId) {
        try {
            gitlab4jclient
                    .getGitLabApi(userId)
                    .getRepositoryApi()
                    .deleteBranch(projectId, branchName);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Branch queryBranchByName(Integer projectId, String branchName) {
        try {
            return gitlab4jclient.getGitLabApi()
                    .getRepositoryApi()
                    .getBranch(projectId, branchName);
        } catch (GitLabApiException e) {
            return new Branch();
        }
    }

    @Override
    public List<Branch> listBranches(Integer projectId, Integer userId, String url, String token, String username, String password) {
        try {
            GitLabApi gitLabApi;
            gitLabApi = gitlab4jclient.getGitLabApi(url, token, username, password, userId);
            return gitLabApi
                    .getRepositoryApi()
                    .getBranches(projectId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }


    @Override
    public RepositoryFile getFile(Integer projectId, String commit, String filePath) {
        GitLabApi gitLabApi;
        gitLabApi = gitlab4jclient.getGitLabApi();
        RepositoryFile file;
        try {
            file = gitLabApi.getRepositoryFileApi().getFile(filePath, projectId, commit);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
        return file;
    }

    @Override
    public CompareResults getDiffs(Integer projectId, String from, String to) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi();
        try {
            return gitLabApi.getRepositoryApi().compare(projectId, from, to);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public RepositoryFile createFile(Integer projectId, Integer userId, String url, String token, String username, String password, FileCreationReq fileCreationReq) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(url, token, username, password, userId);
        RepositoryFile repositoryFile = new RepositoryFile();
        try {
            RepositoryFile oldRepositoryFile = gitLabApi.getRepositoryFileApi().getFile(projectId, fileCreationReq.getPath(), fileCreationReq.getBranchName());
            if (oldRepositoryFile != null) {
                return oldRepositoryFile;
            }
        } catch (GitLabApiException e) {
            if (e.getHttpStatus() == 404) {
                logger.info("文件已存在");
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
            }
        }

        try {
            repositoryFile.setContent(fileCreationReq.getContent());
            repositoryFile.setFilePath(fileCreationReq.getPath());
            repositoryFile = gitLabApi.getRepositoryFileApi().createFile(projectId, repositoryFile, fileCreationReq.getBranchName(), fileCreationReq.getCommitMessage());
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
        return repositoryFile;
    }

    @Override
    public RepositoryFile updateFile(Integer projectId, Integer userId, String url, String token, String username, String password, FileCreationReq fileCreationReq) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(url, token, username, password, userId);

        try {
            gitLabApi.getRepositoryFileApi().getFile(projectId, fileCreationReq.getPath(), fileCreationReq.getBranchName());
        } catch (GitLabApiException e) {
            if (e.getHttpStatus() == 404) {
                RepositoryFile repositoryFile = new RepositoryFile();
                repositoryFile.setContent(fileCreationReq.getContent());
                repositoryFile.setFilePath(fileCreationReq.getPath());
                try {
                    repositoryFile = gitLabApi.getRepositoryFileApi().createFile(projectId, repositoryFile, fileCreationReq.getBranchName(), fileCreationReq.getCommitMessage());
                } catch (GitLabApiException ex) {
                    throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
                }
                return repositoryFile;
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
            }
        }

        try {
            RepositoryFile repositoryFile = new RepositoryFile();
            repositoryFile.setContent(fileCreationReq.getContent());
            repositoryFile.setFilePath(fileCreationReq.getPath());
            String branch = fileCreationReq.getBranchName() != null ? fileCreationReq.getBranchName() : "master";
            repositoryFile = gitLabApi.getRepositoryFileApi().updateFile(repositoryFile, projectId, branch, fileCreationReq.getCommitMessage());
            return repositoryFile;
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(Integer projectId, Integer userId, String url, String token, String username, String password, FileDeleteReq  fileDeleteReq) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(url, token, username, password, userId);
        try {
            String branch = fileDeleteReq.getBranchName() != null ? fileDeleteReq.getBranchName() : "master";
            gitLabApi.getRepositoryFileApi().deleteFile(fileDeleteReq.getPath(), projectId, branch, fileDeleteReq.getCommitMessage());
        } catch (GitLabApiException e) {
                logger.info("文件不存在");
        }
    }

    @Override
    public byte[] downloadArchive(Integer projectId, Integer userId, String commitSha) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);
        try {
            InputStream inputStream = gitLabApi.getRepositoryApi().getRepositoryArchive(projectId, commitSha);
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public InputStream downloadArchiveByFormat(Integer projectId, Integer userId, String commitSha, String format) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);
        try {
            return gitLabApi.getRepositoryApi().getRepositoryArchive(projectId, commitSha, format);
        } catch (Exception e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }
}
