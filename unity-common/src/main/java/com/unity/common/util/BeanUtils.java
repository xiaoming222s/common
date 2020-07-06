package com.unity.common.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * 实体类工具类
 * <p>
 * create by qinhuan at 2018/9/4 13:53
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 获得给定实体中非空的属性
     *
     * @param
     * @return
     * @author qinhuan
     * @since 2018/11/22 7:10 PM
     */
    public static String[] getNoNullProperties(Object target) {
        BeanWrapper srcBean = new BeanWrapperImpl(target);
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        Set<String> noEmptyName = new HashSet<>();
        for (PropertyDescriptor p : pds) {
            Object value = srcBean.getPropertyValue(p.getName());
            if (value != null) noEmptyName.add(p.getName());
        }
        String[] result = new String[noEmptyName.size()];
        return noEmptyName.toArray(result);
    }

}
