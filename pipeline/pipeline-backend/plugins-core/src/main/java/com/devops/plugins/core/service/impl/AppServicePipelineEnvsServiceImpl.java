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
import com.devops.plugins.core.dao.AppServicePipelineEnvsMapper;
import com.devops.plugins.core.dao.entity.AppServicePipelineEnvsEntity;
import com.devops.plugins.core.service.IAppServicePipelineEnvsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 流水线执行环境变量表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-08-17 17:21:22
 */
@Service
@Transactional
public class AppServicePipelineEnvsServiceImpl extends ServiceImpl<AppServicePipelineEnvsMapper, AppServicePipelineEnvsEntity> implements IAppServicePipelineEnvsService {

	@Resource
	private AppServicePipelineEnvsMapper appServicePipelineEnvsMapper;

	@Override
	public Long findCount(AppServicePipelineEnvsEntity query) {
		return appServicePipelineEnvsMapper.findCount(query);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AppServicePipelineEnvsEntity> findPage(AppServicePipelineEnvsEntity query, Page<AppServicePipelineEnvsEntity> page) {
		return appServicePipelineEnvsMapper.findPage(query, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppServicePipelineEnvsEntity> findList(AppServicePipelineEnvsEntity query) {
		return appServicePipelineEnvsMapper.findList(query);
	}

	@Override
	public boolean updateByPrimaryKeySelective(AppServicePipelineEnvsEntity entity){
	    return SqlHelper.retBool(appServicePipelineEnvsMapper.updateByPrimaryKeySelective(entity));
	}
}
