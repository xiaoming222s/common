package com.unity.common.base.config.datasource;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.unity.common.exception.UnityLoginException;
import com.unity.common.pojos.AuthUser;
import com.unity.springboot.support.holder.LoginContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        Object createTime = getFieldValByName("gmtCreate", metaObject);
        Object updateTime = getFieldValByName("gmtModified", metaObject);
        Object sort = getFieldValByName("sort", metaObject);
        Object creator = getFieldValByName("creator", metaObject);
        Object editor = getFieldValByName("editor", metaObject);
        if (createTime == null) {
            setFieldValByName("gmtCreate", System.currentTimeMillis(), metaObject);//mybatis-plus版本2.0.9+
        }
        if (updateTime == null) {
            setFieldValByName("gmtModified",System.currentTimeMillis(), metaObject);//mybatis-plus版本2.0.9+
        }
        if (sort == null) {
            setFieldValByName("sort",System.currentTimeMillis(), metaObject);
        }

//        System.out.println("insertFill=========token======"
//                + SessionHolder.getToken());
        AuthUser customer = null;
        try {
            customer = LoginContextHolder.getRequestAttributes();
        } catch (Exception e){

        }
        if(customer!=null){
            if (creator == null) {
                setFieldValByName("creator",customer.getId()+"."+customer.getName(), metaObject);
            }
            if (editor == null) {
                setFieldValByName("editor",customer.getId()+"."+customer.getName(), metaObject);
            }
        }
        else{
//            throw new UnityLoginException();
            if (creator == null)
                setFieldValByName("creator","1.管理员", metaObject);
            if (editor == null)
                setFieldValByName("editor","1.管理员", metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName("gmtModified", metaObject);
        Object editor = getFieldValByName("editor", metaObject);
        boolean useUpdateFill = (boolean) getFieldValByName("useUpdateFill", metaObject);

//        System.out.println("updateFill=========token======"
//                + SessionHolder.getToken());
//        if (updateTime == null) {
//            setFieldValByName("gmtModified", System.currentTimeMillis(), metaObject);//mybatis-plus版本2.0.9+
//        }

        if (useUpdateFill) {
            setFieldValByName("gmtModified", System.currentTimeMillis(), metaObject);//mybatis-plus版本2.0.9+
            AuthUser customer = null;
            try {
                customer = LoginContextHolder.getRequestAttributes();
            } catch (Exception e){

            }
            if (customer != null) {
                setFieldValByName("editor", customer.getId() + "." + customer.getName(), metaObject);
            } else {
                setFieldValByName("editor", "1.管理员", metaObject);
    //            throw new UnityLoginException();
            }
        }

    }
}