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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.devops.plugins.common.constraint.GitlabCIConstraint;
import com.devops.plugins.common.constraint.PlatFormConstraint;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.core.dao.*;
import com.devops.plugins.core.dao.entity.*;
import com.devops.plugins.core.pojo.vo.req.customPipeline.CustomPipelineDetailReq;
import com.devops.plugins.core.pojo.vo.req.customPipeline.CustomPipelinePageReq;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelinePageResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.PaasVariableResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineDetailResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineListResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelinePageResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineStageResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.gitlabci.CiJob;
import com.devops.plugins.core.service.*;
import com.devops.plugins.core.utils.ConvertUtils;
import com.devops.plugins.core.utils.YamlUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>
 * 自定义流水线表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:50:59
 */
@Service
@Transactional
public class CustomPipelineServiceImpl extends ServiceImpl<CustomPipelineMapper, CustomPipelineEntity> implements ICustomPipelineService {

    @Resource
    private CustomPipelineMapper customPipelineMapper;
    @Resource
    private ICustomPipelineEnvsService customPipelineEnvsService;
    @Resource
    private ICustomPipelineStageService customPipelineStageService;
    @Resource
    private CustomPipelineEnvsMapper customPipelineEnvsMapper;
    @Resource
    private CustomPipelineStageMapper customPipelineStageMapper;
    @Resource
    private CustomPipelineYamlMapper customPipelineYamlMapper;
    @Resource
    private CustomPipelineRelationMapper customPipelineRelationMapper;
    @Resource
    private IAppServiceService appServiceService;
    @Resource
    private AppServiceMapper appServiceMapper;
    @Resource
    private IAppServicePipelineService appServicePipelineService;
    @Resource
    private CustomPipelineTempMapper customPipelineTempMapper;

    @Override
    public Long findCount(CustomPipelineEntity query) {
        return customPipelineMapper.findCount(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomPipelinePageResp> findPage(CustomPipelinePageReq query, ReqPage<CustomPipelinePageReq> page) {
        Page<CustomPipelinePageResp> result = new Page<>();
        //添加最新流水线状态和关联应用服务的信息
        Page<CustomPipelineEntity> customPipelineEntityPage = customPipelineMapper.findPage(ConvertUtils.convertObject(query, CustomPipelineEntity.class), ConvertUtils.convertReqPage(page, new Page<>()));
        List<CustomPipelineEntity> customPipelineEntities = customPipelineEntityPage.getRecords();
        List<CustomPipelinePageResp> newCustomPipelinePageResps = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(customPipelineEntities)) {
            result = ConvertUtils.convertPage(customPipelineEntityPage, CustomPipelinePageResp.class);
            List<CustomPipelinePageResp> customPipelinePageResps = result.getRecords();
            Map<String, CustomPipelinePageResp> customPipelinePageRespMap = customPipelinePageResps.stream().collect(Collectors.toMap(CustomPipelinePageResp::getId, Function.identity()));
            LambdaQueryWrapper<CustomPipelineRelationEntity> customPipelineRelationEntityQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineRelationEntityQueryWrapper.in(CustomPipelineRelationEntity::getPipelineId, customPipelinePageResps.stream().map(CustomPipelinePageResp::getId).collect(Collectors.toList()));
            List<CustomPipelineRelationEntity> customPipelineRelationEntities = customPipelineRelationMapper.selectList(customPipelineRelationEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(customPipelineRelationEntities)) {
                Map<String, String> relationsMap = customPipelineRelationEntities.stream().collect(Collectors.toMap(CustomPipelineRelationEntity::getPipelineId, CustomPipelineRelationEntity::getAppServiceId));
                Map<String, AppServicePipelinePageResp> pipelineStageMap = new HashMap<>();
                LambdaQueryWrapper<AppServiceEntity> appServiceEntityQueryWrapper = new LambdaQueryWrapper<>();
                appServiceEntityQueryWrapper.in(AppServiceEntity::getId, relationsMap.values());
                List<AppServiceEntity> appServiceEntities = appServiceMapper.selectList(appServiceEntityQueryWrapper);
                Map<String, String> appServiceNameMap = appServiceEntities.stream().collect(Collectors.toMap(AppServiceEntity::getId, AppServiceEntity::getName));
                Map<String, AppServicePipelinePageResp> appServiceStageMap = appServicePipelineService.findAppServiceLatestJobs(new ArrayList<>(relationsMap.values()));

                relationsMap.forEach((pipelineId, appServiceId) -> {
                    relationsMap.put(pipelineId, appServiceNameMap.get(appServiceId));
                    pipelineStageMap.put(pipelineId, appServiceStageMap.containsKey(appServiceId) ? appServiceStageMap.get(appServiceId) : null);

                });
                customPipelinePageRespMap.forEach((pipelineId, customPipelinePageResp) -> {
                    customPipelinePageResp.setAppServiceName(relationsMap.getOrDefault(pipelineId, null));
                    if (pipelineStageMap.get(pipelineId) != null) {
                        customPipelinePageResp.setStages(pipelineStageMap.get(pipelineId).getStages());
                        customPipelinePageResp.setStartTime(pipelineStageMap.get(pipelineId).getStartTime());
                    }
                    newCustomPipelinePageResps.add(customPipelinePageResp);
                });
            }
        }
        result.setRecords(CollectionUtils.isNotEmpty(newCustomPipelinePageResps) ? newCustomPipelinePageResps.stream().sorted(Comparator.comparing(CustomPipelinePageResp::getCreateTime).reversed()).collect(Collectors.toList()) : ConvertUtils.convertList(customPipelineEntities, CustomPipelinePageResp.class).stream().sorted(Comparator.comparing(CustomPipelinePageResp::getCreateTime).reversed()).collect(Collectors.toList()));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomPipelineEntity> findList(CustomPipelineEntity query) {
        return customPipelineMapper.findList(query);
    }

    @Override
    public List<CustomPipelineListResp> list(String teamId) {
        LambdaQueryWrapper<CustomPipelineEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustomPipelineEntity::getTeamId, teamId);
        List<CustomPipelineEntity> customPipelineEntities = customPipelineMapper.selectList(queryWrapper);
        LambdaQueryWrapper<AppServiceEntity> appServiceEntityQueryWrapper = new LambdaQueryWrapper<>();
        appServiceEntityQueryWrapper.eq(AppServiceEntity::getTeamId, teamId);
        List<AppServiceEntity> appServiceEntities = appServiceMapper.selectList(appServiceEntityQueryWrapper);
        if (CollectionUtils.isNotEmpty(appServiceEntities)) {
            LambdaQueryWrapper<CustomPipelineRelationEntity> customPipelineRelationEntityQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineRelationEntityQueryWrapper.in(CustomPipelineRelationEntity::getAppServiceId, appServiceEntities.stream().map(AppServiceEntity::getId).collect(Collectors.toList()));
            List<CustomPipelineRelationEntity> customPipelineRelationServices = customPipelineRelationMapper.selectList(customPipelineRelationEntityQueryWrapper);
            if (CollectionUtils.isNotEmpty(customPipelineRelationServices)) {
                List<String> pipelineIds = customPipelineRelationServices.stream().map(CustomPipelineRelationEntity::getPipelineId).collect(Collectors.toList());
                customPipelineEntities = customPipelineEntities.stream().filter(customPipelineEntity -> !pipelineIds.contains(customPipelineEntity.getId())).collect(Collectors.toList());
            }
        }
        return ConvertUtils.convertList(customPipelineEntities, CustomPipelineListResp.class);
    }

    @Override
    public boolean updateByPrimaryKeySelective(CustomPipelineEntity entity) {
        return SqlHelper.retBool(customPipelineMapper.updateByPrimaryKeySelective(entity));
    }

    @Override
    @Transactional
    public void createPipeline(CustomPipelineDetailReq customPipelineDetailReq) {

        //研究流水线名称是否被使用过
        LambdaQueryWrapper<CustomPipelineEntity> customPipelineEntityQueryWrapper = new LambdaQueryWrapper<>();
        customPipelineEntityQueryWrapper.eq(CustomPipelineEntity::getName, customPipelineDetailReq.getName());
        customPipelineEntityQueryWrapper.eq(CustomPipelineEntity::getTeamId, customPipelineDetailReq.getTeamId());
        List<CustomPipelineEntity> exists = customPipelineMapper.selectList(customPipelineEntityQueryWrapper);
        if (CollectionUtils.isNotEmpty(exists)) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "流水线名称已存在");
        }

        //插入自定义流水线
        CustomPipelineEntity customPipeline = new CustomPipelineEntity();
        customPipeline.setId(UUID.randomUUID().toString().replace("-", ""));
        customPipeline.setName(customPipelineDetailReq.getName());
        customPipeline.setTeamId(customPipelineDetailReq.getTeamId());
        customPipeline.setTempId(StringUtils.isBlank(customPipelineDetailReq.getTempId())?customPipelineDetailReq.getStages().get(0).getTempId(): customPipelineDetailReq.getTempId());


        //插入自定义流水线环境变量
        List<PaasVariableResp> variableVOS = customPipelineDetailReq.getVariables();
        List<CustomPipelineEnvsEntity> customPipelineEnvsEntities = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(variableVOS)) {
            variableVOS.stream().forEach(variableVO -> {
                CustomPipelineEnvsEntity customPipelineEnvsEntity = getCustomPipelineEnvsEntity(customPipeline.getId(), variableVO);
                customPipelineEnvsEntities.add(customPipelineEnvsEntity);
            });
        }


        //插入自定义流水线阶段
        if (CollectionUtils.isEmpty(customPipelineDetailReq.getStages())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "流水线阶段不能为空");
        }
        List<CustomPipelineStageEntity> customPipelineStageEntities = new ArrayList<>();
        Integer[] order = {1};
        customPipelineDetailReq.getStages().stream().forEach(customPipelineStageVO -> {
            validate(customPipelineStageVO);
            CustomPipelineStageEntity customPipelineStageEntity = getCustomPipelineStageEntity(customPipeline.getId(), customPipelineStageVO);
            customPipelineStageEntity.setStageOrder(order[0]);
            customPipelineStageEntities.add(customPipelineStageEntity);
            order[0] = order[0] + 1;
        });
        if (CollectionUtils.isNotEmpty(customPipelineEnvsEntities)) {
            customPipelineEnvsService.saveBatch(customPipelineEnvsEntities);
        }
        if (CollectionUtils.isNotEmpty(customPipelineStageEntities)) {
            customPipelineStageService.saveBatch(customPipelineStageEntities);
        }

        //生成gitlab-ci yaml文件
        CustomPipelineYamlEntity customPipelineYamlEntity = new CustomPipelineYamlEntity();
        customPipelineYamlEntity.setId(UUID.randomUUID().toString());
        customPipelineYamlEntity.setPipelineId(customPipeline.getId());
        customPipelineYamlEntity.setYaml(YamlUtil.getGitlabCi(customPipelineDetailReq));

        customPipelineYamlMapper.insert(customPipelineYamlEntity);
        customPipelineMapper.insert(customPipeline);
    }


    @Override
    @Transactional
    public void updatePipeline(CustomPipelineDetailReq customPipelineDetailReq) {

        CustomPipelineEntity oldCustomPipelineEntity = customPipelineMapper.selectById(customPipelineDetailReq.getId());
        if (oldCustomPipelineEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "流水线不存在");
        }
        oldCustomPipelineEntity.setName(customPipelineDetailReq.getName());

        LambdaQueryWrapper<CustomPipelineEnvsEntity> envsWrapper = new LambdaQueryWrapper<>();
        envsWrapper.eq(CustomPipelineEnvsEntity::getPipelineId,  customPipelineDetailReq.getId());
        List<CustomPipelineEnvsEntity> oldEnvs = customPipelineEnvsMapper.selectList(envsWrapper);

        LambdaQueryWrapper<CustomPipelineStageEntity> stageWrapper = new LambdaQueryWrapper<>();
        stageWrapper.eq(CustomPipelineStageEntity::getPipelineId, customPipelineDetailReq.getId());
        List<CustomPipelineStageEntity> oldStages = customPipelineStageMapper.selectList(stageWrapper);

        //处理环境变量更新
        List<CustomPipelineEnvsEntity> addEnvs = new ArrayList<>();
        List<CustomPipelineEnvsEntity> updateEnvs = new ArrayList<>();
        List<CustomPipelineEnvsEntity>  envs = new ArrayList<>();
        List<String> deleteEnvs = new ArrayList<>();
        List<String> existEnvs = new ArrayList<>();
        List<PaasVariableResp> updateVariableResps = customPipelineDetailReq.getVariables();
        if(CollectionUtils.isNotEmpty(updateVariableResps)) {
            existEnvs = updateVariableResps.stream().filter(variableVO -> variableVO.getId() != null).map(PaasVariableResp::getId).collect(Collectors.toList());
            updateVariableResps.stream().forEach(variableVO -> {
                if (variableVO.getId() == null) {
                    CustomPipelineEnvsEntity customPipelineEnvsEntity = getCustomPipelineEnvsEntity(customPipelineDetailReq.getId(), variableVO);
                    addEnvs.add(customPipelineEnvsEntity);
                    envs.add(customPipelineEnvsEntity);
                } else {
                    CustomPipelineEnvsEntity customPipelineEnvsEntity = getCustomPipelineEnvsEntity(customPipelineDetailReq.getId(), variableVO);
                    updateEnvs.add(customPipelineEnvsEntity);
                    envs.add(customPipelineEnvsEntity);
                }
            });
        }
        for(CustomPipelineEnvsEntity customPipelineEnvsEntity: oldEnvs) {
            if (!existEnvs.contains(customPipelineEnvsEntity.getId())) {
                deleteEnvs.add(customPipelineEnvsEntity.getId());
            }
        }

        //处理阶段更新
        List<CustomPipelineStageEntity> addStages = new ArrayList<>();
        List<CustomPipelineStageEntity> updateStages = new ArrayList<>();
        List<String> deleteStages = new ArrayList<>();
        Integer[] order = {1};
        customPipelineDetailReq.getStages().stream().forEach(customPipelineStageVO -> {
            validate(customPipelineStageVO);
            if (customPipelineStageVO.getId() == null) {
                CustomPipelineStageEntity customPipelineStageEntity = getCustomPipelineStageEntity(customPipelineDetailReq.getId(), customPipelineStageVO);
                customPipelineStageEntity.setStageOrder(order[0]);
                addStages.add(customPipelineStageEntity);
            } else {
                CustomPipelineStageEntity customPipelineStageEntity = getCustomPipelineStageEntity(customPipelineDetailReq.getId(), customPipelineStageVO);
                customPipelineStageEntity.setStageOrder(order[0]);
                updateStages.add(customPipelineStageEntity);
            }
            order[0] = order[0] + 1;
        });
        List<String> existStages = customPipelineDetailReq.getStages().stream().filter(customPipelineStageVO -> customPipelineStageVO.getId() != null).map(CustomPipelineStageResp::getId).collect(Collectors.toList());
        oldStages.stream().forEach(customPipelineStageEntity -> {
            if (!existStages.contains(customPipelineStageEntity.getId())) {
                deleteStages.add(customPipelineStageEntity.getId());
            }
        });


        //数据库操作
        LambdaQueryWrapper<CustomPipelineYamlEntity> customPipelineYamlEntityQueryWrapper = new LambdaQueryWrapper<>();
        customPipelineYamlEntityQueryWrapper.eq(CustomPipelineYamlEntity::getPipelineId, customPipelineDetailReq.getId());
        List<CustomPipelineYamlEntity> customPipelineYamlEntities = customPipelineYamlMapper.selectList(customPipelineYamlEntityQueryWrapper);
        String yaml = YamlUtil.getGitlabCi(customPipelineDetailReq);
        if (CollectionUtils.isNotEmpty(customPipelineYamlEntities)) {
            CustomPipelineYamlEntity customPipelineYamlEntity = customPipelineYamlEntities.get(0);
            customPipelineYamlEntity.setYaml(yaml);
            customPipelineYamlMapper.updateByPrimaryKeySelective(customPipelineYamlEntity);
        }

        if (CollectionUtils.isNotEmpty(addEnvs)) {
            customPipelineEnvsService.saveBatch(addEnvs);
        }
        if (CollectionUtils.isNotEmpty(updateEnvs)) {
            customPipelineEnvsService.saveOrUpdateBatch(updateEnvs);
        }
        if (CollectionUtils.isNotEmpty(deleteEnvs)) {
            customPipelineEnvsMapper.deleteBatchIds(deleteEnvs);
        }
        if (CollectionUtils.isNotEmpty(addStages)) {
            customPipelineStageService.saveBatch(addStages);
        }
        if (CollectionUtils.isNotEmpty(updateStages)) {
            customPipelineStageService.saveOrUpdateBatch(updateStages);
        }
        if (CollectionUtils.isNotEmpty(deleteStages)) {
            customPipelineStageMapper.deleteBatchIds(deleteStages);
        }

        customPipelineMapper.updateByPrimaryKeySelective(oldCustomPipelineEntity);
        // .gitlab-ci文件更新操作
        LambdaQueryWrapper<CustomPipelineRelationEntity> relations = new LambdaQueryWrapper<>();
        relations.eq(CustomPipelineRelationEntity::getPipelineId, customPipelineDetailReq.getId());
        List<CustomPipelineRelationEntity> relationEntities = customPipelineRelationMapper.selectList(relations);
        if (CollectionUtils.isNotEmpty(relationEntities)) {
            //更新文件和变量
            appServiceService.saveOrUpdateRepositoryFileAndEnvs( relationEntities.get(0).getAppServiceId(), yaml, ".gitlab-ci.yml", envs);

        }
    }

    @Override
    @Transactional
    public void deletePipeline(String id) {
        customPipelineMapper.deleteById(id);
        LambdaQueryWrapper<CustomPipelineEnvsEntity> envDelete = new LambdaQueryWrapper<>();
        envDelete.eq(CustomPipelineEnvsEntity::getPipelineId,  id);
        customPipelineEnvsMapper.delete(envDelete);
        LambdaQueryWrapper<CustomPipelineStageEntity> stageDelete = new LambdaQueryWrapper<>();
        stageDelete.eq(CustomPipelineStageEntity::getPipelineId, id);
        customPipelineStageMapper.delete(stageDelete);
        LambdaQueryWrapper<CustomPipelineYamlEntity> yamlDelete = new LambdaQueryWrapper<>();
        yamlDelete.eq(CustomPipelineYamlEntity::getPipelineId, id);
        customPipelineYamlMapper.delete(yamlDelete);
        LambdaQueryWrapper<CustomPipelineRelationEntity> relationDelete = new LambdaQueryWrapper<>();
        relationDelete.eq(CustomPipelineRelationEntity::getPipelineId, id);
        List<CustomPipelineRelationEntity> relationEntities = customPipelineRelationMapper.selectList(relationDelete);
        customPipelineRelationMapper.delete(relationDelete);
        // .gitlab-ci文件删除操作
        if (CollectionUtils.isNotEmpty(relationEntities)) {
            appServiceService.deleteRepositoryFile(relationEntities.get(0).getAppServiceId(), ".gitlab-ci.yml");
        }
    }





    @Override
    public CustomPipelineDetailResp getPipeline(String id) {
        CustomPipelineDetailResp result = new CustomPipelineDetailResp();
        CustomPipelineEntity customPipeline = customPipelineMapper.selectById(id);
        if (customPipeline == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "流水线不存在");
        }
        result.setId(id);
        result.setName(customPipeline.getName());

        LambdaQueryWrapper<CustomPipelineEnvsEntity> envsEntityQueryWrapper = new LambdaQueryWrapper<>();
        envsEntityQueryWrapper.eq(CustomPipelineEnvsEntity::getPipelineId, id);
        List<CustomPipelineEnvsEntity> envs = customPipelineEnvsMapper.selectList(envsEntityQueryWrapper);
        if (CollectionUtils.isNotEmpty(envs)) {
            List<PaasVariableResp> variableVOS = new ArrayList<>();
            envs.stream().forEach(customPipelineEnvsEntity -> {
                PaasVariableResp variableVO = getVariableVO(customPipelineEnvsEntity);
                variableVOS.add(variableVO);
            });
            result.setVariables(variableVOS);
        }

        LambdaQueryWrapper<CustomPipelineStageEntity> stageEntityQueryWrapper = new  LambdaQueryWrapper<>();
        stageEntityQueryWrapper.eq(CustomPipelineStageEntity::getPipelineId,  id);
        List<CustomPipelineStageEntity> stages = customPipelineStageMapper.selectList(stageEntityQueryWrapper);
        List<String> cache = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(stages)) {
            List<CustomPipelineStageResp> customPipelineStageResps = new ArrayList<>();
            stages.stream().sorted(Comparator.comparing(CustomPipelineStageEntity::getStageOrder)).forEach(customPipelineStageEntity -> {
                CustomPipelineStageResp customPipelineStageResp = getCustomPipelineStageVO(customPipelineStageEntity);
                customPipelineStageResp.setTempId(customPipeline.getTempId());
                if (customPipelineStageResp.getCache() != null) {
                    cache.addAll(customPipelineStageResp.getCache().getPaths());
                }
                customPipelineStageResps.add(customPipelineStageResp);
            });
            result.setStages(customPipelineStageResps);
            result.setCache(cache);
        }
        return result;
    }

    @Override
    public CustomPipelineDetailResp copyPipeline(String id) {
        CustomPipelineDetailResp customPipelineDetailResp = getPipeline(id);
        customPipelineDetailResp.setName(null);
        customPipelineDetailResp.setId(null);
        customPipelineDetailResp.setStages(customPipelineDetailResp.getStages().stream().peek(customPipelineStageVO -> customPipelineStageVO.setId(null)).collect(Collectors.toList()));
        customPipelineDetailResp.setVariables(customPipelineDetailResp.getVariables().stream().peek(variableVO -> variableVO.setId(null)).collect(Collectors.toList()));
        return customPipelineDetailResp;
    }

    @Override
    public Boolean use(String id) {
        LambdaQueryWrapper<CustomPipelineRelationEntity> relationEntityQueryWrapper = new  LambdaQueryWrapper<>();
        relationEntityQueryWrapper.eq(CustomPipelineRelationEntity::getPipelineId, id);
        List<CustomPipelineRelationEntity> customPipelineRelationEntities = customPipelineRelationMapper.selectList(relationEntityQueryWrapper);
        if (CollectionUtils.isNotEmpty(customPipelineRelationEntities)) {
            return true;
        } else {
            return false;
        }
    }



    private void validate(CustomPipelineStageResp customPipelineStageResp) {

        //校验name
        if (StringUtils.isBlank(customPipelineStageResp.getName())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "阶段的名称为空，不合法");
        }

        //校验code
        if (StringUtils.isBlank(customPipelineStageResp.getCode())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, String.format("阶段:%s的编码为空，不合法", customPipelineStageResp.getName()));
        }

        //校验image
        if (StringUtils.isBlank(customPipelineStageResp.getImage())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, String.format("阶段:%s的镜像为空，不合法", customPipelineStageResp.getName()));
        }

        //校验code, 不能是gitlab-ci中的关键字
        if (PlatFormConstraint.STAGE_VALIDATE.contains(customPipelineStageResp.getCode())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, String.format("阶段:%s的编码%s是关键字，不合法", customPipelineStageResp.getName(), customPipelineStageResp.getCode()));
        }

        if (CollectionUtils.isEmpty(customPipelineStageResp.getScript())) {
            throw new BusinessRuntimeException(ABizCode.FAIL, String.format("阶段:%s的执行脚本为空，不合法", customPipelineStageResp.getName()));
        }
        //todo 其它校验
    }


    public static PaasVariableResp getVariableVO(CustomPipelineEnvsEntity customPipelineEnvsEntity) {
        PaasVariableResp variableVO = new PaasVariableResp();
        variableVO.setId(customPipelineEnvsEntity.getId());
        variableVO.setKey(customPipelineEnvsEntity.getEnvKey());
        variableVO.setValue(customPipelineEnvsEntity.getEnvValue());
        variableVO.setDesc(customPipelineEnvsEntity.getDescription());
        return variableVO;
    }

        public static CustomPipelineStageResp getCustomPipelineStageVO(CustomPipelineStageEntity customPipelineStageEntity) {
        CustomPipelineStageResp customPipelineStageResp = new CustomPipelineStageResp();
        customPipelineStageResp.setId(customPipelineStageEntity.getId());
        customPipelineStageResp.setName(customPipelineStageEntity.getName());
        customPipelineStageResp.setCode(customPipelineStageEntity.getCode());
        CiJob ciJob = JSONObject.parseObject(customPipelineStageEntity.getStage(), CiJob.class);
        customPipelineStageResp.setImage(ciJob.getImage());
        customPipelineStageResp.setTags(ciJob.getTags());
        customPipelineStageResp.setArtifacts(ciJob.getArtifacts());
        customPipelineStageResp.setExcept(ciJob.getExcept());
        customPipelineStageResp.setCache(ciJob.getCache());
        customPipelineStageResp.setScript(ciJob.getScript());
        customPipelineStageResp.setOnly(ciJob.getOnly());
        return customPipelineStageResp;
    }

//
    public static CustomPipelineStageResp getCustomPipelineStageVO(CustomPipelineTempStageEntity customPipelineTempStageEntity) {
        CustomPipelineStageResp customPipelineStageResp = new CustomPipelineStageResp();
        customPipelineStageResp.setName(customPipelineTempStageEntity.getName());
        customPipelineStageResp.setCode(customPipelineTempStageEntity.getCode());
        CiJob ciJob = JSONObject.parseObject(customPipelineTempStageEntity.getStage(), CiJob.class);
        customPipelineStageResp.setImage(ciJob.getImage());
        customPipelineStageResp.setTags(ciJob.getTags());
        customPipelineStageResp.setArtifacts(ciJob.getArtifacts());
        customPipelineStageResp.setExcept(ciJob.getExcept());
        customPipelineStageResp.setCache(ciJob.getCache());
        customPipelineStageResp.setScript(ciJob.getScript());
        customPipelineStageResp.setOnly(ciJob.getOnly());
        return customPipelineStageResp;
    }



    public static CustomPipelineEnvsEntity getCustomPipelineEnvsEntity(String pipelineId, PaasVariableResp variableVO) {
        CustomPipelineEnvsEntity customPipelineEnvsEntity = new CustomPipelineEnvsEntity();
        customPipelineEnvsEntity.setId(variableVO.getId() == null ? UUID.randomUUID().toString().replace("-", "") : variableVO.getId());
        customPipelineEnvsEntity.setPipelineId(pipelineId);
        customPipelineEnvsEntity.setEnvKey(variableVO.getKey());
        customPipelineEnvsEntity.setEnvValue(variableVO.getValue());
        customPipelineEnvsEntity.setDescription(variableVO.getDesc());
        return customPipelineEnvsEntity;
    }


        public static CustomPipelineStageEntity getCustomPipelineStageEntity(String pipelineId, CustomPipelineStageResp customPipelineStageResp) {
        CustomPipelineStageEntity customPipelineStageEntity = new CustomPipelineStageEntity();
        customPipelineStageEntity.setId(customPipelineStageResp.getId() == null? UUID.randomUUID().toString().replace("-", ""): customPipelineStageResp.getId());
        customPipelineStageEntity.setPipelineId(pipelineId);
        customPipelineStageEntity.setName(customPipelineStageResp.getName());
        customPipelineStageEntity.setCode(customPipelineStageResp.getCode());
        CiJob ciJob = new CiJob();
        ciJob.setImage(customPipelineStageResp.getImage());
        ciJob.setTags(customPipelineStageResp.getTags());
        ciJob.setScript(customPipelineStageResp.getScript());
        ciJob.setArtifacts(customPipelineStageResp.getArtifacts());
        ciJob.setCache(customPipelineStageResp.getCache());
        ciJob.setExcept(customPipelineStageResp.getExcept());
        ciJob.setOnly(customPipelineStageResp.getOnly());
        ciJob.setStage(customPipelineStageResp.getCode());
        customPipelineStageEntity.setStage(JSONObject.toJSONString(ciJob));
        return customPipelineStageEntity;
    }
}
