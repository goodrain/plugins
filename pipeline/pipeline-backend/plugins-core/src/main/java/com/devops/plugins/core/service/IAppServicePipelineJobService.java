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
import com.devops.plugins.core.dao.entity.AppServicePipelineJobEntity;
import com.devops.plugins.core.pojo.gitlab.JobWebHook;

import java.util.List;


/**
 * <p>
 * 应用服务的CI流水线任务表 Service 接口
 * </p>
 *
 * @author sheep
 * @Date 2022-01-11 14:39:23
 */
public interface IAppServicePipelineJobService extends IService<AppServicePipelineJobEntity> {

	/**
	 * 统计
	 * @param query
	 * @return
	 */
	Long findCount(AppServicePipelineJobEntity query);

    /**
     * 分页
     * @param query
     * @param page
     * @return
     */
	Page<AppServicePipelineJobEntity> findPage(AppServicePipelineJobEntity query, Page<AppServicePipelineJobEntity> page);

	/**
	 * 列表
	 * @param query
	 * @return
	 */
	List<AppServicePipelineJobEntity> findList(AppServicePipelineJobEntity query);

    /**
     * 根据id动态更新传入了值的字段
     * @param entity
     * @return
    */
	boolean updateByPrimaryKeySelective(AppServicePipelineJobEntity entity);


	/**
	 * webhook同步pipeline-job的状态
	 *
	 * @param jobWebHook
	 */
	void webhookPipelineJobSync(JobWebHook jobWebHook);
}
