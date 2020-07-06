package com.unity.common.base.config.rabbit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * yml文件属性读取工具类
 * <p>
 * create by gengjiajia at 2018年06月11日14:41:11
 */
@Data
@Component
@ConfigurationProperties(prefix = "mq.config.queue")
public class AmqpYmlConfig {

    /**rbac队列名称*/
    public String rbac;
    /**cms队列名称*/
    public String cms;
}
