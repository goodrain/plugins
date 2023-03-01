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

package com.devops.plugins.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.devops.plugins.core.dao.entity.CustomPipelineTempStageEntity;

import java.util.List;


/**
 * <p>
 * 自定义流水线模板阶段表 Service 接口
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:51:37
 */
public interface ICustomPipelineTempStageService extends IService<CustomPipelineTempStageEntity> {

	/**
	 * 统计
	 * @param query
	 * @return
	 */
	Long findCount(CustomPipelineTempStageEntity query);

    /**
     * 分页
     * @param query
     * @param page
     * @return
     */
	Page<CustomPipelineTempStageEntity> findPage(CustomPipelineTempStageEntity query, Page<CustomPipelineTempStageEntity> page);

	/**
	 * 列表
	 * @param query
	 * @return
	 */
	List<CustomPipelineTempStageEntity> findList(CustomPipelineTempStageEntity query);

    /**
     * 根据id动态更新传入了值的字段
     * @param entity
     * @return
    */
	boolean updateByPrimaryKeySelective(CustomPipelineTempStageEntity entity);
}
