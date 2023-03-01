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
import com.devops.plugins.core.dao.entity.CustomPipelineTempEntity;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineDetailResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineTempResp;

import java.util.List;


/**
 * <p>
 * 自定义流水线模板表 Service 接口
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:51:44
 */
public interface ICustomPipelineTempService extends IService<CustomPipelineTempEntity> {

	/**
	 * 统计
	 * @param query
	 * @return
	 */
	Long findCount(CustomPipelineTempEntity query);

    /**
     * 分页
     * @param query
     * @param page
     * @return
     */
	Page<CustomPipelineTempEntity> findPage(CustomPipelineTempEntity query, Page<CustomPipelineTempEntity> page);

	/**
	 * 列表
	 * @param query
	 * @return
	 */
	List<CustomPipelineTempEntity> findList(CustomPipelineTempEntity query);

    /**
     * 根据id动态更新传入了值的字段
     * @param entity
     * @return
    */
	boolean updateByPrimaryKeySelective(CustomPipelineTempEntity entity);


	/**
	 *
	 * 获取自定义流水线模板
	 *
	 * @return
	 */
	List<CustomPipelineTempResp> listTemps();


	/**
	 *
	 * 获取模板详情
	 *
	 */
	CustomPipelineDetailResp getTemp(String id);


	/**
	 * 查询流水线模板类型
	 *
	 * @param pipelineId
	 * @return
	 */
	String getPiPelineTempName(String appServiceId, String pipelineId);
}
