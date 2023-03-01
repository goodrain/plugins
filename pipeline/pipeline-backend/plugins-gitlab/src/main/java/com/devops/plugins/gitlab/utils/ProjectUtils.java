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

package com.devops.plugins.gitlab.utils;


import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import org.apache.commons.lang3.tuple.MutablePair;

public class ProjectUtils {


    public static MutablePair<String, String> parseUrl(String url){
        MutablePair<String, String> result = new MutablePair<>();
        try {
            String[] url0 = url.split("//");
            String[] url1 = url0[1].split("/");
            result.setLeft(String.format("%s//%s", url0[0], url1[0]));
            result.setRight(url1[url1.length-1].split("\\.git")[0]);
        } catch (Exception e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "仓库地址不合法");
        }
        return result;
    }

    public static MutablePair<String, String> parseUrl(String url, String username, String password){
        MutablePair<String, String> result = new MutablePair<>();
        try {
            String[] url0 = url.split("//");
            String[] url1 = url0[1].split("/");
            result.setLeft(String.format("%s//%s:%s@%s", url0[0], username, password, url1[0]));
            result.setRight(url1[url1.length-1].split("\\.git")[0]);
        } catch (Exception e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, "仓库地址不合法");
        }
        return result;
    }
}
