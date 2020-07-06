package com.unity.common.ui.word;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.util.PoiReflectorUtil;
import cn.afterturn.easypoi.word.WordExportUtil;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.google.common.collect.Maps;
import com.unity.common.base.IBaseEntity;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.ui.excel.ExcelExportStyler;
import com.unity.common.util.FieldConvert;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * word 模板方式导出
 * @param <T> 实体泛型接口
 * @author 张云浩 曾庆超
 */
@Component
@Data
public class ExportWordEntity<T extends IBaseEntity>  {
    private Map<String, Object> params;
    /**
     * 响应
     */
    private HttpServletResponse res;
    /**
     * Word模板
     */
    private String url;


    public ExportWordEntity(){
        this.params = Maps.newHashMap();
    }
    public ExportWordEntity(HttpServletResponse res){
        this.res = res;
        this.params = Maps.newHashMap();
    }


    public ExportWordEntity(HttpServletResponse res, String url){
        this.res = res;
        this.params = Maps.newHashMap();
        this.url = url;
    }

    public ExportWordEntity(HttpServletResponse res, String url, Map params){
        this.res = res;
        this.params = params;
        this.url = url;
    }

    public static ExportWordEntity instance(HttpServletResponse res, String url){
        ExportWordEntity word = new ExportWordEntity<>();
        word.init(res,url);
        return word;
    }

    public final ExportWordEntity<T> init(HttpServletResponse res, String url){
        this.res = res;
        this.url = url;
        return this;
    }

    public final ExportWordEntity<T> addParams(SFunction<T,?> key, Object val){
        SerializedLambda serializedLambda = LambdaUtils.resolve(key);
        return addParams(FieldConvert.getToField(serializedLambda.getImplMethodName()), val);
    }

    public final ExportWordEntity<T> addParams(String key, Object val){
        this.params.put(key, val);
        return this;
    }

    public final ExportWordEntity<T> addAll(T entity, Class<?> tClass){
        if(tClass == null || entity == null){
            throw new RuntimeException("params can not be null ");
        }

        PoiReflectorUtil reflector = PoiReflectorUtil.fromCache(tClass);
        for (Field fie : reflector.getFieldList()) {
            Object valObj = reflector.getValue(entity, fie.getName());
            if(valObj != null){
                addParams(fie.getName(), valObj);
            }
        }

        return this;
    }

    public void export(String name) throws IOException {


        if(this.params==null || this.url == null){
            throw UnityRuntimeException.newInstance().message("参数不正确").build();
        }else{
            //响应头信息
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/ms-word; charset=UTF-8");
            res.addHeader("Content-Disposition", "attachment;filename=" +
                    new String(name.getBytes("gb2312"), "ISO-8859-1") + ".docx");


            XWPFDocument doc = null;
            try {
                doc = WordExportUtil.exportWord07(this.url, this.params);
            } catch (Exception e) {
                this.exportError(name, e);
            }
            doc.write(res.getOutputStream());
        }
    }

    public void exportError(String name,Exception ex){

        try {
            name =name+"*导出错误*";
            //响应头信息
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/ms-word; charset=UTF-8");
            res.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("gb2312"), "ISO-8859-1") + ".xls");

            ExportParams parms = new ExportParams(name, name);
            parms.setStyle(ExcelExportStyler.class);
            List<?> entities = Arrays.asList("msg",ex.getMessage());
            List<ExcelExportEntity> errCols =  Arrays.asList(
                    new ExcelExportEntity("错误信息", "msg", 80));
            Workbook workbook = ExcelExportUtil.exportExcel(parms, errCols, entities);

            workbook.write(res.getOutputStream());
        }
        catch (Exception e){

        }
    }
}
