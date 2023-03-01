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

package com.devops.plugins.common.exception;

import com.alibaba.fastjson.JSON;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.common.result.R;
import com.devops.plugins.common.utils.LocaleUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常统一捕获
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOCALE_VALIDATE_PREFIX = "validate.";

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * HttpRequestMethodNotSupportedException http方法类型不匹配异常处理
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public R methodNotSupportedHandler(ServletWebRequest request, HttpServletResponse response, Exception e) {
        printErrorLog(e, request);
        return R.fail(ABizCode.EXCEPTION, e.getMessage());
    }


    /**
     * MethodArgumentNotValidException 参数校验异常统一拦截
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class,MethodArgumentNotValidException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class})
    @ResponseBody
    public R<?> argumentNotValidHandler(ServletWebRequest request, HttpServletResponse response, Exception e) {
        String errorMsg = "";
        if(e instanceof BindException){
            BindException bindException=(BindException)e;
            errorMsg = bindException.getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage();
        }
        if (e instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
            errorMsg = fieldError.getDefaultMessage();
        }
        if (e instanceof ConstraintViolationException || e instanceof MissingServletRequestParameterException) {
            errorMsg = e.getMessage();
        }
        if (StringUtils.isNotBlank(errorMsg) && errorMsg.startsWith(LOCALE_VALIDATE_PREFIX)) {
            //在需要支持国际化的情况下，参数校验标签中需要填写国际化资源文件中的key，然后动态获取值
            errorMsg = LocaleUtils.get(errorMsg);
        }
        printErrorLog(e, request);
        return R.fail(ABizCode.INVALID_PARAM, errorMsg);
    }

    /**
     * Exception 参数类型错误、日期格式化错误、枚举参数取值错误导致的异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = InvalidFormatException.class)
    @ResponseBody
    public R<?> invalidFormatExceptionHandler(ServletWebRequest request, HttpServletResponse response, Exception e) {
        printErrorLog(e, request);
        return R.fail(ABizCode.ILLEGAL_PARAMETER_FORMAT);
    }

    /**
     * Exception 参数类型错误、日期格式化错误、枚举参数取值错误导致的异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public R<?> messageNotReadableExceptionHandler(ServletWebRequest request, HttpServletResponse response, Exception e) {
        printErrorLog(e, request);
        if (e.getCause() instanceof InvalidFormatException) {
            Object invalidValue = ((InvalidFormatException) e.getCause()).getValue();
            return R.fail(ABizCode.ILLEGAL_PARAMETER_VALUE, "", ": " + invalidValue.toString());
        }
        return R.fail(ABizCode.ILLEGAL_PARAMETER_VALUE);
    }

    /**
     * BusinessErrorException 自定义业务异常统一拦截
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessErrorException.class)
    @ResponseBody
    public R<?> businessExceptionHandler(ServletWebRequest request, BusinessErrorException e) {
        printErrorLog(e, request);
        if (StringUtils.isEmpty(e.getMessage())) {
            return R.fail(e.getError());
        } else {
            return R.fail(e.getError(), e.getMessage());
        }
    }

    /**
     * BusinessErrorException 自定义业务异常统一拦截
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessRuntimeException.class)
    @ResponseBody
    public R<?> businessExceptionHandler(ServletWebRequest request, BusinessRuntimeException e) {
        printErrorLog(e, request);
        if (StringUtils.isEmpty(e.getMessage())) {
            return R.fail(e.getResultCode());
        } else {
            return R.fail(e.getResultCode(), e.getMessage());
        }
    }

    /**
     * Exception 未知异常统一拦截
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public R<?> exceptionHandler(ServletWebRequest request, HttpServletResponse response, Exception e) {
//        printErrorLog(e, request);
//        return R.fail(ABizCode.EXCEPTION, ABizCode.EXCEPTION.getMsg());
//    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public void exceptionHandler(ServletWebRequest request, HttpServletResponse response, Exception e) throws Exception {
        printErrorLog(e, request);
        throw e;
    }

    private void printErrorLog(Exception ex, ServletWebRequest request) {
        try {
            String parameters = "";
            if (request.getHttpMethod() == HttpMethod.GET) {
                parameters = JSON.toJSONString(request.getParameterMap());
            } else {
                /*byte[] bodyBytes = StreamUtils.copyToByteArray(request.getRequest().getInputStream());
                parameters = new String(bodyBytes, request.getRequest().getCharacterEncoding());*/
                parameters = JSON.toJSONString(request.getParameterMap());
            }
            //byte[] bodyBytes = StreamUtils.copyToByteArray(request.getRequest().getInputStream());
            //String body = new String(bodyBytes, request.getRequest().getCharacterEncoding());
            if (log.isErrorEnabled()) {
                log.error("统一异常拦截 -> uri= {} params={} ",
                        request.getHttpMethod() + ": " + request.getRequest().getRequestURI(),
                        parameters,
                        ex
                );
            }
        } catch (Exception e) {
            // e.printStackTrace();
            if (log.isErrorEnabled()) {
                log.error("打印异常错误:{}", ex.getMessage(), e);
            }
        }
    }
}
