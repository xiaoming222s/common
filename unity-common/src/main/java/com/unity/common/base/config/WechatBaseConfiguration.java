package com.unity.common.base.config;

import lombok.Data;

/**
 * 微信的配置基类
 * <p>
 * create by Jung at 2018-02-23 10:46:38
 */
@Data
public class WechatBaseConfiguration {
    private String appId;
    private String appSecret;
}
