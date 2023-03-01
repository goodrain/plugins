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

package com.devops.plugins.common.interceptor;

import com.devops.plugins.common.constraint.HeaderParamsConstants;
import com.devops.plugins.common.context.SecurityContextHolder;
import com.devops.plugins.common.exception.BusinessErrorException;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.common.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中
 */

public class HeaderInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(HeaderInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 用户信息
        String userRealName = ServletUtils.getHeader(request, HeaderParamsConstants.USER_REAL_NAME);


        //设置到线程本地变量
        SecurityContextHolder.setUserRealName(userRealName);

        // 认证token
        String authorization = request.getHeader(HeaderParamsConstants.AUTHORIZATION);
        if (null != authorization) {
            SecurityContextHolder.getLocalMap().put(HeaderParamsConstants.AUTHORIZATION, authorization);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}
