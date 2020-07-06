package com.unity.common.base.config.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unity.common.util.XyStrings;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationException;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * RestTempalte的配置支持,主要修改了对于序列化部分的行为.
 * Created by jung at 2017年12月14日17:25:00
 */
@Configuration
public class RedisTemplateConfiguration {

    @Resource
    private RedisProperties redisProperties;

    private RedisSerializer<Object> strSerializer = new RedisSerializer<Object>() {
        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return XyStrings.bytes(o);
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return ArrayUtils.isEmpty(bytes) ? null : new String(bytes, StandardCharsets.UTF_8);
        }
    };

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(strSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(strSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        setSerializer(template);
        return template;
    }

    private void setSerializer(RedisTemplate<?, ?> tmpl) {
        tmpl.setKeySerializer(strSerializer);
        tmpl.setValueSerializer(strSerializer);
        tmpl.setHashValueSerializer(strSerializer);
        tmpl.setHashKeySerializer(strSerializer);
    }
}