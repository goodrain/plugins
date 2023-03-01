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

package com.devops.plugins.core.retrofit;

import com.devops.plugins.core.pojo.rainbond.*;
import liquibase.pro.packaged.G;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * @author sheep
 * @create 2022-02-28 9:21
 */
public interface PaasService {


    /**
     * 获取团队列表
     *
     * @return
     */
    @GET("/openapi/v1/teams")
    Call<RainbondTeamPageResult> listTeams();



    @GET("/openapi/v1/currentuser")
    Call<RainbondUserResp>  getCurrentUser();


    /**
     * 获取应用列表
     *
     * @param teamId
     * @param regionName
     * @return
     */
    @GET("/openapi/v1/teams/{team_id}/regions/{region_name}/apps")
    Call<List<RainbondApp>> listApps(@Path(value = "team_id")String teamId, @Path(value = "region_name")String regionName);


    /**
     *
     * 创建组件
     *
     * @param teamCode
     * @param regioName
     * @param groupId
     * @param rainbondServiceAdd
     * @return
     */
    @POST("/openapi/v1/teams/{team_name}/regions/{region_name}/apps/{group_id}/services")
    Call<RainbondResult<RainbondServiceAddResp>>  ServiceAdd(@Path(value = "team_name")String teamCode, @Path(value = "region_name")String regioName, @Path(value = "group_id")String groupId, @Body RainbondServiceAdd rainbondServiceAdd);

    /**
     * 部署业务组件
     *
     * @param serviceBuild
     * @return
     */
    @POST("/openapi/v1/teams/{team_id}/regions/{region_name}/apps/{app_id}/services/{service_id}/build")
    Call<Void>  ServiceBuild(@Path(value = "team_id")String teamId, @Path(value = "region_name")String regionName, @Path(value = "app_id")String appId,  @Path(value = "service_id")String serviceId, @Body RainbondServiceBuild serviceBuild);





    /**
     * 获取组件列表
     *
     */
    @GET("/openapi/v1/teams/{team_id}/regions/{region_name}/apps/{app_id}/services")
    Call<List<RainbondService>> ServiceGet(@Path(value = "team_id")String team_id, @Path(value = "region_name") String region_name, @Path(value = "app_id") String app_id);


    /**
     * 获取组件列表
     *
     */
    @GET("/openapi/v1/teams/{team_id}/regions/{region_name}/apps/{app_id}/services/{service_id}")
    Call<RainbondService> ServiceDetail(@Path(value = "team_id")String team_id, @Path(value = "region_name") String region_name, @Path(value = "app_id") String app_id, @Path(value = "service_id") String  serviceId);


    /**
     *
     *  获取团队绑定的集群列表
     *
     * @param teamId
     * @return
     */
    @GET("/openapi/v1/teams/{team_id}/regions")
    Call<RainbondRegionPageResult> listRegions(@Path(value = "team_id")String teamId);




}
