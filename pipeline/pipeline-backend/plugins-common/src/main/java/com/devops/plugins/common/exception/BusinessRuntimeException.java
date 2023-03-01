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


import com.devops.plugins.common.result.ABizCode;

public class BusinessRuntimeException extends RuntimeException {
    protected ABizCode bizCode;

    public BusinessRuntimeException(Throwable e) {
        super(e);
        bizCode=ABizCode.EXCEPTION;
    }

    public BusinessRuntimeException(ABizCode bizCode) {
        super(bizCode.getMsg());
        this.bizCode=bizCode;
    }

    public BusinessRuntimeException(String message, Throwable e) {
        super(message, e);
        this.bizCode=new ABizCode(ABizCode.FAIL.getCode(),ABizCode.FAIL.getI18nKey(),message);
    }

    public BusinessRuntimeException(ABizCode bizCode, String message) {
        super(message);
        this.bizCode=new ABizCode(bizCode.getCode(),bizCode.getI18nKey(),message);
    }

    public BusinessRuntimeException(ABizCode bizCode, String message, Throwable throwable) {
        super(message, throwable);
        this.bizCode=new ABizCode(bizCode.getCode(),bizCode.getI18nKey(),message);
    }

    public BusinessRuntimeException(String message) {
        super(message);
        this.bizCode=new ABizCode(ABizCode.FAIL.getCode(),ABizCode.FAIL.getI18nKey(),message);
    }

    public ABizCode getResultCode(){
        return this.bizCode;
    }

}
