package com.unity.common.util;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.unity.common.base.IBaseEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldConvert {

    private static final Map<String, String> LAMBDA_CACHE = new ConcurrentHashMap<>();



    public static <T extends IBaseEntity> String getColumn(Class<T> clazz, String fieldName) {

//        Map<String, String> columnMap = LambdaUtils.getColumnMap(clazz.getName());
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(clazz.getName());

        return columnMap.get(fieldName).getColumn();
    }

    public static <T extends IBaseEntity>  String getToField(SFunction<T, ?> column){
        SerializedLambda lambda = LambdaUtils.resolve(column);
        return getToField(lambda.getImplMethodName());
    }

    public static <T extends IBaseEntity> String getToField(String methodName){
        return StringUtils.resolveFieldName(methodName);
    }

    public static <T extends IBaseEntity> String getToTableField(Class<T> clazz, SFunction<T, ?> column){
        SerializedLambda serializedLambda = LambdaUtils.resolve(column);
        TableInfo tableInfo =  TableInfoHelper.getTableInfo(clazz);
        String key = String.format("%s::%s", serializedLambda.getImplClass().getSimpleName(),
                serializedLambda.getImplMethodName());
        if(!LAMBDA_CACHE.containsKey(key)){
            String n = getToField(serializedLambda.getImplMethodName());
            TableFieldInfo field = tableInfo.getFieldList().stream().filter(f->f.getProperty().equals(n)).findAny().orElse(null);
            LAMBDA_CACHE.put(key,field.getColumn());
        }
        return LAMBDA_CACHE.get(key);
    }


}
