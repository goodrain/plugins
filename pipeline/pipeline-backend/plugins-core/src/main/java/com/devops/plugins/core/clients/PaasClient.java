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

package com.devops.plugins.core.clients;

import com.alibaba.fastjson.JSONObject;
import com.devops.plugins.common.context.SecurityContextHolder;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.common.retrofit.ClientUtils;
import com.devops.plugins.core.pojo.rainbond.*;
import com.devops.plugins.core.retrofit.PaasService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

/**
 * @author sheep
 * @create 2022-03-11 15:07
 */
@Component
public class PaasClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${plugins.rainbond.url}")
    private String paasUrl;


    private PaasService getPaasService() {
        String token = SecurityContextHolder.getToken();
        ClientUtils clientUtils = ClientUtils.client(paasUrl, "", token);
        return clientUtils.getClient().type(PaasService.class);
    }


    public String getNickName(){
        try {
            Response<RainbondUserResp> userRespResponse = getPaasService().getCurrentUser().execute();
            if (userRespResponse.isSuccessful()) {
                return userRespResponse.body().getBean().getRealName();
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, "获取rainbond用户名称失败");
            }
        }catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    public RainbondTeamPageResult listTeams() {
        try {
            Response<RainbondTeamPageResult> teamsResp = getPaasService().listTeams().execute();
            if (teamsResp.isSuccessful()) {
                return teamsResp.body();
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, "获取rainbond团队失败");
            }
        }catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }


    public List<RainbondApp> listApps(String teamId, String regionName) {
        try {
            Response<List<RainbondApp>> teamsResp = getPaasService().listApps(teamId,regionName).execute();
            if (teamsResp.isSuccessful()) {
                return teamsResp.body();
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, "获取rainbond应用失败");
            }
        }catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    public MutablePair<Boolean, String> serviceAdd(String teamCode, String regionCode, String appId,  RainbondServiceAdd serviceAddReq) {
        MutablePair<Boolean, String> result = new MutablePair<>();
        try {
            Response<RainbondResult<RainbondServiceAddResp>> paasResultResponse = getPaasService().ServiceAdd(teamCode, regionCode, appId , serviceAddReq).execute();
            if (paasResultResponse.isSuccessful()) {
                result.setLeft(true);
                result.setRight(paasResultResponse.body().getData().getBean().getServiceId());
                return result;
            } else {
                RainbondResult errorResult = JSONObject.parseObject(paasResultResponse.errorBody().string(), RainbondResult.class);
                logger.error(JSONObject.toJSONString(serviceAddReq));
                logger.error(String.format("部署失败:%s", errorResult.getMsgShow()));
                result.setLeft(false);
                result.setRight(errorResult.getMsgShow());
                return result;
            }
        } catch (IOException e) {
            result.setLeft(false);
            result.setRight(e.getMessage());
            return result;
        }
    }




    public MutablePair<Boolean, String> serviceBuild(String teamCode, String regionCode, String appId,  String serviceId, RainbondServiceBuild serviceBuild) {
        MutablePair<Boolean, String> result = new MutablePair<>();
        try {
            Response<Void> paasResultResponse = getPaasService().ServiceBuild(teamCode, regionCode, appId, serviceId, serviceBuild).execute();
            if (paasResultResponse.isSuccessful()) {
                result.setLeft(true);
                result.setRight(null);
                return result;
            } else {
                logger.error(JSONObject.toJSONString(serviceBuild));
                result.setLeft(false);
                result.setRight("部署失败");
                return result;
            }
        } catch (IOException e) {
            result.setLeft(false);
            result.setRight(e.getMessage());
            return result;
        }
    }


    public RainbondRegionPageResult listTeamRegions(String teamId) {
        try {
            Response<RainbondRegionPageResult> paasResultResponse = getPaasService().listRegions(teamId).execute();
            if (paasResultResponse.isSuccessful()) {
                return paasResultResponse.body();
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, "查询paas团队绑定的集群失败");
            }
        } catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "查询paas团队绑定的集群失败", e);
        }
    }


    public List<RainbondService> serviceGet(String teamCode, String regionCode, String appId) {
        try {
            Response<List<RainbondService>> paasResultResponse = getPaasService().ServiceGet(teamCode, regionCode, appId).execute();
            if (paasResultResponse.isSuccessful()) {
                return paasResultResponse.body();
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, "查询paas组件列表失败");
            }
        } catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "查询paas组件列表失败", e);
        }
    }


    public RainbondService serviceGet(String teamCode, String regionCode, String appId, String serviceId) {
        try {
            Response<RainbondService> paasResultResponse = getPaasService().ServiceDetail(teamCode, regionCode, appId, serviceId).execute();
            if (paasResultResponse.isSuccessful()) {
                return paasResultResponse.body();
            } else {
                throw new BusinessRuntimeException(ABizCode.FAIL, "查询paas组件详情失败");
            }
        } catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "查询paas组件详情失败", e);
        }
    }
}
