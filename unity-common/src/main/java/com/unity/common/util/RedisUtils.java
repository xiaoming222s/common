package com.unity.common.util;

import com.unity.common.constants.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 * create at 2017年12月14日20:44:25
 */
@Component
@Slf4j
public class RedisUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    //当前用户登录的标识(Token)
    private static final String CURRENT_USER_TOKEN = "current_token";
    private static final String USER_FLAG = "user";


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 给valueOperation设置值并且设置有效期，只有当获取到的obj不存在的时候才放入
     *
     * @param key      键
     * @param value    值
     * @param validate 有效期，单位为秒
     * @author Jung
     * @since 2017年12月14日21:09:31
     */
    public synchronized void valueOperationSet(String key, Object value, Long validate) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
        if (validate != null) {
            valueOperations.getOperations().expire(key, validate, TimeUnit.SECONDS);
        }
    }

    /**
     * 放入当前的用户(根据token识别用户)
     *
     * @param token    用户的token
     * @param authUser 登录的用户
     * @author Jung
     * @since 2018年02月18日11:49:10
     */
    @Async
    public <T> void putCurrentUserByToken(String token, T authUser,Integer day) {
        valueOperationSet(RedisKeys.CUSTOMER.prefix(CURRENT_USER_TOKEN).going(token), authUser, 3600L * 24 * day);
    }

    public synchronized <T> void putUser(int id, T user) {
        valueOperationSet(RedisKeys.CUSTOMER.prefix(USER_FLAG).done(String.valueOf(id)), user, Long.MAX_VALUE);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T getUser(int id){
        return (T) redisTemplate.opsForValue().get(RedisKeys.CUSTOMER.prefix(USER_FLAG).done(String.valueOf(id)));
    }

    /**
     * 移除当前的用户（根据token识别用户）
     *
     * @param token 用户的token
     * @author Jung
     * @since 2018-02-19 22:32:38
     */
    public void removeCurrentUserByToken(String token) {
        redisTemplate.delete(RedisKeys.CUSTOMER.prefix(CURRENT_USER_TOKEN).going(token));
    }


    /**
     * 获得当前的用户，请注意，如果直接通过redis操作，或者或者没有通过本工具类进行直接操作，那么将可能导致得到的用户实体触发类转换异常
     *
     * @param token 需要获得的token
     * @return 得到的用户，如果没有登录，或者类转换异常，则返回null
     * @author Jung
     * @since 2018年02月18日11:54:17
     */
    public <T> T getCurrentUserByToken(String token) {
        log.info("RedisKeys================"+RedisKeys.CUSTOMER.prefix(CURRENT_USER_TOKEN).going(token));
        return commonGetCurrentUser(RedisKeys.CUSTOMER.prefix(CURRENT_USER_TOKEN).going(token));
    }

    /**
     * 获得所有在redis中的用户（根据token识别）
     *
     * @return 拿到的列表
     * @author Jung
     * @since 2018-02-20 15:54:52
     */
//    public List<UserInfo> getAllCurrentUserByToken() {
//        return multiValueGet(XyRedisKeys.USER.prefix(CURRENT_USER_TOKEN).going(XyStrings.ASTERISK));
//    }

    /**
     * 公共-获取用户的工具类
     *
     * @param key 需要拿的key
     * @return 得到的用户
     * @author Jung
     * @since 2018年02月18日18:12:22
     */
    @SuppressWarnings("unchecked")
    private <T> T commonGetCurrentUser(String key) {
        try {
            T obj = (T) redisTemplate.opsForValue().get(key);
            if (obj != null) {
                redisTemplate.expire(key, 3600 * 24 * 7L, TimeUnit.SECONDS);
            }
            return obj;
        } catch (ClassCastException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Attention! Redis current user is NOT A DISTANCE of AdminUser, Please Check, Current key:{}", key);
            }
            return null;
        }
    }

    public <T> T commonGetValue(String key) {
        try {
            T obj = (T) redisTemplate.opsForValue().get(key);
            return obj;
        } catch (ClassCastException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Please Check, Current key:{}", key);
            }
            return null;
        }
    }

    /**
     * ValueOperation批量获得，注意，如果类型不对，会抛出异常
     *
     * @param pattern 需要get的模式
     * @param <T>     返回值类型
     * @return 拿到的值
     * @author Jung
     * @since 2018-02-20 15:52:32
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> multiValueGet(String pattern) {
        //获取所有的key
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null) {
            return new ArrayList<>();
        }
        //构造返回值
        List<T> list = new ArrayList<>();
        keys.parallelStream().forEach(key -> list.add((T) redisTemplate.opsForValue().get(key)));
        return list;
    }
    
    /**
     *  放入当前的验证码CODE
     * 
     * @param: code 验证码CODE  IP地址 ipAddr
     * @return: T
     * @auther: jiaww
     * @since: 2018/10/6 15:31
     */
    @Async
    public <T> void putCurrentVerifyCode(String code, String ipAddr) {
        long time = 30*60*1000;
        valueOperationSet(code, ipAddr, time);
    }

    /**
     *  获取到当前的验证码CODE
     *
     * @param: code
     * @return: T
     * @auther: jiaww
     * @since: 2018/10/6 15:31
     */
    public <T> T getCurrentVerifyCodeByCode(String code) {
        return commonGetCurrentUser(code);
    }

    /**
     *  移除当前的验证码CODE
     *
     * @param: code
     * @auther: jiaww
     * @since: 2018/10/6 15:31
     */
    public void removeCurrentVerifyCodeByCode(String code) {
        redisTemplate.delete(code);
    }

}
