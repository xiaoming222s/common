
package com.unity.common.base.config.interceptor;




import com.unity.common.util.Reflections;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Properties;
import java.util.stream.Stream;


/**
 * 过滤is_deleted=0的记录，仅针对mp通用crud方法。
 * <p>
 * 当有多个拦截器时，优先级如何，并未深入研究，测试发现此拦截器在分页拦截器前执行，因此无需考虑在sql的limit
 * offset前添加is_deleted=0 的条件。但是最好做出判断，此拦截器暂不判断。解决办法：
 * <li>1.调整拦截器优先级，确保此拦截器在分页拦截器前执行</li>
 * <li>2.解析sql，如果有limit offset，则在其前加上is_deleted=0条件</li> <br>
 * 注意,以上解决方法此类<b><i> 均无 </i></b>实现 :-)
 * </p>
 * 
 *
 */
@Intercepts({
        @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class, Integer.class }) })
public class DeletedFilterInterceptor implements Interceptor {

    private String dialectType;

    private String[] targetMethods = {};

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler) realTarget(invocation.getTarget());
        StatementHandler delegate = (StatementHandler) Reflections.getFieldValue(handler, "delegate");
        BoundSql boundSql = delegate.getBoundSql();
        MappedStatement mappedStatement = (MappedStatement) Reflections.getFieldValue(delegate, "mappedStatement");
        // 获取方法签名
        String method = mappedStatement.getId();
        String simpleMethod = method = method.substring(method.lastIndexOf(".") + 1, method.length());
        // 仅mp部分通用方法需处理
        if (Stream.of(targetMethods).filter(m -> m.equals(simpleMethod)).findAny().isPresent()) {
            // 获取当前要执行的Sql语句
            String sql = boundSql.getSql();
            sql = addDeletedFilterContidion(sql);
            Reflections.setFieldValue(boundSql, "sql", sql);
        }

        return invocation.proceed();
    }

    private String addDeletedFilterContidion(String sql) {
        boolean hasWhere = sql.contains("WHERE");
//        sql += getConnector(hasWhere) + "is_deleted=0 ";
//        sql += getConnector(hasWhere);
        return sql;
    }

    private String getConnector(boolean hasWhere) {
        if (hasWhere) {
            return " AND ";
        } else {
            return " WHERE ";
        }
    }

    /**
     * 获取RoutingStatementHandler
     * 
     * @param target
     * @return
     */
    private Object realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject mo = SystemMetaObject.forObject(target);
            return realTarget(mo.getValue("h.target"));
        } else {
            return target;
        }
    }

    /**
     * 拦截器对应的封装原始对象的方法
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置注册拦截器时设定的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

    public String getDialectType() {
        return dialectType;
    }

    public void setDialectType(String dialectType) {
        this.dialectType = dialectType;
    }

    public String[] getTargetMethods() {
        return targetMethods;
    }

    public void setTargetMethods(String[] targetMethods) {
        this.targetMethods = targetMethods;
    }

}
