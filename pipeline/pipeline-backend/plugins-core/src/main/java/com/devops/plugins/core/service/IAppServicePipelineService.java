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
import com.devops.plugins.core.dao.entity.AppServicePipelineEntity;
import com.devops.plugins.core.pojo.gitlab.PipelineWebHook;
import com.devops.plugins.core.pojo.vo.req.appServicePipeline.AppServicePipelinePageReq;
import com.devops.plugins.core.pojo.vo.req.appServicePipeline.CiReq;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelineEnvsResp;
import com.devops.plugins.core.pojo.vo.resp.appServicePipeline.AppServicePipelinePageResp;


import java.util.List;
import java.util.Map;


/**
 * <p>
 * 应用服务的ci流水线表 Service 接口
 * </p>
 *
 * @author sheep
 * @Date 2022-01-11 14:36:10
 */
public interface IAppServicePipelineService extends IService<AppServicePipelineEntity> {

	/**
	 * 统计
	 * @param query
	 * @return
	 */
	Long findCount(AppServicePipelineEntity query);

    /**
     * 分页
     * @param
     * @param page
     * @return
     */
	Page<AppServicePipelinePageResp> findPage(AppServicePipelinePageReq appServicePipelinePageReq, ReqPage<AppServicePipelinePageReq> page);

	/**
	 * 列表
	 * @param query
	 * @return
	 */
	List<AppServicePipelineEntity> findList(AppServicePipelineEntity query);

    /**
     * 根据id动态更新传入了值的字段
     * @param entity
     * @return
    */
	boolean updateByPrimaryKeySelective(AppServicePipelineEntity entity);


	/**
	 * webhook创建pipeline
	 *
	 * @param pipelineWebHook
	 */
	void webhookPipelineSync(PipelineWebHook pipelineWebHook);

	/**
	 * 获取流水线任务id
	 *
	 * @param jobId
	 */
	String getJobLog(String pipelineId, String jobId);


	/**
	 * 执行流水线任务
	 */

	void runCi(String appServiceId, String branch, String module);


	/**
	 * 执行流水线任务
	 */

	void runCiWithVars(CiReq ciReq);

	/**
	 *  获取流水线执行的环境变量列表
	 *
	 * @param appServiceId
	 * @param branch
	 * @param module
	 * @return
	 */
	List<AppServicePipelineEnvsResp> getEnvs(String appServiceId, String branch , String module);


	/**
	 *
	 * 查询应用服务最近运行的流水线记录
	 * @param appServiceIds
	 * @return
	 */
	Map<String,AppServicePipelinePageResp> findAppServiceLatestJobs(List<String> appServiceIds);
}
