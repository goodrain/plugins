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

public class GitlabCIConstraint {

    public static final String MAVEN_PACKAGE  = "mvn clean package -DskipTests=true";

    public static final String SONAR_MAVEN =  "mvn --batch-mode verify sonar:sonar -DskipTests=true -Dsonar.host.url=${SONARQUBE_URL}  -Dsonar.login=${SONARQUBE_TOKEN}  -Dsonar.projectKey=${PROJECT_CODE}:${SERVICE_CODE}";

    public static final String YARN_INSTALL = "yarn install";

    public static final String YARN_BUILD = "yarn run build";

    public static final String SONAR_YARN = "scanner to do";

    public static final String DOCKER_LOGIN = "docker login -u ${HARBOR_USERNAME} -p ${HARBOR_PASSWORD} ${HARBOR_SCHEMAL}//${HARBOR_URL}";

    public static final String DOCKER_BUILD = "docker build -t  ${HARBOR_URL}/${PROJECT_SET_CODE}-${PROJECT_CODE}/${MODULE}:${DEVOPS_VERSION} .";

    public static final String DOCKER_PUSH = "docker push ${HARBOR_URL}/${PROJECT_SET_CODE}-${PROJECT_CODE}/${MODULE}:${DEVOPS_VERSION}";

    public static final String DEVOPS_HTTP_REQUEST_RESULT = "http_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} \"${GATEWAY}/hook/ci?gitlabProjectId=${CI_PROJECT_ID}&version=${DEVOPS_VERSION}&commit=${CI_COMMIT_SHA}&branch=${CI_COMMIT_REF_SLUG}&module=${SUB_SERVICE}&image=${IMAGE}\"`\n" +
            "      if [ \"$http_status_code\" != \"200\" ]; then\n" +
            "      echo $http_status_code\n" +
            "      echo \"上传应用服务版本失败\"\n" +
            "      exit 1\n" +
            "      fi";
}
