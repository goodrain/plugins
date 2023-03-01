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
import com.devops.plugins.core.dao.CustomPipelineStageMapper;
import com.devops.plugins.core.dao.entity.CustomPipelineStageEntity;
import com.devops.plugins.core.service.ICustomPipelineStageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 自定义流水线阶段表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:52:23
 */
@Service
@Transactional
public class CustomPipelineStageServiceImpl extends ServiceImpl<CustomPipelineStageMapper, CustomPipelineStageEntity> implements ICustomPipelineStageService {

	@Resource
	private CustomPipelineStageMapper customPipelineStageMapper;

	@Override
	public Long findCount(CustomPipelineStageEntity query) {
		return customPipelineStageMapper.findCount(query);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomPipelineStageEntity> findPage(CustomPipelineStageEntity query, Page<CustomPipelineStageEntity> page) {
		return customPipelineStageMapper.findPage(query, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomPipelineStageEntity> findList(CustomPipelineStageEntity query) {
		return customPipelineStageMapper.findList(query);
	}

	@Override
	public boolean updateByPrimaryKeySelective(CustomPipelineStageEntity entity){
	    return SqlHelper.retBool(customPipelineStageMapper.updateByPrimaryKeySelective(entity));
	}
}
