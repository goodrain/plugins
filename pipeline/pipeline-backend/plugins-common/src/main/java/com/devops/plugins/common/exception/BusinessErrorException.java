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

public class BusinessErrorException extends Exception {
  private static final long serialVersionUID = -6357149550353160810L;

  private final ABizCode error;

  private static final int DEFAULT_ERROR_CODE = -99;

  public BusinessErrorException(String message) {
    this(ABizCode.FAIL, message);
  }

  public BusinessErrorException(ABizCode error) {
    super(error.getMsg());
    this.error = error;
  }

  public BusinessErrorException(ABizCode error, String message) {
    super(message);
    this.error = error;
  }

  public BusinessErrorException(ABizCode error, Throwable cause) {
    super(error.toString(), cause);
    this.error = error;
  }

  public BusinessErrorException(Throwable cause) {
    super(cause.getMessage(), cause);
    this.error = ABizCode.EXCEPTION;
  }

  public ABizCode getError() {
    return this.error;
  }
}
