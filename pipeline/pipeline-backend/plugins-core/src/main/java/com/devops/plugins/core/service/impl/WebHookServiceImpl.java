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

package com.devops.plugins.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.devops.plugins.core.pojo.gitlab.JobWebHook;
import com.devops.plugins.core.pojo.gitlab.PipelineWebHook;
import com.devops.plugins.core.pojo.gitlab.PushWebHook;
import com.devops.plugins.core.service.IAppServicePipelineJobService;
import com.devops.plugins.core.service.IAppServicePipelineService;
import com.devops.plugins.core.service.IGitlabBranchService;
import com.devops.plugins.core.service.WebHookService;
import com.devops.plugins.core.utils.FastjsonParserConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sheep
 * @create 2022-01-11 14:50
 */
@Service
public class WebHookServiceImpl implements WebHookService {


    @Autowired
    IGitlabBranchService branchService;
    @Autowired
    IAppServicePipelineService appServicePipelineService;
    @Autowired
    IAppServicePipelineJobService appServicePipelineJobService;

    @Override
    public void receiveGitlabWebhook(String body) {
        JSONObject jsonObject = JSON.parseObject(body);
        String kind = jsonObject.get("object_kind").toString();
        switch (kind) {
            //创建分支，创建commit
            case "push":
                PushWebHook pushWebHook = JSONArray.parseObject(body, PushWebHook.class, FastjsonParserConfigProvider.getParserConfig());
                branchService.webhookBranchSync(pushWebHook);
                break;
            //流水线pipeline
            case "pipeline":
                PipelineWebHook pipelineWebHook = JSONArray.parseObject(body, PipelineWebHook.class, FastjsonParserConfigProvider.getParserConfig());
                appServicePipelineService.webhookPipelineSync(pipelineWebHook);
                break;
            //流水线pipeline-job
            case "build":
                JobWebHook jobWebHook = JSONArray.parseObject(body, JobWebHook.class, FastjsonParserConfigProvider.getParserConfig());
                appServicePipelineJobService.webhookPipelineJobSync(jobWebHook);
                break;
            default:
                break;
        }
    }
}
