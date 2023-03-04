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

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.devops.plugins.common.constraint.PlatFormConstraint;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.core.async.GitlabAysnc;
import com.devops.plugins.core.clients.HarborClient;
import com.devops.plugins.core.clients.PaasClient;
import com.devops.plugins.core.config.HarborConfig;
import com.devops.plugins.core.dao.*;
import com.devops.plugins.core.dao.entity.*;
import com.devops.plugins.core.pojo.rainbond.RainbondRegionPageResult;
import com.devops.plugins.core.pojo.rainbond.RainbondRegion;
import com.devops.plugins.core.pojo.vo.req.appService.*;
import com.devops.plugins.core.pojo.vo.req.appService.subAppService.SubAppServiceDeployEnvVO;
import com.devops.plugins.core.pojo.vo.req.appService.subAppService.SubAppServiceVO;
import com.devops.plugins.core.pojo.vo.resp.appService.*;
import com.devops.plugins.core.pojo.vo.resp.team.TeamResp;
import com.devops.plugins.core.service.*;
import com.devops.plugins.core.utils.Base64Util;
import com.devops.plugins.core.utils.ConvertUtils;
import com.devops.plugins.core.utils.FileUtil;
import com.devops.plugins.gitlab.pojo.FileCreationReq;
import com.devops.plugins.gitlab.pojo.FileDeleteReq;
import com.devops.plugins.gitlab.service.HookService;
import com.devops.plugins.gitlab.service.ProjectService;
import com.devops.plugins.gitlab.service.RepositoryService;
import liquibase.pro.packaged.V;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.gitlab4j.api.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用服务表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-01-06 10:07:19
 */
@Service
@Transactional
public class AppServiceServiceImpl extends ServiceImpl<AppServiceMapper, AppServiceEntity> implements IAppServiceService {


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private AppServiceMapper appServiceMapper;
    @Autowired
    private ISubAppServiceService subAppServiceService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private HookService hookService;
    @Autowired
    private HarborConfig harborConfig;
    @Autowired
    private SubAppServiceMapper subAppServiceMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private AppServiceConfigMapper appServiceConfigMapper;
    @Autowired
    private PaasClient paasClient;
    @Autowired
    private AppServiceAutoDeployMapper appServiceAutoDeployMapper;
    @Autowired
    private IAppServiceAutoDeployService appServiceAutoDeployService;
    @Autowired
    private IAppServiceComponentService appServiceComponentService;
    @Autowired
    private CustomPipelineYamlMapper customPipelineYamlMapper;
    @Autowired
    private CustomPipelineRelationMapper customPipelineRelationMapper;
    @Autowired
    private CustomPipelineMapper customPipelineMapper;
    @Autowired
    private IGitlabBranchService gitlabBranchService;
    @Autowired
    private GitlabBranchMapper gitlabBranchMapper;
    @Autowired
    private IAppServicePipelineService appServicePipelineService;
    @Autowired
    private AppServiceDeployHistoryMapper appServiceDeployHistoryMapper;
    @Autowired
    private HarborClient harborClient;
    @Autowired
    private GitlabAysnc gitlabAysnc;
    @Autowired
    private CustomPipelineEnvsMapper customPipelineEnvsMapper;
    @Autowired
    private ICustomPipelineTempService customPipelineTempService;
    @Autowired
    private TeamService teamService;


    @Value("${plugins.backend.url}")
    private String gatewayUrl;



    private static List<String> templates = new ArrayList<>();

    static {
        templates.add("maven父子模块");
        templates.add("其它类型");
    }


    @Override
    public Long findCount(AppServiceEntity query) {
        return appServiceMapper.findCount(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppServicePageResp> findPage(AppServicePageReq query, ReqPage<AppServicePageReq> page) {
        Page<AppServicePageResp> result = new Page<>();

        Page<AppServiceEntity> appServiceEntityPage = appServiceMapper.findPage(ConvertUtils.convertObject(query, AppServiceEntity.class), ConvertUtils.convertReqPage(page, new Page<>()));
        List<AppServiceEntity> appServiceEntities = appServiceEntityPage.getRecords();
        if (CollectionUtils.isEmpty(appServiceEntities)) {
            return result;
        }

        appServiceEntityPage.setRecords(appServiceEntities.stream().sorted(Comparator.comparing(AppServiceEntity::getCreateTime).reversed()).collect(Collectors.toList()));
        result = ConvertUtils.convertPage(appServiceEntityPage, AppServicePageResp.class);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppServiceEntity> findList(AppServiceEntity query) {
        return appServiceMapper.findList(query);
    }

    @Override
    public boolean update(AppServiceUpdateReq appServiceUpdateReq) {
        appServiceUpdateReq.setPipelineType(PlatFormConstraint.PIPELINE_TYPE_CUSTOM);

        AppServiceEntity appServiceEntity = new AppServiceEntity();
        appServiceEntity.setId(appServiceUpdateReq.getId());
        appServiceEntity.setName(appServiceUpdateReq.getName());
        appServiceEntity.setAutoBuild(appServiceUpdateReq.getAutoBuild() == null ? false : appServiceUpdateReq.getAutoBuild());
        appServiceEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        appServiceEntity.setPipelineType(appServiceUpdateReq.getPipelineType());
        appServiceEntity.setStatus(appServiceUpdateReq.getStatus());

        AppServiceEntity oldAppServiceEntity = appServiceMapper.selectById(appServiceUpdateReq.getId());
        if (oldAppServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }

        if (appServiceUpdateReq.getName() != null && !appServiceUpdateReq.getName().equals(oldAppServiceEntity.getName())) {
            LambdaQueryWrapper<AppServiceEntity> appServiceEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceEntityQueryWrapper.eq(AppServiceEntity::getName, appServiceUpdateReq.getName());
            List<AppServiceEntity> exists = appServiceMapper.selectList(appServiceEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(exists)) {
                throw new BusinessRuntimeException("应用服务名称已存在");
            }
        }

        //自动部署和手动部署设置
        String oldType = customPipelineTempService.getPiPelineTempName(oldAppServiceEntity.getId(), null);
        if (oldType != null && oldType.equals(PlatFormConstraint.AppService_Template_Microservice)) {
            if (BooleanUtils.isFalse(appServiceUpdateReq.getAutoDeploy())) {
                LambdaQueryWrapper<AppServiceAutoDeployEntity> appServiceAutoDeployEntityQueryWrapper = new LambdaQueryWrapper<>();
                appServiceAutoDeployEntityQueryWrapper.eq(AppServiceAutoDeployEntity::getAppServiceId, appServiceUpdateReq.getId());
                appServiceAutoDeployMapper.delete(appServiceAutoDeployEntityQueryWrapper);
            } else {
                appServiceUpdateReq.getSubAppServiceDeployEnvVOS().stream().forEach(subAppServiceDeployEnvVO -> {
                    if (StringUtils.isNotBlank(subAppServiceDeployEnvVO.getRegion_code())) {
                        List<AppServiceAutoDeployEntity> appServiceAutoDeployEntities = new ArrayList<>();
                        LambdaQueryWrapper<AppServiceAutoDeployEntity> appServiceAutoDeployEntityQueryWrapper = new LambdaQueryWrapper<>();
                        appServiceAutoDeployEntityQueryWrapper.eq(AppServiceAutoDeployEntity::getAppServiceId, appServiceUpdateReq.getId());
                        appServiceAutoDeployEntityQueryWrapper.eq(AppServiceAutoDeployEntity::getRegionCode, subAppServiceDeployEnvVO.getRegion_code());
                        subAppServiceDeployEnvVO.getSubAppServiceEntities().stream().forEach(subAppServiceDeployVO -> {
                            if (subAppServiceDeployVO.getAuto_deploy()) {
                                AppServiceAutoDeployEntity appServiceAutoDeployEntity = new AppServiceAutoDeployEntity();
                                appServiceAutoDeployEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                                appServiceAutoDeployEntity.setAppServiceId(appServiceUpdateReq.getId());
                                appServiceAutoDeployEntity.setSubAppServiceId(subAppServiceDeployVO.getId());
                                appServiceAutoDeployEntity.setRegionCode(subAppServiceDeployEnvVO.getRegion_code());
                                appServiceAutoDeployEntities.add(appServiceAutoDeployEntity);
                            }

                        });
                        appServiceAutoDeployMapper.delete(appServiceAutoDeployEntityQueryWrapper);
                        if (CollectionUtils.isNotEmpty(appServiceAutoDeployEntities)) {
                            appServiceAutoDeployService.saveBatch(appServiceAutoDeployEntities);
                        }
                    }
                });
            }
        } else {
            LambdaQueryWrapper<AppServiceAutoDeployEntity> appServiceAutoDeployEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceAutoDeployEntityQueryWrapper.eq(AppServiceAutoDeployEntity::getAppServiceId, appServiceUpdateReq.getId());
            if (BooleanUtils.isTrue(appServiceUpdateReq.getAutoDeploy())) {
                List<AppServiceAutoDeployEntity> appServiceAutoDeployEntities = new ArrayList<>();
                appServiceUpdateReq.getRegionCodes().stream().forEach(regionCode -> {
                    AppServiceAutoDeployEntity appServiceAutoDeployEntity = new AppServiceAutoDeployEntity();
                    appServiceAutoDeployEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                    appServiceAutoDeployEntity.setAppServiceId(appServiceUpdateReq.getId());
                    appServiceAutoDeployEntity.setRegionCode(regionCode);
                    appServiceAutoDeployEntities.add(appServiceAutoDeployEntity);
                });
                appServiceAutoDeployMapper.delete(appServiceAutoDeployEntityQueryWrapper);
                appServiceAutoDeployService.saveBatch(appServiceAutoDeployEntities);
            } else {
                appServiceAutoDeployMapper.delete(appServiceAutoDeployEntityQueryWrapper);
            }
        }


        if (PlatFormConstraint.PIPELINE_TYPE_INTENAL.equals(appServiceUpdateReq.getPipelineType()) && PlatFormConstraint.PIPELINE_TYPE_CUSTOM.equals(oldAppServiceEntity.getPipelineType())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "自定义流水线不能更新为内置流水线");
        }

        //更新自定义流水线
        if (StringUtils.isNotBlank(appServiceUpdateReq.getPipelineId()) && PlatFormConstraint.PIPELINE_TYPE_CUSTOM.equals(appServiceUpdateReq.getPipelineType())) {
            LambdaQueryWrapper<CustomPipelineYamlEntity> customPipelineYamlEntityQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineYamlEntityQueryWrapper.eq(CustomPipelineYamlEntity::getPipelineId, appServiceUpdateReq.getPipelineId());
            List<CustomPipelineYamlEntity> customPipelineYamlEntities = customPipelineYamlMapper.selectList(customPipelineYamlEntityQueryWrapper);
            String yaml = customPipelineYamlEntities.get(0).getYaml();

            LambdaQueryWrapper<CustomPipelineRelationEntity> customPipelineRelationEntityQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineRelationEntityQueryWrapper.eq(CustomPipelineRelationEntity::getAppServiceId, oldAppServiceEntity.getId());
            List<CustomPipelineRelationEntity> relationEntities = customPipelineRelationMapper.selectList(customPipelineRelationEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(relationEntities)) {
                if (!relationEntities.get(0).getPipelineId().equals(appServiceUpdateReq.getPipelineId())) {
                    LambdaQueryWrapper<CustomPipelineEnvsEntity> customPipelineEnvsEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    customPipelineEnvsEntityLambdaQueryWrapper.eq(CustomPipelineEnvsEntity::getPipelineId, appServiceUpdateReq.getPipelineId());
                    List<CustomPipelineEnvsEntity> customPipelineEnvsEntities = customPipelineEnvsMapper.selectList(customPipelineEnvsEntityLambdaQueryWrapper);

                    saveOrUpdateRepositoryFileAndEnvs(oldAppServiceEntity.getId(), yaml, ".gitlab-ci.yml", customPipelineEnvsEntities);
                    CustomPipelineRelationEntity customPipelineRelationEntity = relationEntities.get(0);
                    customPipelineRelationEntity.setPipelineId(appServiceUpdateReq.getPipelineId());
                    customPipelineRelationMapper.updateByPrimaryKeySelective(customPipelineRelationEntity);
                }
            }
        }
        return SqlHelper.retBool(appServiceMapper.updateByPrimaryKeySelective(appServiceEntity));
    }

    @Override
    public void delete(String appServiceId) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(appServiceId);

        //内部代码仓库类型应用服务-删除gitlab仓库
        if (!PlatFormConstraint.SERVICE_TYPE_EXTERNAL.equals(appServiceEntity.getServiceType())) {
            projectService.deleteProject(Integer.parseInt(appServiceEntity.getGitlabProjectId()), PlatFormConstraint.GITLAB_ROOT_USER_ID);
        } else {
            //外部代码仓库类型应用服务-删除ci环境变量，删除webhook
            LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, appServiceId);
            List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
            if (CollectionUtils.isEmpty(appServiceConfigEntities)) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务配置不存在");
            }
            List<Variable> variableResult = projectService.getProjectVariable(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
            if (CollectionUtils.isNotEmpty(variableResult)) {
                projectService.batchDeleteVariable(Integer.parseInt(appServiceEntity.getGitlabProjectId()), variableResult.stream().map(Variable::getKey).collect(Collectors.toList()), null, appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
            }

            List<ProjectHook> hooksResult = hookService.listProjectHook(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
            if (CollectionUtils.isNotEmpty(hooksResult)) {
                hookService.deleteProjectHook(Integer.parseInt(appServiceEntity.getGitlabProjectId()), hooksResult.stream().map(ProjectHook::getId).collect(Collectors.toList()), appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()), null);
            }
        }

        //删除子服务
        LambdaQueryWrapper<SubAppServiceEntity> appServiceIdWrapper = new LambdaQueryWrapper<>();
        appServiceIdWrapper.eq(SubAppServiceEntity::getAppServiceId, appServiceId);
        List<SubAppServiceEntity> subAppServiceEntities = subAppServiceMapper.selectList(appServiceIdWrapper);
        subAppServiceService.remove(appServiceIdWrapper);
        LambdaQueryWrapper<AppServiceComponentEntity> appServiceComponentWrapper = new LambdaQueryWrapper<>();
        //删除组件信息
        appServiceComponentWrapper.eq(AppServiceComponentEntity::getAppServiceId, appServiceId);
        if (CollectionUtils.isNotEmpty(subAppServiceEntities)) {
            appServiceComponentWrapper.or();
            appServiceComponentWrapper.in(AppServiceComponentEntity::getSubAppServiceId, subAppServiceEntities.stream().map(SubAppServiceEntity::getId).collect(Collectors.toList()));
        }
        appServiceComponentService.remove(appServiceComponentWrapper);

        //删除流水线信息
        LambdaQueryWrapper<AppServicePipelineEntity> appServicePipelineEntityQueryWrapper = new LambdaQueryWrapper<>();
        appServicePipelineEntityQueryWrapper.eq(AppServicePipelineEntity::getAppServiceId, appServiceId);
        appServicePipelineService.remove(appServicePipelineEntityQueryWrapper);

        //删除自定义流水线关联关系
        LambdaQueryWrapper<CustomPipelineRelationEntity> customPipelineRelationEntityQueryWrapper = new LambdaQueryWrapper<>();
        customPipelineRelationEntityQueryWrapper.eq(CustomPipelineRelationEntity::getAppServiceId, appServiceId);
        customPipelineRelationMapper.delete(customPipelineRelationEntityQueryWrapper);

        //删除部署历史
        LambdaQueryWrapper<AppServiceDeployHistoryEntity> appServiceDeployHistoryEntityQueryWrapper = new LambdaQueryWrapper<>();
        appServiceDeployHistoryEntityQueryWrapper.eq(AppServiceDeployHistoryEntity::getAppServiceId, appServiceId);
        appServiceDeployHistoryMapper.delete(appServiceDeployHistoryEntityQueryWrapper);


        //删除分支信息
        LambdaQueryWrapper<GitlabBranchEntity> gitlabBranchEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        gitlabBranchEntityLambdaQueryWrapper.eq(GitlabBranchEntity::getAppServiceId, appServiceId);
        gitlabBranchMapper.delete(gitlabBranchEntityLambdaQueryWrapper);

        //删除应用服务
        appServiceMapper.deleteById(appServiceId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppServiceAddReq appServiceAddReq) {

        //移除了一些界面配置，初始化值
        appServiceAddReq.setPipelineType(PlatFormConstraint.PIPELINE_TYPE_CUSTOM);
        appServiceAddReq.setServiceType(PlatFormConstraint.SERVICE_TYPE_EXTERNAL);
        if(PlatFormConstraint.TRUE.equals(appServiceAddReq.getType())) {
            appServiceAddReq.setType(PlatFormConstraint.AppService_Template_Microservice);
        } else{
            appServiceAddReq.setType(PlatFormConstraint.OTHER);
        }
        AppServiceEntity appServiceEntity = ConvertUtils.convertObject(appServiceAddReq, this::VoToEntity);
        appServiceEntity.setId(UUID.randomUUID().toString().replace("-", ""));
        List<TeamResp> teamResps = teamService.list();
        appServiceEntity.setTeamId(appServiceAddReq.getTeamId());
        appServiceEntity.setTeamCode(teamResps.stream().filter(teamResp -> teamResp.getTeamId().equals(appServiceAddReq.getTeamId())).findFirst().get().getTeamCode());
        //检验应用服务编码
        {
            checkAppService(appServiceAddReq);
        }

        String yaml = null;
        if (PlatFormConstraint.PIPELINE_TYPE_CUSTOM.equals(appServiceAddReq.getPipelineType())) {
            if (StringUtils.isBlank(appServiceAddReq.getPipelineId())) {
                throw new BusinessRuntimeException("自定义流水线类型必须选择流水线");
            }
            LambdaQueryWrapper<CustomPipelineYamlEntity> yamlEntityQueryWrapper = new LambdaQueryWrapper<>();
            yamlEntityQueryWrapper.eq(CustomPipelineYamlEntity::getPipelineId, appServiceAddReq.getPipelineId());
            List<CustomPipelineYamlEntity> customPipelineYamlEntities = customPipelineYamlMapper.selectList(yamlEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(customPipelineYamlEntities)) {
                yaml = customPipelineYamlEntities.get(0).getYaml();
            }

        }


        AppServiceConfigEntity appServiceConfig = null;
        if (appServiceAddReq.getServiceType().equals(PlatFormConstraint.SERVICE_TYPE_EXTERNAL)) {
            Integer checkResult = projectService.check(appServiceAddReq.getUrl(), appServiceAddReq.getToken(), appServiceAddReq.getUsername(), appServiceAddReq.getPassword());

            LambdaQueryWrapper<CustomPipelineEnvsEntity> customPipelineEnvsEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineEnvsEntityLambdaQueryWrapper.eq(CustomPipelineEnvsEntity::getPipelineId, appServiceAddReq.getPipelineId());
            List<CustomPipelineEnvsEntity> customPipelineEnvsEntities = customPipelineEnvsMapper.selectList(customPipelineEnvsEntityLambdaQueryWrapper);



            String type = customPipelineTempService.getPiPelineTempName(null, appServiceAddReq.getPipelineId());
            handleExternalGitlab(customPipelineEnvsEntities, appServiceAddReq, appServiceEntity.getId(), appServiceAddReq.getTeamCode(), checkResult, yaml, type);
            appServiceEntity.setGitlabCodeUrl(appServiceAddReq.getUrl());
            appServiceEntity.setGitlabProjectId(checkResult.toString());
            appServiceEntity.setCreateBy(paasClient.getNickName());

            appServiceConfig = new AppServiceConfigEntity();
            appServiceConfig.setAppServiceId(appServiceEntity.getId());
            appServiceConfig.setToken(appServiceAddReq.getToken());
            appServiceConfig.setUsername(appServiceAddReq.getUsername());
            appServiceConfig.setPassword(appServiceAddReq.getPassword() == null ? null : Base64Util.setEncryptionBase64(appServiceAddReq.getPassword()));
            //外部配置
        }  else {
            throw new BusinessRuntimeException("必须指定应用服务类型");
        }


        //团队和harbor-project1对1绑定，没创建则创建harbor-project
        LambdaQueryWrapper<AppServiceEntity>  teamQueryWrapper = new LambdaQueryWrapper<>();
        teamQueryWrapper.eq(AppServiceEntity::getTeamCode, appServiceAddReq.getTeamCode());
        List<AppServiceEntity> appServiceEntities = appServiceMapper.selectList(teamQueryWrapper);
        if(CollectionUtils.isEmpty(appServiceEntities)&&harborConfig.getEnable()) {
            harborClient.createProject(appServiceAddReq.getTeamCode());
        }

        //创建平台应用服务
        {
            if (PlatFormConstraint.PIPELINE_TYPE_CUSTOM.equals(appServiceAddReq.getPipelineType())) {
                CustomPipelineRelationEntity customPipelineRelationEntity = new CustomPipelineRelationEntity();
                customPipelineRelationEntity.setId(UUID.randomUUID().toString());
                customPipelineRelationEntity.setPipelineId(appServiceAddReq.getPipelineId());
                customPipelineRelationEntity.setAppServiceId(appServiceEntity.getId());
                customPipelineRelationMapper.insert(customPipelineRelationEntity);
            }

//            appServiceEntity.setProjectSetName(projectSetEntity.getName());
            appServiceMapper.insert(appServiceEntity);

            if (CollectionUtils.isNotEmpty(appServiceAddReq.getSubAppServices())) {
                List<String> subCodes = new ArrayList<>();
                List<SubAppServiceEntity> subAppServiceEntities = new ArrayList<>();
                for (AppServiceAddReq AppServiceAddReq1 : appServiceAddReq.getSubAppServices()) {
                    if (!subCodes.contains(AppServiceAddReq1.getCode())) {
                        SubAppServiceEntity subAppServiceEntity = new SubAppServiceEntity();
                        subAppServiceEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                        subAppServiceEntity.setAppServiceId(appServiceEntity.getId());
                        subAppServiceEntity.setCode(AppServiceAddReq1.getCode());
                        subAppServiceEntities.add(subAppServiceEntity);
                        subCodes.add(AppServiceAddReq1.getCode());
                    }
                }
                subAppServiceService.saveBatch(subAppServiceEntities);
            }

            if (appServiceConfig != null) {
                appServiceConfigMapper.insert(appServiceConfig);
            }
        }
    }


    @Override
    public AppServiceUrlResp getAppServiceUrl(String appServiceId) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(appServiceId);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }
        AppServiceUrlResp appServiceUrlResp = new AppServiceUrlResp();
        Project projectR;
        if (appServiceEntity.getServiceType().equals(PlatFormConstraint.SERVICE_TYPE_INTERNAL)) {
            projectR = projectService.getProjectById(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, null, null, null);
        } else {
            LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, appServiceId);
            List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(appServiceConfigEntities)) {
                projectR = projectService.getProjectById(Integer.parseInt(appServiceEntity.getGitlabProjectId()), appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() != null ? Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()) : null);
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务无外部配置信息");
            }
        }
        appServiceUrlResp.setHttpUrl(projectR.getHttpUrlToRepo());
        appServiceUrlResp.setSshUrl(projectR.getSshUrlToRepo());
        return appServiceUrlResp;
    }

    @Override
    public String getAppServiceDesc(String appServiceId) {
        try (InputStream inputStream = AppServiceServiceImpl.class.getResourceAsStream("/md/service.md")) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "查询应用服务仓库介绍失败");
        }
    }

    @Override
    public List<AppServiceListResp> listAppServiceByTeam(String teamCode) {
        LambdaQueryWrapper<AppServiceEntity> projectIdWrapper = new LambdaQueryWrapper<>();
        projectIdWrapper.eq(AppServiceEntity::getTeamId, teamCode);
        projectIdWrapper.eq(AppServiceEntity::getStatus, "enable");
        List<AppServiceEntity> projectIdQueryResult = appServiceMapper.selectList(projectIdWrapper);
        if (CollectionUtils.isNotEmpty(projectIdQueryResult)) {
            projectIdQueryResult = projectIdQueryResult.stream().sorted(Comparator.comparing(AppServiceEntity::getCreateTime).reversed()).collect(Collectors.toList());
        }
        return ConvertUtils.convertList(projectIdQueryResult, AppServiceListResp.class);
    }

    @Override
    public List<String> listAppTemplate() {
        return templates;
    }

    @Override
    public List<String> listSubService(String id) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(id);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }

        LambdaQueryWrapper<SubAppServiceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubAppServiceEntity::getAppServiceId, id);
        List<SubAppServiceEntity> results = subAppServiceMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(results)) {
            return new ArrayList<>();
        }
        return results.stream().map(SubAppServiceEntity::getCode).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addSubService(String id, List<String> modules) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(id);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }
        String type = customPipelineTempService.getPiPelineTempName(id, null);
        if (!type.equals(PlatFormConstraint.AppService_Template_Microservice)) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "该应用服务类型无法添加子模块");
        }
        LambdaQueryWrapper<SubAppServiceEntity> subAppServiceEntityQueryWrapper = new LambdaQueryWrapper<>();
        subAppServiceEntityQueryWrapper.eq(SubAppServiceEntity::getAppServiceId, id);
        List<SubAppServiceEntity> existSubAppServices = subAppServiceMapper.selectList(subAppServiceEntityQueryWrapper);
        List<String> existCodes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(existSubAppServices)) {
            existCodes.addAll(existSubAppServices.stream().map(SubAppServiceEntity::getCode).collect(Collectors.toList()));
        }

        for (String module : modules) {
            if (!existCodes.contains(module)) {
                SubAppServiceEntity subAppServiceEntity = new SubAppServiceEntity();
                subAppServiceEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                subAppServiceEntity.setAppServiceId(appServiceEntity.getId());
                subAppServiceEntity.setCode(module);
                subAppServiceMapper.insert(subAppServiceEntity);
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, String.format("子模块编码[%s]已存在", module));
            }
        }
    }

    @Override
    @Transactional
    public void deleteSubService(String id, List<String> modules) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(id);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }
        LambdaQueryWrapper<SubAppServiceEntity> subAppServiceEntityQueryWrapper = new LambdaQueryWrapper<>();
        subAppServiceEntityQueryWrapper.eq(SubAppServiceEntity::getAppServiceId, id);
        List<SubAppServiceEntity> existSubAppServices = subAppServiceMapper.selectList(subAppServiceEntityQueryWrapper);

        if (CollectionUtils.isNotEmpty(existSubAppServices)) {
           List<SubAppServiceEntity> deletes =  existSubAppServices.stream().filter(subAppServiceEntity -> modules.contains(subAppServiceEntity.getCode())).collect(Collectors.toList());
           if(CollectionUtils.isNotEmpty(deletes)) {
               appServiceMapper.deleteBatchIds(deletes.stream().map(SubAppServiceEntity::getId).collect(Collectors.toList()));
           }
        }
    }

    @Override
    public Boolean canDeploy(String id, String sub_id) {
        return null;
    }


//    @Override
//    public Page<ProjectMemberResp> findMemberPage(ProjectMembePageReq query, Page<ProjectMemberEntity> page) {
//        Page<ProjectMemberResp> result = new Page<>();
//        Page<ProjectMemberEntity> appServiceMemberEntityPage = projectMemberMapper.findPage(query, page);
//        List<ProjectMemberEntity> appServiceMemberEntities = appServiceMemberEntityPage.getRecords();
//        List<String> idaasUserNames = appServiceMemberEntities.stream().map(ProjectMemberEntity::getIdaas_account).collect(Collectors.toList());
//        UserQuery userQuery = new UserQuery();
//        userQuery.setUsernameList(idaasUserNames);
//        userQuery.setTenantId("defaultTenantId");
//
//        R<List<com.talkweb.idaas.account.pojo.vo.UserVO>> usersR = userApi.findListByUsernameList(userQuery);
//        if (CollectionUtils.isEmpty(usersR.getData())) {
//            return result;
//        }
//        Map<String, UserEntity> userEntityMap = usersR.getData().stream().collect(Collectors.toMap(UserEntity::getUsername, u -> u));
//        List<ProjectMemberResp> appServiceMemberVOS = appServiceMemberEntities.stream().map(appServiceMemberEntity -> {
//            ProjectMemberResp appServiceMemberVO = new ProjectMemberResp();
//            UserEntity userEntity = userEntityMap.get(appServiceMemberEntity.getIdaas_account());
//            appServiceMemberVO.setIdaas_account(appServiceMemberEntity.getIdaas_account());
//            appServiceMemberVO.setEmail(userEntity.getEmail());
//            appServiceMemberVO.setPhone(userEntity.getMobile());
//            appServiceMemberVO.setRole(appServiceMemberEntity.getRole());
//            appServiceMemberVO.setReal_name(userEntity.getReal_name());
//            appServiceMemberVO.setStatus(userEntity.getStatus().equals(StatusEnum.ENABLE) ? "启用" : "禁用");
//            return appServiceMemberVO;
//        }).collect(Collectors.toList());
//        BeanUtils.copyProperties(appServiceMemberEntityPage, result);
//        result.setRecords(appServiceMemberVOS);
//        return result;
//    }


    @Override
    public AppServiceDetailResp selectById(String id) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(id);
        AppServiceDetailResp appServiceDetailResp = ConvertUtils.convertObject(appServiceEntity, AppServiceDetailResp.class);

        if (PlatFormConstraint.PIPELINE_TYPE_CUSTOM.equals(appServiceEntity.getPipelineType())) {
            LambdaQueryWrapper<CustomPipelineRelationEntity> customPipelineRelationEntityQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineRelationEntityQueryWrapper.eq(CustomPipelineRelationEntity::getAppServiceId, appServiceEntity.getId());
            List<CustomPipelineRelationEntity> relationEntities = customPipelineRelationMapper.selectList(customPipelineRelationEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(relationEntities)) {
                appServiceDetailResp.setPipelineId(relationEntities.get(0).getPipelineId());
                CustomPipelineEntity customPipeline = customPipelineMapper.selectById(relationEntities.get(0).getPipelineId());
                appServiceDetailResp.setPipelineName(customPipeline.getName());
            }
        }

        String type = customPipelineTempService.getPiPelineTempName(id, null);
        if (type != null && type.equals(PlatFormConstraint.AppService_Template_Microservice)) {
            LambdaQueryWrapper<SubAppServiceEntity> subAppServiceEntityQueryWrapper = new LambdaQueryWrapper<>();
            subAppServiceEntityQueryWrapper.eq(SubAppServiceEntity::getAppServiceId, id);
            List<SubAppServiceEntity> subAppServiceEntities = subAppServiceMapper.selectList(subAppServiceEntityQueryWrapper);
            List<SubAppServiceVO> subAppServiceAddReqS = ConvertUtils.convertList(subAppServiceEntities, SubAppServiceVO.class);


            LambdaQueryWrapper<AppServiceAutoDeployEntity> appServiceAutoDeployEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceAutoDeployEntityQueryWrapper.eq(AppServiceAutoDeployEntity::getAppServiceId, id);
            appServiceAutoDeployEntityQueryWrapper.isNotNull(AppServiceAutoDeployEntity::getSubAppServiceId);
            List<AppServiceAutoDeployEntity> appServiceAutoDeployEntities = appServiceAutoDeployMapper.selectList(appServiceAutoDeployEntityQueryWrapper);
            //该环境下是否设置了自动部署
            List<SubAppServiceDeployEnvVO> subAppServiceDeployEnvVOS = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(appServiceAutoDeployEntities)) {
                Map<String, List<AppServiceAutoDeployEntity>> autoDeploysMap = appServiceAutoDeployEntities.stream().collect(Collectors.groupingBy(AppServiceAutoDeployEntity::getRegionCode));
                for (String regionCode : autoDeploysMap.keySet()) {
                    List<AppServiceAutoDeployEntity> autoDeploys = autoDeploysMap.get(regionCode);
                    SubAppServiceDeployEnvVO subAppServiceDeployEnvVO = new SubAppServiceDeployEnvVO();
                    subAppServiceDeployEnvVO.setRegion_code(regionCode);
                    //是否设置自动部署
                    List<String> subAppIds = autoDeploys.stream().map(AppServiceAutoDeployEntity::getSubAppServiceId).collect(Collectors.toList());
                    subAppServiceAddReqS = subAppServiceAddReqS.stream().peek(subAppServiceAddReq -> {
                        if (subAppIds.contains(subAppServiceAddReq.getId())) {
                            subAppServiceAddReq.setAuto_deploy(true);
                        } else {
                            subAppServiceAddReq.setAuto_deploy(false);
                        }
                    }).collect(Collectors.toList());
                    subAppServiceDeployEnvVO.setSubAppServiceEntities(subAppServiceAddReqS);
                    subAppServiceDeployEnvVOS.add(subAppServiceDeployEnvVO);
                }
            } else {
                RainbondRegionPageResult regionResps = paasClient.listTeamRegions(appServiceEntity.getTeamCode());
                for (RainbondRegion regionResp : regionResps.getRegions()) {
                    SubAppServiceDeployEnvVO subAppServiceDeployEnvVO = new SubAppServiceDeployEnvVO();
                    subAppServiceDeployEnvVO.setSubAppServiceEntities(subAppServiceAddReqS);
                    subAppServiceDeployEnvVO.setRegion_code(regionResp.getRegionName());
                    subAppServiceDeployEnvVOS.add(subAppServiceDeployEnvVO);
                }
            }
            appServiceDetailResp.setSubAppServiceDeployEnvVOS(subAppServiceDeployEnvVOS);
        } else {
            //该环境下是否设置了自动部署
            appServiceDetailResp.setAutoDeploy(false);
            LambdaQueryWrapper<AppServiceAutoDeployEntity> appServiceAutoDeployEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceAutoDeployEntityQueryWrapper.eq(AppServiceAutoDeployEntity::getAppServiceId, id);
            appServiceAutoDeployEntityQueryWrapper.isNull(AppServiceAutoDeployEntity::getSubAppServiceId);
            List<AppServiceAutoDeployEntity> appServiceAutoDeployEntities = appServiceAutoDeployMapper.selectList(appServiceAutoDeployEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(appServiceAutoDeployEntities)) {
                appServiceDetailResp.setAutoDeploy(true);
                appServiceDetailResp.setRegionCodes(appServiceAutoDeployEntities.stream().map(AppServiceAutoDeployEntity::getRegionCode).collect(Collectors.toList()));
            }
        }
        return appServiceDetailResp;
    }


    @Override
    public Boolean check(String url, String token, String username, String password) {
        if (Strings.isNotBlank(url)) {
            //校验外部gitlab仓库地址
            if (!Pattern.matches(PlatFormConstraint.PROJECT_URL_PATTRRN, url)
            ) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "外部gitlab仓库地址不合法");
            }
        }
        Integer result = projectService.check(url, token, username, password);
        if (result == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "外部gitlab仓库地址值不合法");
        }
        return true;
    }


    @Override
    public void saveOrUpdateRepositoryFileAndEnvs(String appServiceId, String yamlContent, String file, List<CustomPipelineEnvsEntity> customPipelineEnvsEntities) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(appServiceId);
        String url = null;
        String code = null;
        String username = null;
        String password = null;

        if (appServiceEntity.getServiceType() != null && appServiceEntity.getServiceType().equals(PlatFormConstraint.SERVICE_TYPE_EXTERNAL)) {
            LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, appServiceEntity.getId());
            List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(appServiceConfigEntities)) {
                url = appServiceEntity.getGitlabCodeUrl();
                code = appServiceConfigEntities.get(0).getToken();
                username = appServiceConfigEntities.get(0).getUsername();
                password = appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword());
            }
        }

        List<Branch> branchesResult = repositoryService.listBranches(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, appServiceEntity.getGitlabCodeUrl(), code, username, password);

        Map<String, String> params = new HashMap<>();
        params.put("{{auto.build}}", appServiceEntity.getCode().replace("-", "_"));
        for(Branch branch:branchesResult) {
            FileCreationReq fileCreationReq = new FileCreationReq();
            fileCreationReq.setPath(file);
            fileCreationReq.setBranchName(branch.getName());
            fileCreationReq.setContent(FileUtil.replaceReturnString(IOUtils.toInputStream(yamlContent, StandardCharsets.UTF_8), params));
            fileCreationReq.setCommitMessage("更新" + file + "文件");
            RepositoryFile repositoryFileR = repositoryService.updateFile(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, url, code, username, password, fileCreationReq);
            if (repositoryFileR==null) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "更新" + file + "失败");
            }
        }
        if(CollectionUtils.isNotEmpty(customPipelineEnvsEntities)) {
            List<Variable> variables = new ArrayList<>();
            for (CustomPipelineEnvsEntity customPipelineEnvsEntity : customPipelineEnvsEntities) {
                if (StringUtils.isNotBlank(customPipelineEnvsEntity.getEnvValue())) {
                    Variable variable = new Variable();
                    variable.setKey(customPipelineEnvsEntity.getEnvKey());
                    variable.setValue(customPipelineEnvsEntity.getEnvValue());
                    variables.add(variable);
                }
            }
            if(CollectionUtils.isNotEmpty(variables)) {
                List<Variable> variablesR = projectService.batchCreateVariable(Integer.parseInt(appServiceEntity.getGitlabProjectId()), variables, PlatFormConstraint.GITLAB_ROOT_USER_ID, url, code, username, password);
                if (CollectionUtils.isEmpty(variablesR)) {
                    throw new BusinessRuntimeException("创建代码服务ci变量失败");
                }
            }
        }
    }


    @Override
    public void deleteRepositoryFile(String appServiceId, String file) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(appServiceId);
        String url = null;
        String code = null;
        String username = null;
        String password = null;

        if (appServiceEntity.getServiceType() != null && appServiceEntity.getServiceType().equals(PlatFormConstraint.SERVICE_TYPE_EXTERNAL)) {
            LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
            appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, appServiceEntity.getId());
            List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(appServiceConfigEntities)) {
                url = appServiceEntity.getGitlabCodeUrl();
                code = appServiceConfigEntities.get(0).getToken();
                username = appServiceConfigEntities.get(0).getUsername();
                password = appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword());
            }
        }

        FileDeleteReq fileDeleteReq = new FileDeleteReq();
        fileDeleteReq.setBranchName(PlatFormConstraint.BRANCH_MASTER);
        fileDeleteReq.setPath(file);
        fileDeleteReq.setCommitMessage("删除" + file + "文件");
        repositoryService.deleteFile(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null,  url, code, username, password, fileDeleteReq);

    }

    @Override
    public void modifyExternel(String id, AppServiceUrlModifyReq appServiceUrlModifyVO) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(id);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }
        if (!PlatFormConstraint.SERVICE_TYPE_EXTERNAL.equals(appServiceEntity.getServiceType())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "该应用服务不是外部服务类型, 无法修改仓库地址");
        }
        LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
        appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, id);
        List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
        if (CollectionUtils.isEmpty(appServiceConfigEntities)) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务配置不存在");
        }
        String type = customPipelineTempService.getPiPelineTempName(id, null);
        Integer checkResult = projectService.check(appServiceUrlModifyVO.getUrl(), appServiceUrlModifyVO.getToken(), appServiceUrlModifyVO.getUsername(), appServiceUrlModifyVO.getPassword());


        if (!appServiceEntity.getGitlabCodeUrl().equals(appServiceUrlModifyVO.getUrl())) {


            //删除旧仓库的Docker, gitlab-ci文件，ci变量，webhook
            gitlabAysnc.deleteRepositoryFile(appServiceEntity, appServiceConfigEntities.get(0));
            List<Variable> variableResult = projectService.getProjectVariable(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
            if (CollectionUtils.isNotEmpty(variableResult)) {
                projectService.batchDeleteVariable(Integer.parseInt(appServiceEntity.getGitlabProjectId()),  variableResult.stream().map(Variable::getKey).collect(Collectors.toList()), null,  appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
            }

            List<ProjectHook> hooksResult = hookService.listProjectHook(Integer.parseInt(appServiceEntity.getGitlabProjectId()), null, appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
            if (CollectionUtils.isNotEmpty(hooksResult)) {
                hookService.deleteProjectHook(Integer.parseInt(appServiceEntity.getGitlabProjectId()), hooksResult.stream().map(ProjectHook::getId).collect(Collectors.toList()), appServiceEntity.getGitlabCodeUrl(), appServiceConfigEntities.get(0).getToken(), appServiceConfigEntities.get(0).getUsername(), appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()), null);
            }

            //新仓库添加Docker, gitlab-ci文件，ci变量，webhook
            String yaml = null;
            if (PlatFormConstraint.PIPELINE_TYPE_CUSTOM.equals(appServiceEntity.getPipelineType())) {
                LambdaQueryWrapper<CustomPipelineRelationEntity> customPipelineRelationEntityQueryWrapper = new LambdaQueryWrapper<>();
                customPipelineRelationEntityQueryWrapper.eq(CustomPipelineRelationEntity::getAppServiceId, appServiceEntity.getId());
                List<CustomPipelineRelationEntity> customPipelineRelationEntities = customPipelineRelationMapper.selectList(customPipelineRelationEntityQueryWrapper);

                LambdaQueryWrapper<CustomPipelineYamlEntity> yamlEntityQueryWrapper = new LambdaQueryWrapper<>();
                yamlEntityQueryWrapper.eq(CustomPipelineYamlEntity::getPipelineId, customPipelineRelationEntities.get(0).getPipelineId());
                List<CustomPipelineYamlEntity> customPipelineYamlEntities = customPipelineYamlMapper.selectList(yamlEntityQueryWrapper);
                if (CollectionUtils.isNotEmpty(customPipelineYamlEntities)) {
                    yaml = customPipelineYamlEntities.get(0).getYaml();
                }

            }
            LambdaQueryWrapper<GitlabBranchEntity> gitlabBranchEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            gitlabBranchEntityLambdaQueryWrapper.eq(GitlabBranchEntity::getAppServiceId, id);
            gitlabBranchMapper.delete(gitlabBranchEntityLambdaQueryWrapper);

            AppServiceAddReq AppServiceAddReq = new AppServiceAddReq();
            AppServiceAddReq.setCode(appServiceEntity.getCode());
            AppServiceAddReq.setPipelineType(appServiceEntity.getPipelineType());
            AppServiceAddReq.setUrl(appServiceUrlModifyVO.getUrl());
            AppServiceAddReq.setUsername(appServiceUrlModifyVO.getUsername());
            AppServiceAddReq.setToken(appServiceUrlModifyVO.getToken());
            AppServiceAddReq.setPassword(appServiceUrlModifyVO.getPassword());
            handleExternalGitlab(null, AppServiceAddReq, appServiceEntity.getId(), appServiceEntity.getTeamCode(), checkResult, yaml, type);

            appServiceEntity.setGitlabCodeUrl(appServiceUrlModifyVO.getUrl());
            appServiceEntity.setGitlabProjectId(checkResult.toString());
            appServiceMapper.updateByPrimaryKeySelective(appServiceEntity);

        }
        AppServiceConfigEntity appServiceConfig = appServiceConfigEntities.get(0);
        appServiceConfig.setAppServiceId(id);
        appServiceConfig.setToken(appServiceUrlModifyVO.getToken());
        appServiceConfig.setUsername(appServiceUrlModifyVO.getUsername());
        appServiceConfig.setPassword(appServiceUrlModifyVO.getPassword() == null ? null : Base64Util.setEncryptionBase64(appServiceUrlModifyVO.getPassword()));
        appServiceConfigMapper.updateByPrimaryKeySelective(appServiceConfig);

    }

    @Override
    public AppServiceUrlModifyResp getExternel(String id) {

        AppServiceEntity appServiceEntity = appServiceMapper.selectById(id);
        if (appServiceEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务不存在");
        }
        if (!PlatFormConstraint.SERVICE_TYPE_EXTERNAL.equals(appServiceEntity.getServiceType())) {
           return null;
        }
        LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
        appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, id);
        List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
        if (CollectionUtils.isEmpty(appServiceConfigEntities)) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务配置不存在");
        }
        AppServiceUrlModifyResp appServiceUrlModifyResp = new AppServiceUrlModifyResp();
        appServiceUrlModifyResp.setUrl(appServiceEntity.getGitlabCodeUrl());
        appServiceUrlModifyResp.setUsername(appServiceConfigEntities.get(0).getUsername());
        appServiceUrlModifyResp.setToken(appServiceConfigEntities.get(0).getToken());
        appServiceUrlModifyResp.setPassword(Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
        return appServiceUrlModifyResp;
    }

    @Override
    public AppServiceRepositoryUrlResp getRepositoryUrl(String id) {
        AppServiceEntity appServiceEntity = appServiceMapper.selectById(id);
        if (!PlatFormConstraint.SERVICE_TYPE_EXTERNAL.equals(appServiceEntity.getServiceType())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "该应用服务不是外部服务类型, 无法查询仓库地址");
        }
        LambdaQueryWrapper<AppServiceConfigEntity> appServiceConfigEntityQueryWrapper = new LambdaQueryWrapper<>();
        appServiceConfigEntityQueryWrapper.eq(AppServiceConfigEntity::getAppServiceId, id);
        List<AppServiceConfigEntity> appServiceConfigEntities = appServiceConfigMapper.selectList(appServiceConfigEntityQueryWrapper);
        if (CollectionUtils.isEmpty(appServiceConfigEntities)) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务配置不存在");
        }
        AppServiceRepositoryUrlResp appServiceRepositoryUrlVO = new AppServiceRepositoryUrlResp();
        appServiceRepositoryUrlVO.setUrl(appServiceEntity.getGitlabCodeUrl());
        appServiceRepositoryUrlVO.setToken(appServiceConfigEntities.get(0).getToken());
        appServiceRepositoryUrlVO.setUsername(appServiceConfigEntities.get(0).getUsername());
        appServiceRepositoryUrlVO.setPassword(appServiceConfigEntities.get(0).getPassword() == null ? null : Base64Util.getDecodeBase64(appServiceConfigEntities.get(0).getPassword()));
        return appServiceRepositoryUrlVO;
    }





    private void handleExternalGitlab(List<CustomPipelineEnvsEntity> customPipelineEnvsEntities, AppServiceAddReq appServiceAddReq, String appServiceId,  String
            teamCode, Integer projectId, String yamlContent, String type) {


        //创建webhook
        createWebhook(appServiceAddReq, projectId);

        //创建ci-variables
        createCiVariables(customPipelineEnvsEntities, appServiceAddReq, teamCode, projectId);

        //同步分支
        List<Branch> branches = syncBranches(appServiceAddReq, appServiceId, projectId);

        //同步文件
        gitlabAysnc.addRepositoryFile(branches, projectId, appServiceAddReq,yamlContent, type);


    }

    private void createCiVariables(List<CustomPipelineEnvsEntity> customPipelineEnvsEntities, AppServiceAddReq appServiceAddReq,String teamCode, Integer
            projectId) {
        List<Variable> variables = new ArrayList<>();
        Variable projectSetCodeVariable = new Variable();
        projectSetCodeVariable.setKey("TEAM_CODE");
        projectSetCodeVariable.setValue(teamCode);
        variables.add(projectSetCodeVariable);


        Variable serviceCodeVariable = new Variable();
        serviceCodeVariable.setKey("SERVICE_CODE");
        serviceCodeVariable.setValue(appServiceAddReq.getCode());
        variables.add(serviceCodeVariable);

        Variable gatewayVariable = new Variable();
        gatewayVariable.setKey("GATEWAY");
        gatewayVariable.setValue(gatewayUrl.endsWith("/")?gatewayUrl.substring(0, gatewayUrl.length()-1):gatewayUrl);
        variables.add(gatewayVariable);

        //添加自定义流水线环境变量
        if(CollectionUtils.isNotEmpty(customPipelineEnvsEntities)) {
            for(CustomPipelineEnvsEntity customPipelineEnvsEntity: customPipelineEnvsEntities){
                if(StringUtils.isNotBlank(customPipelineEnvsEntity.getEnvValue())) {
                    Variable variable =  new Variable();
                    variable.setKey(customPipelineEnvsEntity.getEnvKey());
                    variable.setValue(customPipelineEnvsEntity.getEnvValue());
                    variables.add(variable);
                }
            }
        }
        if(harborConfig.getEnable()) {
            Variable harborUrlVariable = new Variable();
            harborUrlVariable.setKey("HARBOR_URL");
            harborUrlVariable.setValue(harborConfig.getUrl().split("//")[1]);
            variables.add(harborUrlVariable);

            Variable harborSchemaVariable = new Variable();
            harborSchemaVariable.setKey("HARBOR_SCHEMAL");
            harborSchemaVariable.setValue(harborConfig.getUrl().split("//")[0]);
            variables.add(harborSchemaVariable);

            Variable harborUsernameVariable = new Variable();
            harborUsernameVariable.setKey("HARBOR_USERNAME");
            harborUsernameVariable.setValue(harborConfig.getUsername());
            harborUsernameVariable.setMasked(true);
            variables.add(harborUsernameVariable);

            Variable harborPasswordVariable = new Variable();
            harborPasswordVariable.setKey("HARBOR_PASSWORD");
            harborPasswordVariable.setValue(harborConfig.getPassword());
            harborPasswordVariable.setMasked(true);
            variables.add(harborPasswordVariable);
        }
        //remove变量控制自动构建逻辑
        List<Variable> variablesR = projectService.batchCreateVariable(projectId, variables, PlatFormConstraint.GITLAB_ROOT_USER_ID,  appServiceAddReq.getUrl(), appServiceAddReq.getToken(), appServiceAddReq.getUsername(), appServiceAddReq.getPassword());
        if (CollectionUtils.isEmpty(variablesR)) {
            throw new BusinessRuntimeException("创建代码服务ci变量失败");
        }
    }


    private List<Branch> syncBranches(AppServiceAddReq appServiceAddReq, String appServiceId, Integer projectId) {
        List<Branch> branchesResult = repositoryService.listBranches(projectId, null, appServiceAddReq.getUrl(), appServiceAddReq.getToken(), appServiceAddReq.getUsername(), appServiceAddReq.getPassword());
        List<GitlabBranchEntity> gitlabBranchEntities = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(branchesResult)) {
            branchesResult.stream().forEach(branch -> {
                GitlabBranchEntity gitlabBranchEntity = new GitlabBranchEntity();
                gitlabBranchEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                gitlabBranchEntity.setUserId(appServiceAddReq.getUsername() != null ? appServiceAddReq.getUsername() : null);
                gitlabBranchEntity.setAppServiceId(appServiceId);
                gitlabBranchEntity.setBranchName(branch.getName());
                gitlabBranchEntity.setCommitSha(branch.getCommit().getId());
                gitlabBranchEntity.setOriginBranch(null);
                gitlabBranchEntity.setCommitDate(branch.getCommit().getCommittedDate());
                gitlabBranchEntity.setCommitUserName(branch.getCommit().getCommitterName());
                gitlabBranchEntity.setCommitMsg(branch.getCommit().getMessage());
                gitlabBranchEntities.add(gitlabBranchEntity);
            });
            gitlabBranchService.saveBatch(gitlabBranchEntities);
            return branchesResult;
        }

        return null;
    }


    private void createWebhook(AppServiceAddReq appServiceAddReq, Integer projectId) {
        ProjectHook projectHook = new ProjectHook();
        projectHook.setJobEvents(true);
        projectHook.setPipelineEvents(true);
        projectHook.setPushEvents(true);
        projectHook.setTagPushEvents(true);
        projectHook.setEnableSslVerification(true);
        projectHook.setProjectId(projectId);
        String uri = !gatewayUrl.endsWith("/") ? gatewayUrl + "/hook" : gatewayUrl + "hook";
        projectHook.setUrl(uri);
//        List<ProjectHook> projectHookDTOS = hookService.listProjectHook(projectId, PlatFormConstraint.GITLAB_ROOT_USER_ID, appServiceAddReq.getUrl(), appServiceAddReq.getToken(), appServiceAddReq.getUsername(), appServiceAddReq.getPassword());
        hookService.createProjectHook(projectId, projectHook,  appServiceAddReq.getUrl(), appServiceAddReq.getToken(), appServiceAddReq.getUsername(), appServiceAddReq.getPassword(), PlatFormConstraint.GITLAB_ROOT_USER_ID);
    }


    private void checkAppService(AppServiceAddReq appServiceAddReq) {

        //检验应用服务名称是否存在
        {
            LambdaQueryWrapper<AppServiceEntity> nameWrapper = new LambdaQueryWrapper<>();
            nameWrapper.eq(AppServiceEntity::getName, appServiceAddReq.getName());
            List<AppServiceEntity> nameQueryResult = appServiceMapper.selectList(nameWrapper);
            if (CollectionUtils.isNotEmpty(nameQueryResult)) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务名称已存在");
            }
        }

        //检验应用服务编码是否存在
        {
            LambdaQueryWrapper<AppServiceEntity> codeWrapper = new LambdaQueryWrapper<>();
            codeWrapper.eq(AppServiceEntity::getCode, appServiceAddReq.getCode());
            List<AppServiceEntity> codeQueryResult = appServiceMapper.selectList(codeWrapper);
            if (CollectionUtils.isNotEmpty(codeQueryResult)) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务编码已存在");
            }
        }


        //检验应用服务编码正则表达式
        if (!Pattern.matches(PlatFormConstraint.SERVICE_PATTERN, appServiceAddReq.getCode())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "应用服务编码以小写字母开头，由小写字母数字以及下划线_和中划线-组成");
        }

        if (CollectionUtils.isNotEmpty(appServiceAddReq.getSubAppServices())) {
            appServiceAddReq.getSubAppServices().stream().forEach(AppServiceAddReq1 -> {
                if (!Pattern.matches(PlatFormConstraint.SERVICE_PATTERN, AppServiceAddReq1.getCode())) {
                    throw new BusinessRuntimeException(ABizCode.FAIL, "子模块编码以小写字母开头，由小写字母数字以及下划线_和中划线-组成");
                }
            });
        }


        if (Strings.isNotBlank(appServiceAddReq.getUrl())) {
            //校验外部gitlab仓库地址
            if (!Pattern.matches(PlatFormConstraint.PROJECT_URL_PATTRRN, appServiceAddReq.getUrl())
            ) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "外部gitlab仓库地址不合法");
            }
            LambdaQueryWrapper<AppServiceEntity> codeUrlWrapper = new LambdaQueryWrapper<>();
            codeUrlWrapper.eq(AppServiceEntity::getGitlabCodeUrl, appServiceAddReq.getUrl());
            List<AppServiceEntity> codeUrlQueryResult = appServiceMapper.selectList(codeUrlWrapper);
            if (CollectionUtils.isNotEmpty(codeUrlQueryResult)) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "该外部仓库已经被应用服务绑定");
            }
        }


    }


    private AppServiceEntity VoToEntity(AppServiceAddReq AppServiceAddReq) {
        AppServiceEntity appServiceEntity = new AppServiceEntity();
        BeanUtils.copyProperties(AppServiceAddReq, appServiceEntity);
        appServiceEntity.setAutoBuild(AppServiceAddReq.getAutoBuild());
        appServiceEntity.setStatus("enable");
        return appServiceEntity;
    }
}
