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
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.core.dao.entity.CustomPipelineEntity;
import com.devops.plugins.core.pojo.vo.req.customPipeline.CustomPipelineDetailReq;
import com.devops.plugins.core.pojo.vo.req.customPipeline.CustomPipelinePageReq;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineDetailResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelineListResp;
import com.devops.plugins.core.pojo.vo.resp.customPipeline.CustomPipelinePageResp;

import java.util.List;


/**
 * <p>
 * 自定义流水线表 Service 接口
 * </p>
 *
 * @author sheep
 * @Date 2022-08-10 14:50:59
 */
public interface ICustomPipelineService extends IService<CustomPipelineEntity> {

	/**
	 * 统计
	 * @param query
	 * @return
	 */
	Long findCount(CustomPipelineEntity query);

    /**
     * 分页
     * @param query
     * @param page
     * @return
     */
	Page<CustomPipelinePageResp> findPage(CustomPipelinePageReq query, ReqPage<CustomPipelinePageReq> page);

	/**
	 * 列表
	 * @param query
	 * @return
	 */
	List<CustomPipelineEntity> findList(CustomPipelineEntity query);


	/**
	 * 查询项目下列表
	 *
	 * @param teamCode
	 * @return
	 */
	List<CustomPipelineListResp> list(String teamCode);

    /**
     * 根据id动态更新传入了值的字段
     * @param entity
     * @return
    */
	boolean updateByPrimaryKeySelective(CustomPipelineEntity entity);

	/**
	 * 创建自定义流水线
	 *
	 * @param customPipelineDetailReq
	 * @return
	 */
	void createPipeline(CustomPipelineDetailReq customPipelineDetailReq);


	/**
	 * 更新自定义流水线
	 *
	 * @param customPipelineDetailReq
	 * @return
	 */
	void updatePipeline(CustomPipelineDetailReq customPipelineDetailReq);


	/**
	 * 删除流水线
	 *
	 *
	 * @param id
	 */
	void deletePipeline(String id);


	/**
	 * 查询流水线详情
	 *
	 * @param id
	 * @return
	 */
	CustomPipelineDetailResp getPipeline(String id);



	/**
	 * 复制流水线详情
	 *
	 * @param id
	 * @return
	 */
	CustomPipelineDetailResp copyPipeline(String id);


	/**
	 * 查询自定义流水线是否被使用
	 *
	 * @param id
	 * @return
	 */
	Boolean use(String id);


}
