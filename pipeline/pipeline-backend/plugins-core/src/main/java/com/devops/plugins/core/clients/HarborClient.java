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

import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.common.retrofit.Client;
import com.devops.plugins.common.retrofit.ClientUtils;
import com.devops.plugins.core.config.HarborConfig;
import com.devops.plugins.core.pojo.harbor.HarborProjectVO;
import com.devops.plugins.core.retrofit.HarborService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;

/**
 * @author sheep
 * @create 2022-03-11 16:29
 */
@Component
public class HarborClient {

    @Autowired
    private HarborConfig harborConfig;

    private HarborService getHarborService() {
        Client client = ClientUtils.basicClient(harborConfig.getUrl(), harborConfig.getUsername(), harborConfig.getPassword()).getClient();
        HarborService harborService = client.type(HarborService.class);
        return harborService;
    }



    public void createProject(String projectName){
        try {
            Client client = ClientUtils.basicClient(harborConfig.getUrl(), harborConfig.getUsername(), harborConfig.getPassword()).getClient();
            HarborService harborService = client.type(HarborService.class);
            Response<HarborProjectVO> exist = harborService.getProject(projectName).execute();
            if(!exist.isSuccessful()) {
                Response<Void> result = null;
                result = harborService.insertProject(new HarborProjectVO(
                        projectName, "false")).execute();
                if (result.raw().code() != 201) {
                    throw new BusinessRuntimeException(ABizCode.FAIL, result.errorBody().string());
                }
            }
        } catch (IOException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage());
        }
    }


//    public void updateProject() {
//        try {
//            Client client = ClientUtils.basicClient(harborConfig.getUrl(), harborConfig.getUsername(), harborConfig.getPassword()).getClient();
//            HarborService harborService = client.type(HarborService.class);
//            Response<List<HarborProjectVO>> result = null;
//            result = harborService.listProjects(1, 100).execute();
//            List<HarborProjectVO> projectVOS = result.body();
//            for(HarborProjectVO harborProjectVO:projectVOS) {
//                    MetaData metaData = harborProjectVO.getMetadata();
//                    metaData.setHarborPublic("false");
//                    harborProjectVO.setMetadata(metaData);
//                    harborService.updateProjects(harborProjectVO.getProject_id(), harborProjectVO);
//            }
//            if (result.raw().code() != 201) {
////                throw new BusinessRuntimeException(ABizCode.FAIL, result.errorBody().string());
//            }
//        } catch (IOException e) {
//            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage());
//        }
//    }

}
