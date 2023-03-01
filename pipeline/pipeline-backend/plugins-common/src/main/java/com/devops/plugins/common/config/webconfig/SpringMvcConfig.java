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

package com.devops.plugins.common.config.webconfig;

import com.devops.plugins.common.interceptor.HeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC拦截器配置
 */

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {


    /** 放行不需要拦截swagger地址 */
    public static final String[] excludeSwaggerUrls = {
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/error/**",
            "/hook/**",
            "/swagger-ui.html/**",
            "/doc.html/**"
    };



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludeSwaggerUrls)
                .order(-10);
    }



    /**
     * 自定义请求头拦截器
     */
    @Bean
    public HeaderInterceptor getHeaderInterceptor() {
        return new HeaderInterceptor();
    }

}
