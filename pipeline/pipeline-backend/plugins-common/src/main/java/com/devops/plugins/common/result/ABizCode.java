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

import java.io.Serializable;

public class ABizCode implements Serializable {
    public static final String LOCALE_BASE_PREFIX="base.";
    private static final long serialVersionUID = -2893315045048435214L;
    private String code;
    private String i18nKey;
    private String msg;
    private Throwable throwable;

    public ABizCode() {
    }

    public ABizCode(String code) {
        this.code = code;
    }

    public ABizCode(String code, String msg) {
        this.code = code;
        this.i18nKey=msg;
        this.msg = msg;
    }

    public ABizCode(String code,String i18nKey, String msg) {
        this.code = code;
        this.i18nKey=i18nKey;
        this.msg = msg;
    }

    public ABizCode(String code,String i18nKey, String msg, Throwable throwable) {
        this.code = code;
        this.i18nKey=i18nKey;
        this.msg = msg;
        this.throwable = throwable;
    }

    //未定义错误，-2，private，外部不能调用此错误代码，系统内部使用，初始化时需要这个错误代码
    public static final ABizCode OK = new ABizCode("0", "base.operate-successful","操作成功");
    public static final ABizCode FAIL = new ABizCode("0-001", "base.operate-failed","操作失败");
    public static final ABizCode UNDEFINED = new ABizCode("0-002", "base.unknown-error","未知错误");
    public static final ABizCode IS_NULL = new ABizCode("0-003", "base.object-is-null","对象为空");
    public static final ABizCode MISS_ID = new ABizCode("0-004", "base.primary-key-missing","传入主键为空");
    public static final ABizCode UNEXIST = new ABizCode("0-005", "base.data-none-exist","数据不存在");
    public static final ABizCode EXIST = new ABizCode("0-006", "base.data-exist","数据已存在");
    public static final ABizCode EXCEPTION = new ABizCode("0-007", "base.system-error","系统异常，请稍后重试");
    public static final ABizCode AUTH_INFO_MISS = new ABizCode("0-008", "base.data-valid-failed","数据校验失败");
    public static final ABizCode STATUS_404 = new ABizCode("0-009", "base.not-found","404 接口资源未找到");
    public static final ABizCode FALLBACK = new ABizCode("0-010", "base.fallback","远程调用异常，服务降级处理");
    public static final ABizCode RECODE = new ABizCode("0-011", "base.code-exist","编码已存在");
    public static final ABizCode INVAILD_AUTHORIZATION = new ABizCode("0-012", "base.illegal-auth-request","认证失败，非法请求");
    public static final ABizCode INVALID_CRON = new ABizCode("0-013", "base.invalid-cron","无效的cron表达式");
    public static final ABizCode FILE_ERROR = new ABizCode("0-014", "base.file-error","文件格式错误，请检查");
    public static final ABizCode INVALID_PARAM=new ABizCode("0-015","base.parameter-check-failed","参数校验失败");
    public static final ABizCode TOKEN_IS_MISS=new ABizCode("0-016","base.token-is-missing","非法请求，token为空");
    public static final ABizCode VISIT_LIMIT = new ABizCode("0-017", "base.visit-too-much","访问人数过多，请稍后重试");
    public static final ABizCode HOT_PARAMS_LIMIT= new ABizCode("0-018", "base.hot-params-limit","热点参数限流，请稍后重试");
    public static final ABizCode SYSTEM_RULE_LIMIT= new ABizCode("0-019", "base.system-rule-limit","系统规则限流，请稍后重试");
    public static final ABizCode AUTH_RULE_LIMIT= new ABizCode("0-020", "base.auth-rule-limit","认证规则限流，请稍后重试");
    public static final ABizCode PARAMS_IS_NULL= new ABizCode("0-020", "base.parameter-is-null","参数为空");
    public static final ABizCode ILLEGAL_PARAMETER_VALUE=new ABizCode("0-021","base.illegal-param-value","参数取值不合法");
    public static final ABizCode ILLEGAL_PARAMETER_FORMAT=new ABizCode("0-022","base.illegal-param-format","参数格式不合法");

    /** ------------------------登录阶段---------------------------------------- */
    public static final ABizCode AUTH_ERROR = new ABizCode("4-000", "base.auth-failed","认证失败");
    public static final ABizCode USERNAME_PASSWORD_ERROR = new ABizCode("4-001", "base.account-or-pwd-incorrect","帐号密码错误");
    public static final ABizCode CAPTCHA_CODE_ERROR = new ABizCode("4-002", "base.captcha-incorrect","验证码错误");
    /** ------------------------网关拦截api请求，判断认证状态阶段---------------------------------------- */
    public static final ABizCode AUTHORIZATION_TOKEN_OFFLINE = new ABizCode("4-010", "base.auth-token-offline","帐号已登出");
    public static final ABizCode AUTHORIZATION_TOKEN_EXPIRE = new ABizCode("4-011", "base.auth-token-expired","认证token已过期，请刷新");
    public static final ABizCode AUTHORIZATION_TOKEN_INVALID = new ABizCode("4-012", "base.invalid-auth-token","认证token无效");
    public static final ABizCode REFRESH_TOKEN_EXPIRE = new ABizCode("4-013", "base.refresh-token-expired","刷新token已过期");
    public static final ABizCode REFRESH_TOKEN_INVALID = new ABizCode("4-014", "base.invalid-refresh-token","刷新token无效");

    public static final ABizCode UNAUTHORIZED_PROTOCOL_TYPE_ERROR = new ABizCode("4-020", "base.invalid-protocol-type","protocolType无效");
    public static final ABizCode UNAUTHORIZED_GRANT_TYPE_ERROR = new ABizCode("4-021", "base.invalid-grant-type","grantType无效");
    public static final ABizCode AUTHORIZATION_ACCESS_KEY_INVALID = new ABizCode("4-022", "base.invalid-auth-access-key","AccessKey无效");
    public static final ABizCode AUTHORIZATION_SIGNATURE_EXPIRE = new ABizCode("4-023", "base.expire-auth-signature","认证签名已过期");
    public static final ABizCode AUTHORIZATION_SIGNATURE_INVALID = new ABizCode("4-024", "base.invalid-auth-signature","认证签名无效");
    /** ------------------------网关拦截api请求，判断权限阶段---------------------------------------- */
    public static final ABizCode PERMISSION_DENIED = new ABizCode("4-030", "base.permission-denied","没有权限,拒绝访问");
    public static final ABizCode USER_DISABLE = new ABizCode("4-031", "base.account-is-forbidden","账户已禁用，请联系管理员");
    public static final ABizCode APP_DISABLE = new ABizCode("4-032", "base.app-is-forbidden","应用已禁用，请联系管理员");
    public static final ABizCode TENANT_DISABLE = new ABizCode("4-033", "base.tenant-is-forbidden","租户已禁用，请联系管理员");

    public static final ABizCode SENTINEL_FLOW_LIMIT = new ABizCode("5-429", "base.service-busy","服务器繁忙，请稍后重试");
    public static final ABizCode SENTINEL_DEGRADE_LIMIT = new ABizCode("5-430", "base.service-degrade","服务熔断，请稍后重试");
    public static final ABizCode SENTINEL_SYSTEM_LIMIT = new ABizCode("5-431", "base.system-protection","系统负载保护，请稍后重试");
    public static final ABizCode SENTINEL_OTHER_LIMIT = new ABizCode("5-432", "base.gateway-error","网关异常，请稍后重试");

    /**
     * <b>Summary: 自定义状态描述，前提是这个状态对象已存在了</b>
     * setCode()
     *
     * @param resultCode
     * @param msg
     * @return
     */
    public static ABizCode getCode(ABizCode resultCode, String msg) {
        if (resultCode != null) {
            ABizCode rc = new ABizCode(resultCode.code, msg);
            resultCode = rc;
        } else {
            resultCode = ABizCode.UNDEFINED;
        }
        return resultCode;
    }

    /**
     * <b>Summary: 获取异常信息code对象</b>
     * getExceptionCode()
     *
     * @param e
     * @return
     */
    public static ABizCode getExceptionCode(Throwable e) {
        String errorMsg= LocaleUtils.get(ABizCode.EXCEPTION.getI18nKey());
        if(errorMsg.startsWith(LOCALE_BASE_PREFIX)){
            errorMsg=ABizCode.EXCEPTION.getMsg();
        }
        StringBuilder msg = new StringBuilder(errorMsg);
        StackTraceElement[] messages = e.getStackTrace();
        int length = messages.length;
        if (e.getCause() != null) {
            String message = e.getCause().getMessage();
            if (message != null) {
                message = message.replace("\"", "").replace("\'", "");
            }
            msg.append("cause:" + e.getCause().getClass() + "," + message);
        } else {
            for (int i = 0; i < (length > 1 ? 1 : length); i++) {
                if (i == 0) {
                    msg.setLength(0);
                }
                msg.append((" cause: " + e.getClass().getName() + "  " + e.getMessage()) + "\\n");
                msg.append(" detail: " + messages[i].toString() + "\\n ");
            }
        }
        return getCode(ABizCode.EXCEPTION, msg.toString());
    }

    /**
     * <b>Summary: 得到状态码</b>
     * getCode()
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * <b>Summary: 得到状态描述</b>
     * getMsg()
     *
     * @return
     */
    public String getMsg() {
        return msg;
    }

    /**
     * <b>Summary: 得到i18n的key</b>
     * getI18nKey()
     *
     * @return
     */
    public String getI18nKey(){
        return i18nKey;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((msg == null) ? 0 : msg.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ABizCode other = (ABizCode) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        if (msg == null) {
            return other.msg == null;
        } else {
            return msg.equals(other.msg);
        }
    }

    @Override
    public String toString() {
        return "[code:" + code + ",msg:" + msg+"]";
    }
}
