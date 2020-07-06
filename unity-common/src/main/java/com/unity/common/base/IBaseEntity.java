package com.unity.common.base;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.reflect.Field;

public interface IBaseEntity<T> {
     Object get(String FieldName);
//     Object get(SFunction<T, ?> column);
     void set(String FieldName, Object val);
}
