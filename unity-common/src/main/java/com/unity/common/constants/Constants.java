package com.unity.common.constants;

/**
 * 系统常量池
 * Created by Jung on 23/06/2017.
 */
public abstract class Constants {
    private Constants() {

    }

    //默认用户的有效期
    public static final Integer APP_TOKEN_EXPIRE_DAY = 30;//app有效时间
    public static final Integer PC_TOKEN_EXPIRE_DAY = 1;//pc有效时间
    //短信有效期的默认时间（秒）
    public static final long SMS_VALIDATE = 60;
    //微信用户同一微信openId获取验证码一天三次以上需要获取图形验证码
    public static final long WECHAT_OPENID_VALIDATE = 24 * 3600L;
    //默认redis的锁的有效期（秒）
    public static final long REDIS_LOCK_EXPIRED = 20;
}
