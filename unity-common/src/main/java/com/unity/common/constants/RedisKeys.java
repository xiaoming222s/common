package com.unity.common.constants;


import com.unity.common.util.XyStrings;

/**
 * redis key，通常此处的枚举会作为模块的名称使用
 * create at 2017年12月14日17:48:49
 */
public enum RedisKeys {
    CUSTOMER,
    CUSTOMER_SERVICE,
    PROPERTY_REGULATE,;

    /**
     * 获取namespace
     *
     * @param methodName 需要拼接的后缀,最好是方法名
     * @return 拼接后的字符串
     * @author Jung
     * @since 2017年12月14日17:53:34
     */
    public RedisNameBuilder prefix(String methodName) {
        return RedisNameBuilder.newInstance(name().toLowerCase() + XyStrings.COLON + methodName);
    }
}

/**
 * key的namespace后缀
 * create at 2017年12月14日17:48:49
 */
enum NamespaceSuffix {
    //未开始
    PREPARED,
    //进行中
    GOING,
    //已结束
    DONE;
}
