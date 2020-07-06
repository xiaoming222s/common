package com.unity.springboot.support;

/**
 * 系统模块的划分
 * <p>
 * create by Jung at 2018年06月16日19:01:21
 */
public final class Modules {
    public static final String customer = "supervise-customer";
    public static final String deployment = "supervise-work-deployment";
    public static final String websocket = "supervise-websocket";

    public static class Mapping{
        public static final String CUSTOMER = "/customer/";
        public static final String DEPLOYMENT = "/work-deployment/";
        public static final String WEBSOKET = "/websocket/";
    }


}
