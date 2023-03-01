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

import java.io.Serializable;

public class BizCode extends ABizCode implements Serializable {
    private static final long serialVersionUID = -1410736985764474415L;

    private BizCode() {
        super();
    }
    private BizCode(String code) {
        super(code);
    }
    private BizCode(String code,String i18nKey, String msg) {
        super(code,i18nKey,msg);
    }

    /** OK:0,FAIL:-1,UNDEFINED:-2 这些状态已在父类定义了，请不要覆盖 ！！！
     * 在这里定义此三类错误，这就是为了提醒，此三类错误已定义 请不要覆盖定义！！！
     public static final ABizCode OK = new ABizCode("0","操作成功");
     public static final ABizCode FAIL = new ABizCode("0-001","操作失败");
     public static final ABizCode UNDEFINED = new ABizCode("0-002","未知错误");
     public static final ABizCode IS_NULL = new ABizCode("0-003", "对象为空");
     public static final ABizCode MISS_ID = new ABizCode("0-004", "传入主键为空");
     public static final ABizCode UNEXIST = new ABizCode("0-005", "查询数据为空");
     public static final ABizCode EXIST = new ABizCode("0-006", "数据已存在");
     public static final ABizCode EXCEPTION = new ABizCode("0-007", "系统异常，操作失败");
     **/

    /**
     * 错误编码mm-xxxyyy释义：
     * mm表示项目编码，0：核心模块
     * xxx表示模块编码，
     * yyy表示具体不同的业务编码
     */

    /**
     * 用户模块错误编码，模块编码为050
     */
    public static final ABizCode USER_MISS_USERNAME = new BizCode("0-050001", "base.user-name-is-missing","用户名为空");
    public static final ABizCode USER_MISS_PASSWORD = new BizCode("0-050002", "base.password-is-missing","密码为空");
    public static final ABizCode USER_MISS_ID = new BizCode("0-050003", "base.user-id-missing","用户id为空");
    public static final ABizCode USER_UNEXIST = new BizCode("0-050004", "base.user-none-exist","用户不存在");
    public static final ABizCode USER_EXIST = new BizCode("0-050005", "base.user-already-exist","用户已存在");
    public static final ABizCode USER_PASSWORD_WRONG = new BizCode("0-050006", "base.password-incorrect","用户密码错误");
    public static final ABizCode USER_PHONE_EXIST = new BizCode("0-050007", "base.phone-already-exist","手机号码已存在");
    public static final ABizCode USER_LOG_OFF = new BizCode("0-050008", "base.invalid-user-state","用户已失效");

    /**
     * 用户组模块错误编码，模块编码为051
     */
    public static final ABizCode GROUP_MISS_NAME = new BizCode("0-051001", "base.user-group-name-missing","用户组名称为空");
    public static final ABizCode GROUP_MISS_ID = new BizCode("0-051002", "base.user-group-id-missing","用户组id为空");

    /**
     * 租户模块错误编码，模块编码为052
     */
    public static final ABizCode  TENANT_MISS_ID= new BizCode("0-052001", "base.tenant-id-missing","租户id为空");
    public static final ABizCode  TENANT_EXIST= new BizCode("0-052001", "base.tenant-already-exist","租户已存在");
    public static final ABizCode  TENANT_UNEXIST= new BizCode("0-052001", "base.tenant-none-exist","租户不存在");

    /**
     * 附件模块，模块编码为053
     */
    public static final ABizCode ATTACH_FILE_CHUNKED = new BizCode("0-053000", "base.file-segment-uploaded","文件分片上传完成");
    public static final ABizCode ATTACH_MISS_FILE = new BizCode("0-053001", "base.upload-file-empty","上传文件为空");
    public static final ABizCode ATTACH_MIME_TYPE_EXCLUDE = new BizCode("0-053002", "base.unsupported-file-type","上传文件的类型不支持");
    public static final ABizCode ATTACH_MISS_REALLY_NAME = new BizCode("0-053003", "base.file-name-missing","缺少上传文件名");
    public static final ABizCode ATTACH_MISS_ID = new BizCode("0-053004", "base.file-id-missing","缺少附件id");
    public static final ABizCode ATTACH_MORE_THAN_SETNUM = new BizCode("0-053005", "base.file-quantity-overflow","附件数量超过配置大小");
}
