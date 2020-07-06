
package com.unity.common.base.config.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据源切面基类，子类需实现allService()方法即可完成切面。
 *
 */
public abstract class BaseDataSourceAspect {
    private static Logger logger = LoggerFactory.getLogger(BaseDataSourceAspect.class);
    /**
     * mybatis plus 通用方法切点
     */
    @Pointcut("execution(* com.baomidou.mybatisplus.extension.service..*.*(..))")
    public void superService() {
    }

    public abstract void allService();


    @Before("allService() or superService()")
    public void setReadDataSourceType(JoinPoint point) {
        String method = point.getSignature().getName();
        if (method.matches("^(select|read|find|get|list).*")) {
            DatabaseContextHolder.setDatabaseType(DatabaseType.read.getValue());
            logger.debug(">>>>>>>>{} 方法使用的数据源为:{}", method, DatabaseType.read.getValue());
        } else {
            DatabaseContextHolder.setDatabaseType(DatabaseType.write.getValue());
            logger.debug(">>>>>>>>{} 方法使用的数据源为:{}", method, DatabaseType.write.getValue());
        }
    }
}
