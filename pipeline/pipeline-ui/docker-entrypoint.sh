#!/bin/bash

#
# /*
# Copyright 2023 Talkweb Co., Ltd.
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 3 of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with this program; if not, write to the Free Software Foundation,
# Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
# */
#

for file in /usr/share/nginx/html/${SHELL_ENV}/*.js;
do
    sed -i 's|GLOB_NAME_CONFIG|'${GLOB_NAME_CONFIG}'|g' $file
    sed -i 's|USER_LOGIN_URL_CONFIG|'${USER_LOGIN_URL_CONFIG}'|g' $file
    sed -i 's|SELECT_APP_CENTER_CONFIG|'${SELECT_APP_CENTER_CONFIG}'|g' $file
    sed -i 's|OAUTHOR_CONFIG|'${OAUTHOR_CONFIG}'|g' $file
    sed -i 's|OAUTHOR_ID_CONFIG|'${OAUTHOR_ID_CONFIG}'|g' $file
    sed -i 's|OAUTHOR_SECRET_CONFIG|'${OAUTHOR_SECRET_CONFIG}'|g' $file
    sed -i 's|RESOURCES_URL_CONFIG|'${RESOURCES_URL_CONFIG}'|g' $file
    sed -i 's|WUTONG_PAAS_URL_CONFIG|'${WUTONG_PAAS_URL_CONFIG}'|g' $file
    sed -i 's|GITLAB_URL_CONFIG|'${GITLAB_URL_CONFIG}'|g' $file
    sed -i 's|LOGIN_BACK_CONFIG|'${LOGIN_BACK_CONFIG}'|g' $file
done


nginx
