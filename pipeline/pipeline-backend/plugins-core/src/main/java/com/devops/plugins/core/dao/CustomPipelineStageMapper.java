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

package com.devops.plugins.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devops.plugins.core.dao.entity.CustomPipelineStageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 自定义流水线阶段表 Mapper 接口
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:52:23
 */
@Mapper
public interface CustomPipelineStageMapper extends BaseMapper<CustomPipelineStageEntity> {

	/**
	 * 统计
	 * @param query
	 * @return
	 */
	Long findCount(@Param(value="query") CustomPipelineStageEntity query);

    /**
     * 分页
     * @param query
     * @param page
     * @return
     */
	Page<CustomPipelineStageEntity> findPage(@Param(value="query") CustomPipelineStageEntity query, Page<CustomPipelineStageEntity> page);

	/**
	 * 列表
	 * @param query
	 * @return
	 */
	List<CustomPipelineStageEntity> findList(@Param(value="query") CustomPipelineStageEntity query);

    /**
     * 根据id动态更新传入了值的字段
     * @param entity
     * @return
    */
    int updateByPrimaryKeySelective(@Param(value="entity") CustomPipelineStageEntity entity);
}
