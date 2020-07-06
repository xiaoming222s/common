package com.unity.common.base.config.interceptor;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.AbstractLogicMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

public class PhysicalDeleteByMap extends AbstractLogicMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        CustomSqlMethod sqlMethod = CustomSqlMethod.PHYSICAL_DELETE_BY_MAP;
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), this.sqlWhereByMap(tableInfo));

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Map.class);
        return addUpdateMappedStatement(mapperClass, Map.class, sqlMethod.getMethod(), sqlSource);
    }
}