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

package com.devops.plugins.core.pojo.enums;

import java.util.Arrays;

/**
 * @author sheep
 * @create 2022-01-06 10:39
 */
public enum AppServiceStatusEnum {

    //启用
    ENABLE("0", "ENABLE"),

    //禁用
    DISABLE("1", "DISABLE");

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String value;
    private String text;


    AppServiceStatusEnum(String value, String text){}

    public static AppServiceStatusEnum of(int value) {
        return Arrays.stream(values())
                .filter(appServiceStatusEnum-> appServiceStatusEnum.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}
