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
import com.devops.plugins.core.dao.entity.AppServiceVersionEntity;
import com.devops.plugins.core.pojo.vo.req.appServiceVersion.AppServiceVersionPageReq;
import com.devops.plugins.core.pojo.vo.req.appServiceVersion.DeployReq;
import com.devops.plugins.core.pojo.vo.resp.app.AppResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.AppServiceVersionPageResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.PaasDependencyResp;
import com.devops.plugins.core.pojo.vo.resp.appServiceVersion.PaasVariableResp;
import java.util.List;


/**
 * <p>
 * 应用服务版本表 Service 接口
 * </p>
 *
 * @author huangtf
 * @Date 2022-01-06 18:25:34
 */
public interface IAppServiceVersionService extends IService<AppServiceVersionEntity> {


    /**
     * 分页
     * @param query
     * @param page
     * @return
     */
	Page<AppServiceVersionPageResp> findPage(AppServiceVersionPageReq query, ReqPage<AppServiceVersionPageReq> page);


	/**
	 * 创建应用服务版本
	 *
	 * @param gitlabProjectId
	 * @param version
	 * @param commit
	 * @param branch
	 * @param module
	 */
	void create(String gitlabProjectId, String version, String commit, String branch, String module, String image);


	/**
	 * 查询可部署的应用
	 *
	 * @param appServiceId
	 * @param teamId
	 * @param regionName
	 * @param version
	 * @return
	 */
	List<AppResp> listApps(String appServiceId, String teamId, String regionName, String version);



	/**
	 *
	 * 部署应用服务
	 *
	 */
	void deploy(DeployReq deployReq);
}
