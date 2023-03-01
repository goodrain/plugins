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

package com.devops.plugins.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenParam = new ParameterBuilder();
        ParameterBuilder tenantIdParam = new ParameterBuilder();
        List<Parameter> headerParams = new ArrayList<Parameter>();
        tenantIdParam.name("TenantId").
                description("租户ID").
                modelRef(new ModelRef("string")).
                parameterType("header").
                defaultValue("1").
                required(true).
                build();
        tokenParam.name("Authorization").
                description("访问token").
                modelRef(new ModelRef("string")).
                parameterType("header").
                defaultValue("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxIiwiYXVkIjoiaWRhc3MiLCJ1bmFtZSI6ImFkbWluIiwiaXNzIjoidGFsa3dlYiIsInJuZCI6IjN4bm5xN3o5IiwiZXhwIjoxNjY5ODk2OTQ5LCJpYXQiOjE2MzgzNjA5NTB9.sa-v_g_4gyIC78_8xF-INXB8QTRj3_EfGJ9O9nMCrXU").
                required(true).
                build();
        headerParams.add(tenantIdParam.build());
        headerParams.add(tokenParam.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.devops.plugins.core.controller"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(headerParams);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("Devops插件API文档")
                .version("v1.0.0")
                .title("Devops插件API文档")
                .build();
    }
}
