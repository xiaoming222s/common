package com.unity.common.ui.excel;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelImportEntity;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.afterturn.easypoi.util.PoiReflectorUtil;
import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.google.common.collect.Maps;
import com.unity.common.base.IBaseEntity;
import com.unity.common.base.SessionHolder;
import com.unity.common.constants.ConstString;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.SystemResponse;
import com.unity.common.util.FieldConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class ImportEntity<T extends IBaseEntity> {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private Map<String, ExcelImportEntity> columns;
    private HttpServletResponse res;
    private Class<?> pojoClass;
    private IExcelVerifyHandler verifyHandler;

    public ImportEntity(){
        this.columns = Maps.newHashMap();
    }
    public ImportEntity(HttpServletResponse res,Class<T> pojoClass){
        this.res = res;
        this.pojoClass = pojoClass;
        this.columns = Maps.newHashMap();
    }
    public ImportEntity(HttpServletResponse res,Class<T> pojoClass,RedisTemplate<String, Object> redisTemplate){
        this.res = res;
        this.redisTemplate = redisTemplate;
        this.pojoClass = pojoClass;
        this.columns = Maps.newHashMap();
    }
    public final ImportEntity<T> init(HttpServletResponse res, Class<T> pojoClass){
        this.res = res;
        this.pojoClass = pojoClass;
        return this;
    }

    public final ImportEntity<T> verifyHandler(IExcelVerifyHandler<T> verifyHandler){
        this.verifyHandler=verifyHandler;
        return this;
    }

    public final ImportEntity<T> addColumn(SFunction<T,?> column, String title, boolean importField){
        SerializedLambda serializedLambda = LambdaUtils.resolve(column);
        return addColumn(FieldConvert.getToField(serializedLambda.getImplMethodName()),title,importField);
    }

    public final ImportEntity<T> addColumn(String column, String title, boolean importField){
        ExcelImportEntity col = new ExcelImportEntity();
        col.setName(title);
        col.setImportField(importField);
        col.setMethod(PoiReflectorUtil.fromCache(pojoClass).getSetMethod(column));

        addColumn(col);
        return this;
    }

    public final ImportEntity<T> addColumn(ExcelImportEntity column){
        this.columns.put(column.getName(),column);
        return this;
    }

    public final ImportEntity<T> addAll(ExcelImportEntity... columns){
        for(ExcelImportEntity col:columns){
            addColumn(col);
        }
        return this;
    }

    public List<T> Import(InputStream inputstream){
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        params.setNeedVerfiy(true);
        params.setVerifyHandler(this.verifyHandler);
//        params.setVerfiyGroup(new Class[]{ViliGroupOne.class});

        ExcelImportResult<T> result = null;
        try{
            result =  new ExcelImport().importExcelByIs(columns, inputstream, pojoClass,
                    params, true);
        }
        catch (Exception ex){
            log.error("Excel文件格式错误",ex);
            throw UnityRuntimeException.newInstance().message("Excel文件格式错误").build();
        }

        if(result!=null){
//            try{
//                FileOutputStream fos = new FileOutputStream("D:/baseModetest.xlsx");
//                result.getWorkbook().write(fos);
//                fos.close();
//            }
//           catch (Exception ex){
//
//           }
            if(result.isVerfiyFail()){

                System.out.println("getFailList==========="+ JSON.toJSON(result.getFailList()));
                System.out.println("getList==========="+ JSON.toJSON(result.getList()));


                String key = ConstString.IMPORT_EXCEL_ERR+"_"+SessionHolder.getToken();
                try {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();

                    result.getFailWorkbook().write(os);

//                    key = UUID.randomUUID().toString();
//                    SessionHolder.getCatch().put(key,Base64.byteArrayToAltBase64(os.toByteArray()));
                    //SessionHolder.getSession().setAttribute(ConstString.IMPORT_EXCEL_ERR,Base64.byteArrayToAltBase64(os.toByteArray()));
                    redisTemplate.opsForValue().set(key,Base64.byteArrayToAltBase64(os.toByteArray()));
                    os.close();
                }
                catch (Exception ex){
                    throw new UnityRuntimeException(ex);
                }
                throw UnityRuntimeException.newInstance().message(key)
                        .code(SystemResponse.FormalErrorCode.EXCEL_VERFIY_FAIL).build();
//               throw new UnityRuntimeException(SystemResponse.FormalErrorCode.EXCEL_VERFIY_FAIL);
            }
            return result.getList();
        }
        else{
            return null;
        }

//            return ExcelImportUtil.importExcel(inputstream, pojoClass, params);

    }
}
