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

package com.devops.plugins.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.devops.plugins.**.controller.**",
        "com.devops.plugins.**.service.**",
        "com.devops.plugins.**.async.**",
        "com.devops.plugins.**.exception.**",
        "com.devops.plugins.**.dao.**",
        "com.devops.plugins.**.config.**",
        "com.devops.plugins.**.clients.**",
        "com.devops.plugins.**.quartz.**"
})
@EnableScheduling
@EnableAsync
public class StarterApplication {

    public static void main(String[] args) {

        SpringApplication.run(StarterApplication.class, args);

    }
}
