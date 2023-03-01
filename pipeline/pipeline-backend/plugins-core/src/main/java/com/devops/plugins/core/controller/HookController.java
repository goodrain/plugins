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

package com.devops.plugins.core.controller;


import com.devops.plugins.common.result.R;
import com.devops.plugins.core.service.IAppServiceVersionService;
import com.devops.plugins.core.service.WebHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author sheep
 * @create 2022-01-10 15:14
 */
@RestController
@RequestMapping("/hook")
@ApiIgnore
public class HookController {

    @Autowired
    IAppServiceVersionService appServiceVersionService;
    @Autowired
    WebHookService webHookService;

    /**
     *
     * gitlab-ci，通知生成应用服务版本
     *
     * @param gitlabProjectId
     * @param version
     * @param commit
     * @return
     */
    @GetMapping("ci")
    public R createAppVersion( @RequestParam("gitlabProjectId") String gitlabProjectId, @RequestParam("version") String version, @RequestParam("commit") String commit, @RequestParam("branch") String branch, @RequestParam(value = "module", required = false) String module,  @RequestParam(value = "image", required = false) String image){
        appServiceVersionService.create(gitlabProjectId,  version, commit, branch, module, image);
        return R.ok();
    }



    /**
     * gitlab-project webhook, 生成commit, branch ,pipeline
     *
     * @param body
     * @return
     */
    @PostMapping
    public R receiveGitlabWebHook(@RequestBody String body){
        webHookService.receiveGitlabWebhook(body);
        return R.ok();
    }
}
