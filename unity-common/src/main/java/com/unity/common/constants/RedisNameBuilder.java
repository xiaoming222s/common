package com.unity.common.constants;


import com.unity.common.util.XyStrings;

/**
 * Redis的name构造器
 */
public class RedisNameBuilder {
    private static String prefix;

    public static RedisNameBuilder newInstance(String prefix) {
        RedisNameBuilder.prefix = prefix;
        return new RedisNameBuilder();
    }

    public String prepared = prefix + XyStrings.COLON + NamespaceSuffix.PREPARED.name().toLowerCase();
    public String going = prefix + XyStrings.COLON + NamespaceSuffix.GOING.name().toLowerCase();
    public String done = prefix + XyStrings.COLON + NamespaceSuffix.DONE.name().toLowerCase();
    public String prepared(String concrete){
        return prepared+ XyStrings.COLON+concrete;
    }
    public String going(String concrete){
        return going+ XyStrings.COLON+concrete;
    }
    public String done(String concrete){
        return done+ XyStrings.COLON+concrete;
    }

}