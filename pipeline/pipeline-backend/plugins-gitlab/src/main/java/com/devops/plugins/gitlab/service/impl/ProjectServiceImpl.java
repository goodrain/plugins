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
import com.devops.plugins.gitlab.service.ProjectService;
import com.devops.plugins.gitlab.utils.ProjectUtils;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private Gitlab4jClient gitlab4jclient;

    public ProjectServiceImpl(Gitlab4jClient gitlab4jclient) {
        this.gitlab4jclient = gitlab4jclient;
    }

    @Override
    public Project createProject(Integer groupId, String projectName, Integer userId, boolean visibility) {
        GitLabApi gitLabApi = gitlab4jclient.getGitLabApi(userId);
        try {
            Project projectReq = new Project();
            projectReq.setName(projectName);
            projectReq.setPublic(true);
            projectReq.setPath(projectName);
            Namespace namespace = new Namespace();
            namespace.setId(groupId);
            projectReq.setNamespace(namespace);
            Project project = gitLabApi.getProjectApi().createProject(projectReq);
            return project;
        } catch (GitLabApiException e) {
            LOGGER.info("groupId:{},projectName:{},userId:{},visibility:{}", groupId, projectName, userId, visibility);
            LOGGER.info("{}", e.getMessage());
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public void deleteProject(Integer projectId, Integer userId) {
        try {
            gitlab4jclient
                    .getGitLabApi(userId)
                    .getProjectApi().deleteProject(projectId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public void deleteProjectByName(String groupName, String projectName, Integer userId) {
        try {
            Project project = null;
            try {
                project = gitlab4jclient
                        .getGitLabApi(userId)
                        .getProjectApi().getProject(groupName, projectName);
            } catch (GitLabApiException e) {
                if (e.getHttpStatus() == 404) {
                    LOGGER.info("delete not exist project: {}", e.getMessage());
                } else {
                    throw e;
                }
            }
            if (project != null) {
                gitlab4jclient
                        .getGitLabApi(userId)
                        .getProjectApi().deleteProject(project.getId());
            }
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Project getProjectById(Integer projectId, String url, String token, String username, String password) {
        try {
            return gitlab4jclient.getGitLabApi(url, token, username, password, null).getProjectApi().getProject(projectId);
        } catch (GitLabApiException e) {
            if (e.getHttpStatus() == 404) {
                return new Project();
            }
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Project getProject(Integer userId, String groupCode, String projectCode, Boolean statistics) {
        try {
            String path = groupCode + "/" + projectCode;
            return gitlab4jclient
                    .getGitLabApi(userId)
                    .getProjectApi().getProject(path, statistics);
        } catch (Exception e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<Variable> getProjectVariable(Integer projectId, Integer userId, String url, String token , String username, String password) {
        try {
            return gitlab4jclient.getGitLabApi(url, token , username, password, userId).getProjectApi().getVariables(projectId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL,e.getMessage(), e);
        }
    }

    @Override
    public Variable createVariable(Integer projectId, String key, String value, boolean protecteds,
                                   Integer userId, String url, String token , String username, String password) {
        try {
            return gitlab4jclient.getGitLabApi(url, token , username, password, userId)
                    .getProjectApi().createVariable(projectId, key, value, protecteds);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public void deleteVariable(Integer projectId, String key, Integer userId, String url , String token ,String username, String password) {
        try {
            gitlab4jclient.getGitLabApi(url, token , username, password, userId)
                    .getProjectApi().deleteVariable(projectId, key);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public void batchDeleteVariable(Integer projectId, List<String> keys, Integer userId, String url , String token , String username, String password) {
        keys.forEach(key -> deleteVariable(projectId, key, userId, url, token, username, password));
    }

    @Override
    public List<Variable> batchCreateVariable(Integer projectId, List<Variable> list, Integer userId, String url, String token , String username, String password) {
        List<Variable> oldlist = getProjectVariable(projectId, userId, url, token, username, password);
        return list.stream().filter(t -> t.getValue() != null).map(v -> {
            try {
                String key = v.getKey();
                Optional<Variable> optional = oldlist.stream().filter(t -> key.equals(t.getKey())).findFirst();
                if (optional.isPresent() && !optional.get().getKey().isEmpty()) {
                    return gitlab4jclient.getGitLabApi(url, token , username, password, userId)
                            .getProjectApi().updateVariable(projectId, v.getKey(), v.getValue(), false);
                } else {
                    return gitlab4jclient.getGitLabApi(url, token , username, password, userId)
                            .getProjectApi().createVariable(projectId, v.getKey(), v.getValue(), false);
                }
            } catch (GitLabApiException e) {
                throw new BusinessRuntimeException(ABizCode.FAIL,e.getMessage(), e);
            }
        }).collect(Collectors.toList());
    }


    @Override
    public Project updateProject(Project newProject, Integer userId) {
        try {
            Project project = gitlab4jclient.getGitLabApi().getProjectApi().getProject(newProject.getId());
            project.setCiConfigPath(newProject.getCiConfigPath());
            return gitlab4jclient.getGitLabApi(userId)
                    .getProjectApi().updateProject(project);
        } catch (GitLabApiException e) {
            LOGGER.warn("Failed to update project, the user id is {} and project is [id={},ciConfigPath={}]", userId, newProject.getId(), newProject.getCiConfigPath());
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }


    @Override
    public void createDeployKey(Integer projectId, String title, String key, boolean canPush, Integer userId) {
        try {
            gitlab4jclient.getGitLabApi(userId).getDeployKeysApi().addDeployKey(projectId, title, key, canPush);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<DeployKey> getDeployKeys(Integer projectId, Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi(userId).getDeployKeysApi().getProjectDeployKeys(projectId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Member createMember(Integer projectId, Member member) {
        try {
            return gitlab4jclient.getGitLabApi().getProjectApi()
                    .addMember(projectId, member.getId(), member.getAccessLevel());
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<Member> updateMembers(Integer projectId, List<Member> list) {
        return list.stream().map(m -> {
            try {
                return gitlab4jclient.getGitLabApi().getProjectApi()
                        .updateMember(projectId, m.getId(), m.getAccessLevel());
            } catch (GitLabApiException e) {
                throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteMember(Integer projectId, Integer userId) {
        try {
            gitlab4jclient.getGitLabApi().getProjectApi().removeMember(projectId, userId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Member getMember(Integer projectId, Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi().getProjectApi().getMember(projectId, userId);
        } catch (GitLabApiException e) {
            LOGGER.error("no member found");
            Member member = new Member();
            member.setAccessLevel(AccessLevel.NONE);
            return member;
        }
    }

    @Override
    public List<Member> getAllMemberByProjectId(Integer projectId) {
        try {
            return gitlab4jclient.getGitLabApi().getProjectApi().getMembers(projectId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL,e.getMessage(), e);
        }
    }

    @Override
    public List<Project> getMemberProjects(Integer userId) {
        try {
            return gitlab4jclient.getGitLabApi(userId).getProjectApi().getMemberProjects();
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Integer check(String url, String token, String username, String password) {
        String projectCode = ProjectUtils.parseUrl(url).getRight();
        GitLabApi newGitLabApi = gitlab4jclient.getGitLabApi(url, token, username, password, null);
        User user;
        Project project;
        try {
            user = newGitLabApi.getUserApi().getCurrentUser();
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "访问授权失败");
        }
        try {
            List<Project> projectList =  newGitLabApi.getProjectApi().getProjects(projectCode);
            if(CollectionUtils.isEmpty(projectList)) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "该用户没有该代码仓库的权限");
            }
            project = projectList.stream().filter(p -> p.getHttpUrlToRepo().equals(url))
                    .findFirst()
                    .orElseThrow(()->new BusinessRuntimeException(ABizCode.FAIL, "该用户没有该代码仓库的权限"));
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "该用户没有该代码仓库的权限");
        }
        try {
           newGitLabApi.getProjectApi().getVariables(project.getId());
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "该用户在代码仓库不是Maintainer及以上权限");
        }
        try{
           newGitLabApi.getRepositoryApi().getBranch(project.getId(), "master");
        } catch (GitLabApiException e) {
            try {
                newGitLabApi.getRepositoryApi().getBranch(project.getId(), "main");
            } catch (GitLabApiException e1) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "该代码仓库没有master和main分支");
            }
        }
        return project.getId();
    }

}
