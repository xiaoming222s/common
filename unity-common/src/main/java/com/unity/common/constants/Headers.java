package com.unity.common.constants;

/**
 * Created by Jung on 23/06/2017.
 */
public abstract class Headers {
    private Headers() {

    }

    //调试用的，在开发环境下可以用这个模拟登录用户
    public static final String SESSION_UID_HEADER = "X-TAOTIME-UID";

    //版本号
    public static final String VERSION_HEADER = "X-TAOTIME-API-VERSION";

    //Rest调用的标志
    public static final String INTERNAL_CALL_HEADER = "X-TAOTIME-INTERNAL";

    public static final String APP_VERSION = "X-APP-VERSION";
    public static final String APP_OS = "X-APP-OS";
    public static final String DEVELOPER_DEBUG = "X-DEVELOPER-DEBUG";
    //TOKEN的请求头
    public static final String SESSION_TOKEN_HEADER = "X-USER-TOKEN";
    public static final String USER_ID = "X-USER-ID";
    public static final String APP_TOKEN = "X-APP-TOKEN";
    public static final String AUTH_TOKEN = "X-AUTH-TOKEN";
}
