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
import com.devops.plugins.core.dao.entity.AppServiceEntity;
import com.devops.plugins.core.dao.entity.CustomPipelineEnvsEntity;
import com.devops.plugins.core.pojo.vo.req.appService.*;
import com.devops.plugins.core.pojo.vo.resp.appService.*;

import java.util.List;


/**
 * <p>
 * 应用服务表 Service 接口
 * </p>
 *
 * @author sheep
 * @Date 2022-01-06 10:07:19
 */
public interface IAppServiceService  extends IService<AppServiceEntity>{

    /**
     * 统计
     *
     * @param query
     * @return
     */
    Long findCount(AppServiceEntity query);

    /**
     * 分页
     *
     * @param
     * @param page
     * @return
     */
    Page<AppServicePageResp> findPage(AppServicePageReq appServicePageReq, ReqPage<AppServicePageReq> page);

    /**
     * 列表
     *
     * @param query
     * @return
     */
    List<AppServiceEntity> findList(AppServiceEntity query);


    /**
     * 根据id动态更新传入了值的字段
     *
     * @param appServiceUpdateReq
     * @return
     */
    boolean update(AppServiceUpdateReq appServiceUpdateReq);


    /**
     * 删除应用服务
     *
     * @param appServiceId
     */
    void  delete(String appServiceId);


    /**
     * 创建应用服务
     *
     * @param
     */
    void  create(AppServiceAddReq appServiceAddReq);


    /**
     * 获取应用服务代码仓库的clone-url
     *
     * @param appServiceId
     * @return
     */
    AppServiceUrlResp getAppServiceUrl(String appServiceId);


    /**
     * 获取应用服务代码仓库的介绍
     *
     * @param appServiceId
     * @return
     */
    String getAppServiceDesc(String appServiceId);


    /**
     * 根据团队编码查询应用服务列表
     *
     * @param teamCode
     */
    List<AppServiceListResp> listAppServiceByTeam(String teamCode);

    /**
     * 查询应用服务模板
     * @return
     */
    List<String> listAppTemplate();


    /**
     *
     * 获取应用服务子模块（微服务类型）
     *
     * @return
     */
    List<String> listSubService(String id);


    /**
     * 添加应用服务子模块（微服务类型）
     *
     * @param id
     * @param module
     */
    void  addSubService(String id, List<String> module);



    /**
     * 删除应用服务子模块（微服务类型）
     *
     * @param id
     * @param module
     */
    void  deleteSubService(String id, List<String> module);


//    /**
//     * 分页查询项目成员列表
//     *
//     * @param query
//     * @param page
//     * @return
//     */
//    Page<ProjectMemberResp> findMemberPage(ProjectMembePageReq query, Page<ProjectMemberEntity> page);


    /**
     * 查询应用服务是否可以开启自动部署
     *
     * @param id
     * @return
     */
    Boolean  canDeploy(String id, String sub_id);

    /**
     *  根据应用id查询应用详情
     */
    AppServiceDetailResp selectById(String id);


    /**
     * 校验外部gitlab仓库
     *
     * @param url
     * @param token
     * @param username
     * @param password
     * @return
     */
    Boolean check(String url, String token , String username, String password);




    /**
     *
     * 更新应用服务对应仓库文件
     *
     */
    void saveOrUpdateRepositoryFileAndEnvs(String appServiceId, String yamlContent, String file, List<CustomPipelineEnvsEntity> customPipelineEnvsEntities);

    /**
     *
     * 删除应用服务对应仓库文件
     *
     */
    void deleteRepositoryFile(String appServiceId, String file);


    /**
     *  修改应用服务外部服务仓库地址
     *
     * @param id
     * @param appServiceUrlModifyVO
     */
    void modifyExternel(String id, AppServiceUrlModifyReq appServiceUrlModifyVO);


    /**
     *  获取外部应用服务外部服务仓库信息
     *
     * @param id
     * @param
     */
    AppServiceUrlModifyResp getExternel(String id);

    /**
     *
     *  获取应用服务外部服务仓库地址
     * @param id
     * @return
     */
    AppServiceRepositoryUrlResp getRepositoryUrl(String id);
}
