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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.devops.plugins.core.dao.CustomPipelineRelationMapper;
import com.devops.plugins.core.dao.entity.CustomPipelineRelationEntity;
import com.devops.plugins.core.service.ICustomPipelineRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 自定义流水线关系表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:51:16
 */
@Service
@Transactional
public class CustomPipelineRelationServiceImpl extends ServiceImpl<CustomPipelineRelationMapper, CustomPipelineRelationEntity> implements ICustomPipelineRelationService {

	@Resource
	private CustomPipelineRelationMapper customPipelineRelationMapper;

	@Override
	public Long findCount(CustomPipelineRelationEntity query) {
		return customPipelineRelationMapper.findCount(query);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomPipelineRelationEntity> findPage(CustomPipelineRelationEntity query, Page<CustomPipelineRelationEntity> page) {
		return customPipelineRelationMapper.findPage(query, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomPipelineRelationEntity> findList(CustomPipelineRelationEntity query) {
		return customPipelineRelationMapper.findList(query);
	}

	@Override
	public boolean updateByPrimaryKeySelective(CustomPipelineRelationEntity entity){
	    return SqlHelper.retBool(customPipelineRelationMapper.updateByPrimaryKeySelective(entity));
	}
}
