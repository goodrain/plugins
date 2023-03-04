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

package com.devops.plugins.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devops.plugins.common.constraint.HeaderParamsConstants;
import com.devops.plugins.common.constraint.PlatFormConstraint;
import com.devops.plugins.common.context.SecurityContextHolder;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.common.result.R;
import com.devops.plugins.core.clients.PaasClient;
import com.devops.plugins.core.config.HarborConfig;
import com.devops.plugins.core.dao.*;
import com.devops.plugins.core.dao.entity.*;
import com.devops.plugins.core.pojo.rainbond.RainbondApp;
import com.devops.plugins.core.pojo.rainbond.RainbondService;
import com.devops.plugins.core.pojo.rainbond.RainbondServiceAdd;
import com.devops.plugins.core.pojo.rainbond.RainbondServiceBuild;
import com.devops.plugins.core.pojo.vo.req.appServiceVersion.AppServiceVersionPageReq;
import com.devops.plugins.core.pojo.vo.req.appServiceVersion.DeployReq;
import com.devops.plugins.core.pojo.vo.resp.app.AppResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.AppServiceVersionPageResp;
import com.devops.plugins.core.service.IAppServiceVersionService;
import com.devops.plugins.core.service.ICustomPipelineService;
import com.devops.plugins.core.service.ICustomPipelineTempService;
import com.devops.plugins.core.utils.Base64Util;
import com.devops.plugins.core.utils.ConvertUtils;
import com.devops.plugins.core.utils.DateUtil;
import com.devops.plugins.gitlab.service.UserService;
import com.sun.imageio.spi.RAFImageOutputStreamSpi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.gitlab4j.api.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 应用服务版本表 Service服务实现类
 * </p>
 *
 * @author huangtf
 * @Date 2022-01-06 18:25:34
 */
@Service
@Transactional
public class AppServiceVersionServiceImpl extends ServiceImpl<AppServiceVersionMapper, AppServiceVersionEntity> implements IAppServiceVersionService {

    private static Logger log = LoggerFactory.getLogger(AppServiceVersionServiceImpl.class);

    @Value("${plugins.rainbond.url}")
    private String paasUrl;
    @Value("${plugins.rainbond.token}")
    private String token;

    @Autowired
    private AppServiceVersionMapper appServiceVersionMapper;
    @Autowired
    private AppServiceMapper appServiceMapper;
    @Autowired
    private GitlabCommitMapper gitlabCommitMapper;
    @Autowired
    private SubAppServiceMapper subAppServiceMapper;
    @Autowired
    private AppServiceDeployHistoryMapper appServiceDeployHistoryMapper;
    @Autowired
    private AppServiceConfigMapper appServiceConfigMapper;
    @Autowired
    private HarborConfig harborConfig;
    @Autowired
    private PaasClient paasClient;
    @Autowired
    private UserService userService;
    @Autowired
    private AppServiceComponentMapper appServiceComponentMapper;
    @Autowired
    private AppServiceAutoDeployMapper appServiceAutoDeployMapper;
    @Autowired
    private CustomPipelineRelationMapper customPipelineRelationMapper;
    @Autowired
    private CustomPipelineEnvsMapper customPipelineEnvsMapper;
    @Autowired
    private ICustomPipelineTempService customPipelineTempService;


    @Override
    @Transactional(readOnly = true)
    public Page<AppServiceVersionPageResp> findPage(AppServiceVersionPageReq query, ReqPage<AppServiceVersionPageReq> page) {
        return ConvertUtils.convertPage(appServiceVersionMapper.findPage(query, ConvertUtils.convertReqPage(page, new Page<>())), AppServiceVersionPageResp.class);
    }


    @Override
    public void create(String gitlabProjectId, String version, String commit, String branch, String module, String image) {
        log.info("·················生成应用版本····················");
        //设置admin用户操作
        LambdaQueryWrapper<AppServiceEntity> projectIdWrapper = new LambdaQueryWrapper<>();
        projectIdWrapper.eq(AppServiceEntity::getGitlabProjectId, gitlabProjectId);
        AppServiceEntity appServiceEntity = appServiceMapper.selectOne(projectIdWrapper);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }

        //微服务版本镜像加上子模块的编码，方便区分版本进行部署
        if (StringUtils.isNotBlank(module)) {
            version = String.format("%s-%s", module, version);
        }

        LambdaQueryWrapper<AppServiceVersionEntity> versionWrapper = new LambdaQueryWrapper<>();
        versionWrapper.eq(AppServiceVersionEntity::getAppServiceVersion, version);
        AppServiceVersionEntity appServiceVersionEntity = appServiceVersionMapper.selectOne(versionWrapper);
        if (appServiceVersionEntity == null) {
            appServiceVersionEntity = new AppServiceVersionEntity();
            appServiceVersionEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            appServiceVersionEntity.setAppServiceId(appServiceEntity.getId());
            appServiceVersionEntity.setAppServiceVersion(version);
            appServiceVersionEntity.setCommitValue(commit);
            appServiceVersionEntity.setImage(image);

            LambdaQueryWrapper<GitlabCommitEntity> gitlabCommitEntityQueryWrapper = new LambdaQueryWrapper<>();
            gitlabCommitEntityQueryWrapper.eq(GitlabCommitEntity::getCommitSha, commit);
            List<GitlabCommitEntity> gitlabCommitEntities = gitlabCommitMapper.selectList(gitlabCommitEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(gitlabCommitEntities)) {
                appServiceVersionEntity.setCommitMsg(gitlabCommitEntities.get(0).getCommitContent());
                if (appServiceEntity.getServiceType().equals(PlatFormConstraint.SERVICE_TYPE_EXTERNAL)) {
                    LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
                    appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, appServiceEntity.getId());
                    List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
                    if (CollectionUtils.isNotEmpty(appServiceConfigEntities)) {
                        User userR = userService.queryUserByUserId(Integer.parseInt(gitlabCommitEntities.get(0).getUserId()), appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
                        if (userR != null) {
                            appServiceVersionEntity.setCommitter(userR.getName());
                        }
                    }
                }
                appServiceVersionEntity.setCommitTime(gitlabCommitEntities.get(0).getCommitDate());
            }
            appServiceVersionMapper.insert(appServiceVersionEntity);
        }


        //自动部署逻辑
        List<AppServiceAutoDeployEntity> appServiceAutoDeployEntities;
        if (StringUtils.isNotBlank(module)) {
            LambdaQueryWrapper<SubAppServiceEntity> subAppServiceEntityQueryWrapper = new LambdaQueryWrapper<>();
            subAppServiceEntityQueryWrapper.eq(SubAppServiceEntity::getAppServiceId, appServiceEntity.getId());
            subAppServiceEntityQueryWrapper.eq(SubAppServiceEntity::getCode, module);
            List<SubAppServiceEntity> subAppServiceEntities = subAppServiceMapper.selectList(subAppServiceEntityQueryWrapper);
            if (CollectionUtils.isEmpty(subAppServiceEntities)) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "应用子模块不存在");
            }

            LambdaQueryWrapper<AppServiceAutoDeployEntity> appServiceAutoDeployEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceAutoDeployEntityQueryWrapper.eq(AppServiceAutoDeployEntity::getSubAppServiceId, subAppServiceEntities.get(0).getId());
            appServiceAutoDeployEntities = appServiceAutoDeployMapper.selectList(appServiceAutoDeployEntityQueryWrapper);

        } else {
            LambdaQueryWrapper<AppServiceAutoDeployEntity> appServiceAutoDeployEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceAutoDeployEntityQueryWrapper.eq(AppServiceAutoDeployEntity::getAppServiceId, appServiceEntity.getId());
            appServiceAutoDeployEntityQueryWrapper.isNull(AppServiceAutoDeployEntity::getSubAppServiceId);
            appServiceAutoDeployEntities = appServiceAutoDeployMapper.selectList(appServiceAutoDeployEntityQueryWrapper);

        }

        if (CollectionUtils.isNotEmpty(appServiceAutoDeployEntities)) {
            for (AppServiceAutoDeployEntity appServiceAutoDeployEntity : appServiceAutoDeployEntities) {
                //未部署过，没有部署信息则无法自动部署
                SecurityContextHolder.getLocalMap().put(HeaderParamsConstants.AUTHORIZATION, token);
                DeployReq deployReq = new DeployReq();
                deployReq.setAppServiceId(appServiceEntity.getId());
                deployReq.setVersion(version);
                deployReq.setUsername(appServiceVersionEntity.getCommitter());
                deployReq.setRegionCode(appServiceAutoDeployEntity.getRegionCode());
                deploy(deployReq);

            }
        }
    }

    @Override
    public List<AppResp> listApps(String appServiceId, String teamId, String regionName, String version) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(appServiceId);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }

        LambdaQueryWrapper<AppServiceVersionEntity> serviceVersionEntityQueryWrapper = new LambdaQueryWrapper<>();
        serviceVersionEntityQueryWrapper.eq(AppServiceVersionEntity::getAppServiceId, appServiceId);
        serviceVersionEntityQueryWrapper.eq(AppServiceVersionEntity::getAppServiceVersion, version);
        List<AppServiceVersionEntity> appServiceVersionEntities = appServiceVersionMapper.selectList(serviceVersionEntityQueryWrapper);
        if (CollectionUtils.isEmpty(appServiceVersionEntities)) {
            throw new BusinessRuntimeException("应用服务版本不存在");
        }

        List<AppServiceComponentEntity> existComponents;
        String type = customPipelineTempService.getPiPelineTempName(appServiceId, null);
        if (type != null && type.equals(PlatFormConstraint.AppService_Template_Microservice)) {
            String module = version.split(String.format("-%s", DateUtil.getYear(appServiceVersionEntities.get(0).getCreateTime())))[0];
            LambdaQueryWrapper<SubAppServiceEntity> subAppServiceQueryWrapper = new LambdaQueryWrapper<>();
            subAppServiceQueryWrapper.eq(SubAppServiceEntity::getAppServiceId, appServiceId);
            subAppServiceQueryWrapper.eq(SubAppServiceEntity::getCode, module);
            List<SubAppServiceEntity> subAppServiceEntities = subAppServiceMapper.selectList(subAppServiceQueryWrapper);
            if (CollectionUtils.isEmpty(subAppServiceEntities)) {
                throw new BusinessRuntimeException("微服务子模块不存在");
            }
            LambdaQueryWrapper<AppServiceComponentEntity> existComponentQueryWrapper = new LambdaQueryWrapper<>();
            existComponentQueryWrapper.eq(AppServiceComponentEntity::getSubAppServiceId, subAppServiceEntities.get(0).getId());
            existComponentQueryWrapper.eq(AppServiceComponentEntity::getRegionCode, regionName);
            existComponents = appServiceComponentMapper.selectList(existComponentQueryWrapper);

        } else {
            LambdaQueryWrapper<AppServiceComponentEntity> existComponentQueryWrapper = new LambdaQueryWrapper<>();
            existComponentQueryWrapper.eq(AppServiceComponentEntity::getAppServiceId, appServiceEntity.getId());
            existComponentQueryWrapper.eq(AppServiceComponentEntity::getRegionCode, regionName);
            existComponents = appServiceComponentMapper.selectList(existComponentQueryWrapper);
        }
        //如果应用服务没部署过，则选择应用新建组件
        List<RainbondApp> rainbondApps = paasClient.listApps(teamId, regionName);
        if (CollectionUtils.isEmpty(rainbondApps)) {
            throw new BusinessRuntimeException("团队下无可用的应用,请先创建应用");
        }
        if (CollectionUtils.isNotEmpty(existComponents)) {
            if (rainbondApps.stream().noneMatch(rainbondApp -> rainbondApp.getAppId().equals(existComponents.get(0).getPaasAppId()))) {
                return ConvertUtils.convertList(rainbondApps, AppResp.class);
            } else {
                return new ArrayList<>();
            }
        }
        return ConvertUtils.convertList(rainbondApps, AppResp.class);
    }


    @Override
    public void deploy(DeployReq deployReq) {
        AppServiceDeployHistoryEntity appServiceDeployHistoryEntity = new AppServiceDeployHistoryEntity();
        appServiceDeployHistoryEntity.setAppServiceId(deployReq.getAppServiceId());
        appServiceDeployHistoryEntity.setAppServiceVersion(deployReq.getVersion());
        appServiceDeployHistoryEntity.setRegionCode(deployReq.getRegionCode());
        appServiceDeployHistoryEntity.setDescription(deployReq.getDescription());
        String componentName = null;
        String componentCode = null;
        String appId = null;
        List<AppServiceDeployHistoryEntity> deployHistoryEntities;
        AppServiceComponentEntity appServiceComponentEntity = null;
        List<AppServiceComponentEntity> existComponents;
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(deployReq.getAppServiceId());
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException("应用服务不存在");
        }


        LambdaQueryWrapper<AppServiceVersionEntity> serviceVersionEntityQueryWrapper = new LambdaQueryWrapper<>();
        serviceVersionEntityQueryWrapper.eq(AppServiceVersionEntity::getAppServiceId, deployReq.getAppServiceId());
        serviceVersionEntityQueryWrapper.eq(AppServiceVersionEntity::getAppServiceVersion, deployReq.getVersion());
        List<AppServiceVersionEntity> appServiceVersionEntities = appServiceVersionMapper.selectList(serviceVersionEntityQueryWrapper);
        if (CollectionUtils.isEmpty(appServiceVersionEntities)) {
            throw new BusinessRuntimeException("应用服务版本不存在");
        }

        //根据应用服务类型获取相关值
        String type = customPipelineTempService.getPiPelineTempName(deployReq.getAppServiceId(), null);
        if (type != null && type.equals(PlatFormConstraint.AppService_Template_Microservice)) {
            String module = deployReq.getVersion().split(String.format("-%s", DateUtil.getYear(appServiceVersionEntities.get(0).getCreateTime())))[0];
            LambdaQueryWrapper<SubAppServiceEntity> subAppServiceQueryWrapper = new LambdaQueryWrapper<>();
            subAppServiceQueryWrapper.eq(SubAppServiceEntity::getAppServiceId, deployReq.getAppServiceId());
            subAppServiceQueryWrapper.eq(SubAppServiceEntity::getCode, module);
            List<SubAppServiceEntity> subAppServiceEntities = subAppServiceMapper.selectList(subAppServiceQueryWrapper);
            if (CollectionUtils.isEmpty(subAppServiceEntities)) {
                throw new BusinessRuntimeException("微服务子模块不存在");
            }

            componentName = subAppServiceEntities.get(0).getCode().replace("_", "-");
            appServiceDeployHistoryEntity.setSubAppServiceId(subAppServiceEntities.get(0).getId());

            LambdaQueryWrapper<AppServiceDeployHistoryEntity> deployHistoryEntityQueryWrapper = new LambdaQueryWrapper<>();
            deployHistoryEntityQueryWrapper.eq(AppServiceDeployHistoryEntity::getSubAppServiceId, subAppServiceEntities.get(0).getId());
            deployHistoryEntityQueryWrapper.eq(AppServiceDeployHistoryEntity::getRegionCode, deployReq.getRegionCode());
            deployHistoryEntities = appServiceDeployHistoryMapper.selectList(deployHistoryEntityQueryWrapper);


            LambdaQueryWrapper<AppServiceComponentEntity> existComponentQueryWrapper = new LambdaQueryWrapper<>();
            existComponentQueryWrapper.eq(AppServiceComponentEntity::getSubAppServiceId, subAppServiceEntities.get(0).getId());
            existComponentQueryWrapper.eq(AppServiceComponentEntity::getRegionCode, deployReq.getRegionCode());
            existComponents = appServiceComponentMapper.selectList(existComponentQueryWrapper);
            if (CollectionUtils.isEmpty(existComponents)) {
                appServiceComponentEntity = new AppServiceComponentEntity();
                appServiceComponentEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                appServiceComponentEntity.setRegionCode(deployReq.getRegionCode());
                appServiceComponentEntity.setPaasComponentCode(componentName);
                appServiceComponentEntity.setSubAppServiceId(subAppServiceEntities.get(0).getId());
                appServiceComponentEntity.setPaasAppId(deployReq.getAppId());
                appServiceComponentEntity.setTeamCode(appServiceEntity.getTeamCode());
                appId = deployReq.getAppId();
            } else {
                appServiceComponentEntity = existComponents.get(0);
                componentCode = existComponents.get(0).getPaasComponentId();
                appId = existComponents.get(0).getPaasAppId();
            }

        } else {

            componentName = appServiceEntity.getCode().replace("_", "-");

            LambdaQueryWrapper<AppServiceComponentEntity> existComponentQueryWrapper = new LambdaQueryWrapper<>();
            existComponentQueryWrapper.eq(AppServiceComponentEntity::getAppServiceId, appServiceEntity.getId());
            existComponentQueryWrapper.eq(AppServiceComponentEntity::getRegionCode, deployReq.getRegionCode());
            existComponents = appServiceComponentMapper.selectList(existComponentQueryWrapper);
            if (CollectionUtils.isEmpty(existComponents)) {
                appServiceComponentEntity = new AppServiceComponentEntity();
                appServiceComponentEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                appServiceComponentEntity.setRegionCode(deployReq.getRegionCode());
                appServiceComponentEntity.setAppServiceId(appServiceEntity.getId());
                appServiceComponentEntity.setPaasComponentCode(componentName);
                appServiceComponentEntity.setPaasAppId(deployReq.getAppId());
                appServiceComponentEntity.setTeamCode(appServiceEntity.getTeamCode());
                appId = deployReq.getAppId();
            } else {
                appServiceComponentEntity = existComponents.get(0);
                componentCode = existComponents.get(0).getPaasComponentId();
                appId = existComponents.get(0).getPaasAppId();
            }

            LambdaQueryWrapper<AppServiceDeployHistoryEntity> deployHistoryEntityQueryWrapper = new LambdaQueryWrapper<>();
            deployHistoryEntityQueryWrapper.eq(AppServiceDeployHistoryEntity::getAppServiceId, deployReq.getAppServiceId());
            deployHistoryEntityQueryWrapper.eq(AppServiceDeployHistoryEntity::getRegionCode, deployReq.getRegionCode());
            deployHistoryEntities = appServiceDeployHistoryMapper.selectList(deployHistoryEntityQueryWrapper);

        }

        String imageusername;
        String imagepassword;

        if (harborConfig.getEnable()) {
            imageusername = harborConfig.getUsername();
            imagepassword = harborConfig.getPassword();
        } else {
            LambdaQueryWrapper<CustomPipelineRelationEntity> customPipelineRelationEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineRelationEntityLambdaQueryWrapper.eq(CustomPipelineRelationEntity::getAppServiceId, appServiceEntity.getId());
            List<CustomPipelineRelationEntity> customPipelineRelationEntities = customPipelineRelationMapper.selectList(customPipelineRelationEntityLambdaQueryWrapper);

            LambdaQueryWrapper<CustomPipelineEnvsEntity> customPipelineEnvsEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineEnvsEntityLambdaQueryWrapper.eq(CustomPipelineEnvsEntity::getPipelineId, customPipelineRelationEntities.get(0).getPipelineId());
            List<CustomPipelineEnvsEntity> customPipelineEnvsEntities = customPipelineEnvsMapper.selectList(customPipelineEnvsEntityLambdaQueryWrapper);


            imageusername = customPipelineEnvsEntities.stream().filter(customPipelineEnvsEntity -> customPipelineEnvsEntity.getEnvKey().contains("USERNAME")).findFirst().orElse(new CustomPipelineEnvsEntity()).getEnvValue();
            imagepassword = customPipelineEnvsEntities.stream().filter(customPipelineEnvsEntity -> customPipelineEnvsEntity.getEnvKey().contains("PASSWORD")).findFirst().orElse(new CustomPipelineEnvsEntity()).getEnvValue();
        }

        if (StringUtils.isNotBlank(deployReq.getAppId())) {

            //没有部署记录新增组件构建组件
            RainbondServiceAdd serviceAdd = new RainbondServiceAdd();
            serviceAdd.setGroupId(Integer.parseInt(deployReq.getAppId()));
            serviceAdd.setIsDeploy(true);
            serviceAdd.setServiceCname(componentName);
            serviceAdd.setK8sComponentName(componentName);
            serviceAdd.setImage(appServiceVersionEntities.get(0).getImage());
            serviceAdd.setUsername(imageusername);
            serviceAdd.setPassword(imagepassword);
            MutablePair<Boolean, String> result = paasClient.serviceAdd(appServiceEntity.getTeamCode(), deployReq.getRegionCode(), deployReq.getAppId(), serviceAdd);
            //获取组件组件详情
            if (result.getLeft()) {
                appServiceDeployHistoryEntity.setStatus(PlatFormConstraint.DEPLOY_SUCCESS);
                RainbondService rainbondService = paasClient.serviceGet(appServiceEntity.getTeamCode(), deployReq.getRegionCode(), deployReq.getAppId(), result.getRight());
                appServiceComponentEntity.setPaasComponentId(rainbondService.getServiceAlias());
                paasUrl = paasUrl.endsWith("/") ? paasUrl : paasUrl + "/";
                appServiceDeployHistoryEntity.setDeployInfoUrl(String.format("%s#/team/%s/region/%s/components/%s/overview", paasUrl, appServiceComponentEntity.getTeamCode(), deployReq.getRegionCode(), rainbondService.getServiceAlias()));
                appServiceDeployHistoryEntity.setTeamCode(appServiceComponentEntity.getTeamCode());
                appServiceDeployHistoryEntity.setComponentCode(rainbondService.getServiceAlias());
            } else {
                appServiceDeployHistoryEntity.setStatus(PlatFormConstraint.DEPLOY_FAILED);
                appServiceDeployHistoryEntity.setDescription(result.getRight());
            }
            if(CollectionUtils.isNotEmpty(existComponents)) {
                appServiceComponentEntity.setPaasAppId(deployReq.getAppId());
                appServiceComponentMapper.updateById(appServiceComponentEntity);
            } else {
                appServiceComponentMapper.insert(appServiceComponentEntity);
            }
        } else {
            appServiceDeployHistoryEntity.setDeployInfoUrl(deployHistoryEntities.get(0).getDeployInfoUrl());
            appServiceDeployHistoryEntity.setTeamCode(deployHistoryEntities.get(0).getTeamCode());
            appServiceDeployHistoryEntity.setComponentCode(deployHistoryEntities.get(0).getComponentCode());

            //判断组件是不是被删除，被删除重新创建
            List<RainbondService> rainbondServices = paasClient.serviceGet(appServiceEntity.getTeamCode(), deployReq.getRegionCode(), appId);
            if(CollectionUtils.isEmpty(rainbondServices)||rainbondServices.stream().noneMatch(rainbondService -> rainbondService.getServiceAlias().equals(existComponents.get(0).getPaasComponentId()))) {
                //没有部署记录新增组件构建组件
                RainbondServiceAdd serviceAdd = new RainbondServiceAdd();
                serviceAdd.setGroupId(Integer.parseInt(appId));
                serviceAdd.setIsDeploy(true);
                serviceAdd.setServiceCname(componentName);
                serviceAdd.setK8sComponentName(componentName);
                serviceAdd.setImage(appServiceVersionEntities.get(0).getImage());
                serviceAdd.setUsername(imageusername);
                serviceAdd.setPassword(imagepassword);
                MutablePair<Boolean, String> result = paasClient.serviceAdd(appServiceEntity.getTeamCode(), deployReq.getRegionCode(), appId, serviceAdd);
                //获取组件组件详情
                if (result.getLeft()) {
                    appServiceDeployHistoryEntity.setStatus(PlatFormConstraint.DEPLOY_SUCCESS);
                    RainbondService rainbondService = paasClient.serviceGet(appServiceEntity.getTeamCode(), deployReq.getRegionCode(), appId, result.getRight());
                    appServiceComponentEntity.setPaasComponentId(rainbondService.getServiceAlias());
                    paasUrl = paasUrl.endsWith("/") ? paasUrl : paasUrl + "/";
                    appServiceDeployHistoryEntity.setDeployInfoUrl(String.format("%s#/team/%s/region/%s/components/%s/overview", paasUrl, appServiceComponentEntity.getTeamCode(), deployReq.getRegionCode(), rainbondService.getServiceAlias()));
                    appServiceDeployHistoryEntity.setTeamCode(appServiceComponentEntity.getTeamCode());
                    appServiceDeployHistoryEntity.setComponentCode(rainbondService.getServiceAlias());
                } else {
                    appServiceDeployHistoryEntity.setStatus(PlatFormConstraint.DEPLOY_FAILED);
                    appServiceDeployHistoryEntity.setDescription(result.getRight());
                }
                appServiceComponentMapper.updateById(appServiceComponentEntity);
            } else {
                //有部署记录构建组件
                RainbondServiceBuild serviceBuildReq = new RainbondServiceBuild();
                serviceBuildReq.setDockerImage(appServiceVersionEntities.get(0).getImage());
                serviceBuildReq.setRegistryUser(imageusername);
                serviceBuildReq.setRegistryPassword(imagepassword);
                MutablePair<Boolean, String> result = paasClient.serviceBuild(appServiceEntity.getTeamCode(), deployReq.getRegionCode(), appId, componentCode, serviceBuildReq);
                if (result.getLeft()) {
                    appServiceDeployHistoryEntity.setStatus(PlatFormConstraint.DEPLOY_SUCCESS);
                } else {
                    appServiceDeployHistoryEntity.setStatus(PlatFormConstraint.DEPLOY_FAILED);
                    appServiceDeployHistoryEntity.setDescription(result.getRight());
                }
            }
        }

        //新增部署历史
        if (StringUtils.isNotBlank(deployReq.getUsername())) {
            appServiceDeployHistoryEntity.setCreateBy(deployReq.getUsername());
        } else {
            appServiceDeployHistoryEntity.setCreateBy(paasClient.getNickName());
        }
        appServiceDeployHistoryMapper.insert(appServiceDeployHistoryEntity);

    }


}
