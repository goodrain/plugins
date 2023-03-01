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

package com.devops.plugins.common.result;

import com.devops.plugins.common.utils.LocaleUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * api通用返回对象
 * @param <T>
 */
@ApiModel(description = "通用返回对象")
@JsonIgnoreProperties(ignoreUnknown = true,allowGetters = true,allowSetters = true)
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "状态码", example = "0", position = 0)
    private String code;
    @ApiModelProperty(notes = "状态描述", example = "操作成功", position = 1)
    private String msg;
    // 泛型属性，不能指定example，swagger文档无法显示该属性的真实VO
    // 错误示例：   @ApiModelProperty(notes = "响应数据", example = "响应数据", position = 2)
    @ApiModelProperty(notes = "响应数据", position = 2)
    private T data;

    public R() {

    }

    public R(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private static  <T> R<T> r(String code, String msg, T data){
        return new R(code, msg, data);
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isOK() {
        return ABizCode.OK.getCode().equals(this.code);
    }

    /**
     * 获取OK状态下国际化语言信息
     * @param customMsg 自定义显示信息
     * @return
     */
    private static String getOkLocaleMsg(String customMsg){
        String message=customMsg;
        if(StringUtils.isBlank(customMsg)){
            message= LocaleUtils.get(ABizCode.OK.getI18nKey());
            if(message.startsWith(ABizCode.LOCALE_BASE_PREFIX)){
                message=ABizCode.OK.getMsg();
            }
        }
        return message;
    }

    /**
     * 获取OK状态下国际化语言信息
     * @param
     * @return
     */
    private static String getOkLocaleMsg(ABizCode bizCode) {
        String message = LocaleUtils.get(ABizCode.OK.getI18nKey());
        if (message.startsWith(ABizCode.LOCALE_BASE_PREFIX)) {
            message = ABizCode.OK.getMsg();
        }
        return message;
    }

    /**
     * 获取Fail状态下自定义国际化语言信息
     * @param customMsg 自定义显示信息
     * @return
     */
    private static String getFailLocaleMsg(String customMsg,ABizCode bizCode){
        if(StringUtils.isBlank(customMsg)) {
            if(null == bizCode){
                customMsg= LocaleUtils.get(ABizCode.FAIL.getI18nKey());
                if(customMsg.startsWith(ABizCode.LOCALE_BASE_PREFIX)){
                    customMsg=ABizCode.FAIL.getMsg();
                }
            }else{
                customMsg= LocaleUtils.get(bizCode.getI18nKey());
                if(customMsg.startsWith(ABizCode.LOCALE_BASE_PREFIX)) {
                    customMsg = bizCode.getMsg();
                }
            }
        }
        return customMsg;
    }

    /**
     * success通用返回，默认code=“0”、msg=操作成功、data=null
     *
     * @param
     * @return
     */
    public static <T> R<T> ok() {
        return r(ABizCode.OK.getCode(), getOkLocaleMsg(ABizCode.OK), null);
    }

    /**
     * success通用返回，默认code=“0”、msg=操作成功，data自定义
     *
     * @param data
     * @return
     */
    public static <T> R<T> ok(T data) {
        return r(ABizCode.OK.getCode(), getOkLocaleMsg(ABizCode.OK), data);
    }

    /**
     * success通用返回，默认code=“0”、data=null、msg=自定义
     * 此方法如果要支持国际化，传入的msg就要根据当前请求配置的语言版本动态设置
     * @param message
     * @param <T>
     * @return
     */
    public static <T> R<T> ok(String message) {
        return r(ABizCode.OK.getCode(), getOkLocaleMsg(message), null);
    }

    /**
     * success通用返回，默认code=“0”，msg和data自定义
     * 此方法如果要支持国际化，传入的msg就要根据当前请求配置的语言版本动态设置
     * @param message i18n资源文件中的key
     * @param data
     * @return
     */
    public static <T> R<T> ok(String message, T data) {
        return r(ABizCode.OK.getCode(), getOkLocaleMsg(message), data);
    }

    /**
     * failure通用返回，默认code=“0-001”，msg=操作失败，data=null
     * @param
     * @return
     */
    public static <T> R<T> fail() {
        return r(ABizCode.FAIL.getCode(),getFailLocaleMsg("",ABizCode.FAIL),null);
    }

    /**
     * failure通用返回，默认code=“0-001”，msg自定义
     * 此方法如果要支持国际化，传入的msg就要根据当前请求配置的语言版本动态设置
     * @param msg
     * @return
     */
    public static <T> R<T> fail(String msg) {
        return r(ABizCode.FAIL.getCode(), getFailLocaleMsg(msg,null),null);
    }

    /**
     * failure通用返回，默认code=“0”，msg和data自定义
     * 此方法如果要支持国际化，传入的msg就要根据当前请求配置的语言版本动态设置
     * @param msg
     * @param data
     * @return
     */
    public static <T> R<T> fail(String msg, T data) {
        return r(ABizCode.FAIL.getCode(), getFailLocaleMsg(msg,null), data);
    }

    /**
     * failure通用返回，默认code、msg自定义
     * 此方法如果要支持国际化，传入的msg就要根据当前请求配置的语言版本动态设置
     * @param msg
     * @return
     */
    public static <T> R<T> fail(String code, String msg) {
        return r(code, getFailLocaleMsg(msg,null), null);
    }

    /**
     * code 、msg和data自定义
     * 此方法如果要支持国际化，传入的msg就要根据当前请求配置的语言版本动态设置
     * @param msg
     * @param data
     * @return
     */
    public static <T> R<T> fail(String code,String msg, T data) {
        return r(code, getFailLocaleMsg(msg,null), data);
    }

    /**
     * failure通用返回，code自定义
     * 支持中英文
     * @param bizCode
     * @return
     */
    public static <T> R<T> fail(ABizCode bizCode) {
        return r(bizCode.getCode(), getFailLocaleMsg("",bizCode),null);
    }

    /**
     * failure通用返回,code、msg自定义
     *
     * @param code
     * @param msg
     * @return
     */
    public static <T> R<T> fail(ABizCode code, String msg) {
        return r(code.getCode(), getFailLocaleMsg(msg, code), null);
    }

    /**
     * failure通用返回,code、msg自定义
     *
     * @param bizCode
     * @param msg
     * @param extra 额外补充信息
     * @return
     */
    public static <T> R<T> fail(ABizCode bizCode, String msg,String  extra) {
        return r(bizCode.getCode(), getFailLocaleMsg(msg, bizCode) + extra, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "R{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
