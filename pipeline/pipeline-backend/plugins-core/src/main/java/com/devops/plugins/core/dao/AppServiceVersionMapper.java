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

import java.util.List;

import com.devops.plugins.core.dao.entity.AppServiceVersionEntity;
import com.devops.plugins.core.pojo.vo.req.appServiceVersion.AppServiceVersionPageReq;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 * 应用服务版本表 Mapper 接口
 * </p>
 *
 * @author huangtf
 * @Date 2022-01-06 18:25:34
 */
@Mapper
public interface AppServiceVersionMapper extends BaseMapper<AppServiceVersionEntity> {

	/**
	 * 统计
	 * @param query
	 * @return
	 */
	Long findCount(@Param(value="query") AppServiceVersionEntity query);

    /**
     * 分页
     * @param query
     * @param page
     * @return
     */
	Page<AppServiceVersionEntity> findPage(@Param(value="query") AppServiceVersionPageReq query, Page<AppServiceVersionEntity> page);

	/**
	 * 列表
	 * @param query
	 * @return
	 */
	List<AppServiceVersionEntity> findList(@Param(value="query") AppServiceVersionEntity query);

    /**
     * 根据id动态更新传入了值的字段
     * @param entity
     * @return
    */
    int updateByPrimaryKeySelective(@Param(value="entity") AppServiceVersionEntity entity);
}
