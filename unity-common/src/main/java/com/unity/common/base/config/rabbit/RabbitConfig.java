package com.unity.common.base.config.rabbit;

import com.unity.common.constants.RabbitConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbit-mp队列配置
 */
@Configuration
/*@ConditionalOnProperty(prefix = "mq.config.queue", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(AmqpYmlConfig.class)*/
public class RabbitConfig {

    /*private final AmqpYmlConfig amqpYmlConfig;

    public RabbitConfig(AmqpYmlConfig amqpYmlConfig) {
        this.amqpYmlConfig = amqpYmlConfig;
    }*/

    @Bean
	public Queue rbacQueue(){
		return new Queue(RabbitConstants.RBAC_QUEUE_NAME);
	}

    @Bean
    public Queue cmsQueue(){
        return new Queue(RabbitConstants.CMS_QUEUE_NAME);
    }
}
