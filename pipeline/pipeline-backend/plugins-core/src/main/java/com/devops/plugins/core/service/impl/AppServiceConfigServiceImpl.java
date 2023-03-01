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
import com.devops.plugins.core.dao.AppServiceConfigMapper;
import com.devops.plugins.core.dao.entity.AppServiceConfigEntity;
import com.devops.plugins.core.service.IAppServiceConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-04-08 17:03:20
 */
@Service
@Transactional
public class AppServiceConfigServiceImpl extends ServiceImpl<AppServiceConfigMapper, AppServiceConfigEntity> implements IAppServiceConfigService {

	@Resource
	private AppServiceConfigMapper appServiceConfigMapper;

	@Override
	public Long findCount(AppServiceConfigEntity query) {
		return appServiceConfigMapper.findCount(query);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AppServiceConfigEntity> findPage(AppServiceConfigEntity query, Page<AppServiceConfigEntity> page) {
		return appServiceConfigMapper.findPage(query, page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppServiceConfigEntity> findList(AppServiceConfigEntity query) {
		return appServiceConfigMapper.findList(query);
	}

	@Override
	public boolean updateByPrimaryKeySelective(AppServiceConfigEntity entity){
	    return SqlHelper.retBool(appServiceConfigMapper.updateByPrimaryKeySelective(entity));
	}
}
