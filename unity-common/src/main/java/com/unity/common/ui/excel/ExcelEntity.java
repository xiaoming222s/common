package com.unity.common.ui.excel;

import com.unity.common.base.IBaseEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
@Data
public class ExcelEntity<T extends IBaseEntity> {

    public static ExportEntity exportEntity(HttpServletResponse res){
        return new ExportEntity(res);
    }

    public static ImportEntity importEntity(HttpServletResponse res, Class<?> entity){
        return new ImportEntity(res,entity);
    }
    public static ImportEntity importEntity(HttpServletResponse res, Class<?> entity, RedisTemplate<String, Object> redisTemplate){
        return new ImportEntity(res,entity,redisTemplate);
    }

//    @Autowired
//    private ExportEntity<T> exportEntity;
//
//    @Autowired
//    private ImportEntity<T> importEntity;
}