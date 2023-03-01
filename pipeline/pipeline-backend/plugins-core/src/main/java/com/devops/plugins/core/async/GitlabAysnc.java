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

package com.devops.plugins.core.async;

import com.devops.plugins.common.constraint.PlatFormConstraint;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.core.dao.entity.AppServiceConfigEntity;
import com.devops.plugins.core.dao.entity.AppServiceEntity;
import com.devops.plugins.core.pojo.vo.req.appService.AppServiceAddReq;
import com.devops.plugins.core.service.impl.AppServiceServiceImpl;
import com.devops.plugins.core.utils.Base64Util;
import com.devops.plugins.core.utils.FileUtil;
import com.devops.plugins.gitlab.pojo.FileCreationReq;
import com.devops.plugins.gitlab.pojo.FileDeleteReq;
import com.devops.plugins.gitlab.service.RepositoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.gitlab4j.api.models.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class GitlabAysnc {

    @Autowired
    RepositoryService repositoryService;



    @Async(value = "applicationTaskExecutor")
    public void deleteRepositoryFile(AppServiceEntity appServiceEntity, AppServiceConfigEntity appServiceConfigEntity) {
        List<Branch> branchesResult = repositoryService.listBranches(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntity.getToken(),appServiceConfigEntity.getUsername(), appServiceConfigEntity.getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntity.getPassword()));
        if (CollectionUtils.isNotEmpty(branchesResult)) {
            for(Branch branch:branchesResult) {
                FileDeleteReq dockerfileDelete = new FileDeleteReq();
                dockerfileDelete.setBranchName(branch.getName());
                dockerfileDelete.setPath("Dockerfile");
                dockerfileDelete.setCommitMessage("删除Dockerfile");
                repositoryService.deleteFile(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null,  appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntity.getToken(),appServiceConfigEntity.getUsername(), appServiceConfigEntity.getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntity.getPassword()), dockerfileDelete);

                FileDeleteReq gitlabCiDelete = new FileDeleteReq();
                gitlabCiDelete.setBranchName(branch.getName());
                gitlabCiDelete.setPath(".gitlab-ci.yml");
                gitlabCiDelete.setCommitMessage("删除gitlab-ci文件");
                repositoryService.deleteFile(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntity.getToken(), appServiceConfigEntity.getUsername(), appServiceConfigEntity.getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntity.getPassword()), gitlabCiDelete);
            }
        } else {
            throw new BusinessRuntimeException(ABizCode.FAIL, "查询应用服务分支失败");
        }
    }


    @Async(value = "applicationTaskExecutor")
    public void addRepositoryFile(List<Branch> branches, Integer projectId, AppServiceAddReq appServiceAddReq, String yamlContent, String type){
        //创建Dockerfile和gitlab-ci文件
        for (Branch branch : branches) {
            if(branch.getCanPush()) {
                try (InputStream inputStream = AppServiceServiceImpl.class.getClassLoader().getResourceAsStream("application/" + type + "/Dockerfile")) {
                    FileCreationReq fileCreationReq = new FileCreationReq();
                    fileCreationReq.setPath("Dockerfile");
                    fileCreationReq.setBranchName(branch.getName());
                    fileCreationReq.setContent(FileUtil.replaceReturnString(inputStream, new HashMap<>()));
                    fileCreationReq.setCommitMessage("初始化Dockerfile文件");
                    repositoryService.updateFile(projectId, null, appServiceAddReq.getUrl(), appServiceAddReq.getToken(), appServiceAddReq.getUsername(), appServiceAddReq.getPassword(), fileCreationReq);
                } catch (IOException e) {
                    throw new BusinessRuntimeException("找不到Dockerfile文件");
                }
            }
        }

        if (PlatFormConstraint.PIPELINE_TYPE_CUSTOM.equals(appServiceAddReq.getPipelineType())) {
            Map<String, String> params = new HashMap<>();
            params.put("{{auto.build}}", appServiceAddReq.getCode().replace("-", "_"));
            for (Branch branch : branches) {
                if(branch.getCanPush()) {
                    FileCreationReq fileCreationReq = new FileCreationReq();
                    fileCreationReq.setPath(".gitlab-ci.yml");
                    fileCreationReq.setBranchName(branch.getName());
                    fileCreationReq.setContent(FileUtil.replaceReturnString(IOUtils.toInputStream(yamlContent, StandardCharsets.UTF_8), params));
                    fileCreationReq.setCommitMessage("初始化.gitlab-ci.yml文件");
                    repositoryService.updateFile(projectId, null, appServiceAddReq.getUrl(), appServiceAddReq.getToken(), appServiceAddReq.getUsername(), appServiceAddReq.getPassword(), fileCreationReq);
                }
            }
        } else {
            for (Branch branch : branches) {
                if(branch.getCanPush()) {
                    try (InputStream inputStream = AppServiceServiceImpl.class.getClassLoader().getResourceAsStream("application/" + type + "/gitlab-ci.yml")) {
                        Map<String, String> params = new HashMap<>();
                        params.put("{{service.code}}", appServiceAddReq.getCode());
                        params.put("{{auto.build}}", appServiceAddReq.getCode().replace("-", "_"));
                        FileCreationReq fileCreationReq = new FileCreationReq();
                        fileCreationReq.setPath(".gitlab-ci.yml");
                        fileCreationReq.setBranchName(branch.getName());
                        fileCreationReq.setContent(FileUtil.replaceReturnString(inputStream, params));
                        fileCreationReq.setCommitMessage("初始化.gitlab-ci.yml文件");
                        repositoryService.updateFile(projectId, null, appServiceAddReq.getUrl(), appServiceAddReq.getToken(), appServiceAddReq.getUsername(), appServiceAddReq.getPassword(), fileCreationReq);
                    } catch (Exception e) {
                        throw new BusinessRuntimeException(ABizCode.FAIL, "创建.gitlab-ci文件失败");
                    }
                }
            }
        }


    }

}
