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

package com.devops.plugins.common.constraint;

import java.util.Arrays;
import java.util.List;

/**
 * @author sheep
 * @create 2022-01-06 10:40
 */
public class PlatFormConstraint {

    public static final String  AppService_Template_Microservice = "maven多模块";

    public static final String MAVEN = "maven父子模块";

    public static final String OTHER = "其它类型";

    public static final String SERVICE_PATTERN = "^[a-z][a-z0-9-]*$";

    public static final String PROJECT_URL_PATTRRN = "^(http|https)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?git$";

    public static final Integer GITLAB_ROOT_USER_ID = 1;

    public static final String NO_COMMIT_SHA = "0000000000000000000000000000000000000000";

    public static final String REF_HEADS = "refs/heads/";

    public static final String  Job_Created = "created";

    public static final String  Job_Successed  = "success";

    public static final String  Job_Failed = "failed";

    public static final String  Job_Running = "running";

    public static final String  Job_Canceled = "canceled";

    public static final String  Job_Pending = "pending";

    public static  final String Job_Skipped = "skipped";

    public static final String DEPLOY_SUCCESS = "success";

    public static final String DEPLOY_FAILED = "failed";

    public static final String SERVICE_TYPE_EXTERNAL = "external";

    public static final String SERVICE_TYPE_INTERNAL = "internal";

    public static final String DEVOPS_VERSION = "DEVOPS_VERSION";

    public static final  String DEVOPS_BRANCH = "DEVOPS_BRANCH";

    public static final List<String>  STAGE_VALIDATE = Arrays.asList("default","include","stages","variables","workflow","after_script","artifacts","before_script","cache","image","interruptible","retry","services","tags","timeout");

    public static final String PIPELINE_TYPE_INTENAL = "internal";

    public static final String PIPELINE_TYPE_CUSTOM = "custom";

    public static final String BRANCH_MASTER = "master";

    public static final String TRUE = "true";

}
