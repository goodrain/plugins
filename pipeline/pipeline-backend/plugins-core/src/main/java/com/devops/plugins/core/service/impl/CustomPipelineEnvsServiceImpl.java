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
import com.devops.plugins.core.dao.CustomPipelineEnvsMapper;
import com.devops.plugins.core.dao.entity.CustomPipelineEnvsEntity;
import com.devops.plugins.core.service.ICustomPipelineEnvsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 自定义流水线环境表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:51:09
 */
@Service
@Transactional
public class CustomPipelineEnvsServiceImpl extends ServiceImpl<CustomPipelineEnvsMapper, CustomPipelineEnvsEntity> implements ICustomPipelineEnvsService {

	@Resource
	private CustomPipelineEnvsMapper customPipelineEnvsMapper;

	@Override
	public Long findCount(CustomPipelineEnvsEntity query) {
		return customPipelineEnvsMapper.findCount(query);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CustomPipelineEnvsEntity> findPage(CustomPipelineEnvsEntity query, Page<CustomPipelineEnvsEntity> page) {
		return customPipelineEnvsMapper.findPage(query, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomPipelineEnvsEntity> findList(CustomPipelineEnvsEntity query) {
		return customPipelineEnvsMapper.findList(query);
	}

	@Override
	public boolean updateByPrimaryKeySelective(CustomPipelineEnvsEntity entity){
	    return SqlHelper.retBool(customPipelineEnvsMapper.updateByPrimaryKeySelective(entity));
	}
}
