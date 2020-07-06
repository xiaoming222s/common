package com.unity.common.base;

import java.util.HashMap;
import java.util.Map;

public class ContextHolder {
    private static final ThreadLocal<Map<String,Object>> contextHolder = new ThreadLocal<Map<String,Object>>();

    public static <T> void set(String key,T val) {
        if(contextHolder.get()==null){
            contextHolder.set(new HashMap<String,Object>());
        }
        contextHolder.get().put(key,val);
    }

    public static <T> T get(String key) {
        if(contextHolder.get()==null){
            return null;
        }
        else {
            return (T)contextHolder.get().get(key);
        }
    }

    public static Boolean containsKey(String key){
        if(contextHolder.get()==null){
            return false;
        }
        return contextHolder.get().containsKey(key);
    }

    public static void remove(String key) {
        if(contextHolder.get()!=null){
            contextHolder.get().remove(key);
        }
    }
}
