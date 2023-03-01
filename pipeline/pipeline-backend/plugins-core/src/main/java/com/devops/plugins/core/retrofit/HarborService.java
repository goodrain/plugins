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

import com.devops.plugins.core.pojo.harbor.HarborProjectVO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * @author sheep
 * @create 2022-01-10 11:13
 */
public interface HarborService {


    @POST("api/v2.0/projects")
    Call<Void> insertProject(@Body HarborProjectVO harborProject);


    @GET("api/v2.0/projects")
    Call<List<HarborProjectVO>> listProjects(@Query(value = "page") Integer page, @Query(value = "page_size")Integer page_size);

    @GET("api/v2.0/projects/{project_name_or_id}")
    Call<HarborProjectVO> getProject(@Path(value = "project_name_or_id") String projectName);



    @PUT("api/v2.0/projects/{project_name}")
    Call<Void>  updateProjects(@Path(value = "project_name")String name, @Body HarborProjectVO harborProject);



}
