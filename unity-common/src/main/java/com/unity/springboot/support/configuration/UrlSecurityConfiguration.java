package com.unity.springboot.support.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 安全的配置，
 * <p>
 * create by Jung at 2018年02月19日23:03:25
 */
@Component
@ConfigurationProperties(prefix = "spring.security")
@Data
public class UrlSecurityConfiguration {
    /**
     * 拦截模式，默认是permit模式
     */
    private Model model = Model.permit;
    private String[] permit;
    private String[] authenticated;

    public enum Model{
        /**
         * permit模式：全局拦截模式，只有配置了permit选项之后才能解除拦截
         */
        permit,

        /**
         * authenticated模式：全局放行模式，只有配置了authenticated选项之后才能拦截
         */
        authenticated
    }
}