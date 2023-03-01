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


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devops.plugins.core.dao.AppServiceComponentMapper;
import com.devops.plugins.core.dao.entity.AppServiceComponentEntity;
import com.devops.plugins.core.service.IAppServiceComponentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 应用服务成员表 Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-03-01 14:59:17
 */
@Service
@Transactional
public class AppServiceComponentServiceImpl extends ServiceImpl<AppServiceComponentMapper, AppServiceComponentEntity> implements IAppServiceComponentService {

	@Resource
	private AppServiceComponentMapper appServiceComponentMapper;

}
