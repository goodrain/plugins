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
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.core.config.HarborConfig;
import com.devops.plugins.core.dao.CustomPipelineMapper;
import com.devops.plugins.core.dao.CustomPipelineRelationMapper;
import com.devops.plugins.core.dao.CustomPipelineTempMapper;
import com.devops.plugins.core.dao.CustomPipelineTempStageMapper;
import com.devops.plugins.core.dao.entity.CustomPipelineEntity;
import com.devops.plugins.core.dao.entity.CustomPipelineRelationEntity;
import com.devops.plugins.core.dao.entity.CustomPipelineTempEntity;
import com.devops.plugins.core.dao.entity.CustomPipelineTempStageEntity;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.PaasVariableResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineDetailResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineStageResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineTempResp;
import com.devops.plugins.core.service.ICustomPipelineTempService;
import com.devops.plugins.core.utils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 自定义流水线模板表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:51:44
 */
@Service
@Transactional
public class CustomPipelineTempServiceImpl extends ServiceImpl<CustomPipelineTempMapper, CustomPipelineTempEntity> implements ICustomPipelineTempService {

    @Resource
    private CustomPipelineTempMapper customPipelineTempMapper;
    @Resource
    private CustomPipelineTempStageMapper customPipelineTempStageMapper;
    @Resource
    private HarborConfig harborConfig;
    @Resource
    private CustomPipelineRelationMapper customPipelineRelationMapper;
    @Resource
    private CustomPipelineMapper customPipelineMapper;

    @Override
    public Long findCount(CustomPipelineTempEntity query) {
        return customPipelineTempMapper.findCount(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomPipelineTempEntity> findPage(CustomPipelineTempEntity query, Page<CustomPipelineTempEntity> page) {
        return customPipelineTempMapper.findPage(query, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomPipelineTempEntity> findList(CustomPipelineTempEntity query) {
        return customPipelineTempMapper.findList(query);
    }

    @Override
    public boolean updateByPrimaryKeySelective(CustomPipelineTempEntity entity) {
        return SqlHelper.retBool(customPipelineTempMapper.updateByPrimaryKeySelective(entity));
    }

    @Override
    public List<CustomPipelineTempResp> listTemps() {
        List<CustomPipelineTempEntity> customPipelineTempEntities = customPipelineTempMapper.selectList(new LambdaQueryWrapper<>());
        List<CustomPipelineTempResp> customPipelineTempResps = ConvertUtils.convertList(customPipelineTempEntities, CustomPipelineTempResp.class);
        List<CustomPipelineTempStageEntity> customPipelineTempStageEntities = customPipelineTempStageMapper.selectList(new LambdaQueryWrapper<>());
        Map<String, List<CustomPipelineTempStageEntity>> stageMaps = customPipelineTempStageEntities.stream().collect(Collectors.groupingBy(CustomPipelineTempStageEntity::getTemplateId));
        customPipelineTempResps.stream().forEach(customPipelineTempResp -> {
            List<CustomPipelineTempStageEntity> stages = stageMaps.get(customPipelineTempResp.getId());
            stages = stages.stream().sorted(Comparator.comparing(CustomPipelineTempStageEntity::getStageOrder)).collect(Collectors.toList());
            customPipelineTempResp.setStages(stages.stream().map(CustomPipelineTempStageEntity::getName).collect(Collectors.toList()));
        });
        return customPipelineTempResps;
    }

    @Override
    public CustomPipelineDetailResp getTemp(String id) {
        CustomPipelineTempEntity customPipelineTempEntity = customPipelineTempMapper.selectById(id);
        if (customPipelineTempEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "模板不存在");
        }
        LambdaQueryWrapper<CustomPipelineTempStageEntity> tempStageEntityQueryWrapper = new LambdaQueryWrapper<>();
        tempStageEntityQueryWrapper.eq(CustomPipelineTempStageEntity::getTemplateId, id);
        List<CustomPipelineTempStageEntity> stages = customPipelineTempStageMapper.selectList(tempStageEntityQueryWrapper);
        CustomPipelineDetailResp customPipelineDetailResp = new CustomPipelineDetailResp();
        //设置阶段
        if (CollectionUtils.isNotEmpty(stages)) {
            List<CustomPipelineStageResp> customPipelineStageResps = new ArrayList<>();
            stages.stream().sorted(Comparator.comparing(CustomPipelineTempStageEntity::getStageOrder)).forEach(customPipelineTempStageEntity -> {
                CustomPipelineStageResp customPipelineStageResp = CustomPipelineServiceImpl.getCustomPipelineStageVO(customPipelineTempStageEntity);
                customPipelineStageResps.add(customPipelineStageResp);
            });
            customPipelineDetailResp.setStages(customPipelineStageResps);
        }
        if (!harborConfig.getEnable()) {
            List<PaasVariableResp> variables = new ArrayList<>();
            PaasVariableResp harborUrlVariable = new PaasVariableResp();
            harborUrlVariable.setKey("REPOSITORY_URL");
            harborUrlVariable.setValue("");
            variables.add(harborUrlVariable);

            PaasVariableResp orgVariable = new PaasVariableResp();
			orgVariable.setKey("ORG");
			orgVariable.setValue("");
            variables.add(orgVariable);

            PaasVariableResp repositoryUsernameVariable = new PaasVariableResp();
			repositoryUsernameVariable.setKey("REPOSITORY_USERNAME");
			repositoryUsernameVariable.setValue("");
            variables.add(repositoryUsernameVariable);
            customPipelineDetailResp.setVariables(variables);


			PaasVariableResp repositoryPasswordVariable = new PaasVariableResp();
			repositoryPasswordVariable.setKey("REPOSITORY_PASSWORD");
			repositoryPasswordVariable.setValue("");
			variables.add(repositoryPasswordVariable);
			customPipelineDetailResp.setVariables(variables);
        }
		return customPipelineDetailResp;
    }

    @Override
    public String getPiPelineTempName(String  appServiceId, String pipelineId) {
        if(StringUtils.isNotBlank(appServiceId)) {
            LambdaQueryWrapper<CustomPipelineRelationEntity> customPipelineRelationEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            customPipelineRelationEntityLambdaQueryWrapper.eq(CustomPipelineRelationEntity::getAppServiceId, appServiceId);
            List<CustomPipelineRelationEntity>  customPipelineRelationEntities = customPipelineRelationMapper.selectList(customPipelineRelationEntityLambdaQueryWrapper);
            pipelineId = customPipelineRelationEntities.get(0).getPipelineId();
        }

        CustomPipelineEntity customPipelineEntity = customPipelineMapper.selectById(pipelineId);
        if(customPipelineEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "流水线不存在");
        }
        CustomPipelineTempEntity customPipelineTempEntity = customPipelineTempMapper.selectById(customPipelineEntity.getTempId());
        if(customPipelineTempEntity == null) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "流水线模板不存在");
        }
        return customPipelineTempEntity.getName();
    }
}
