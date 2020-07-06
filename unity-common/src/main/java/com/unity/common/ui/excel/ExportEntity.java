package com.unity.common.ui.excel;

import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.google.common.collect.Lists;
import com.unity.common.base.IBaseEntity;
import com.unity.common.util.FieldConvert;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class ExportEntity<T extends IBaseEntity> extends ExportBase {



    private List<ExportSheet<? extends IBaseEntity>> sheets = Lists.newArrayList();

    public ExportEntity(){
    }

    public ExportEntity(HttpServletResponse res){
        super(res);
    }
//    public final  ExportEntity<T> init(HttpServletResponse res){
//        this.res = res;
//        return this;
//    }

    public final ExportEntity<T> template(TemplateExportParams params){
        this.params = params;
        return this;
    }

    private void initSheet(){
        if(this.getSheets().isEmpty()){
            this.sheet();
        }
    }


    public final ExportEntity<T> addName(String name){
        getSheetLast().setName(name);
        return this;
    }

    public final ExportEntity<T> addEntity(List<?> entities){
        getSheetLast().setEntities(entities);
        return this;
    }

    public final ExportEntity<T> addColumn(SFunction<T,?> column, String title, int width){
        SerializedLambda serializedLambda = LambdaUtils.resolve(column);
        this.addColumn(FieldConvert.getToField(serializedLambda.getImplMethodName()),title,width);
        return this;
    }
    public final ExportEntity<T> addColumn(SFunction<T,?> column, String title){
        this.addColumn(column,title,20);
        return this;
    }
    public final ExportEntity<T> addColumn(String column, String title, int width){

        this.initSheet();
        getSheetLast().column(new ExcelExportEntity(title,column,width));

        return this;
    }
    public final ExportEntity<T> addColumn(String column, String title){
        this.addColumn(column,title,20);
        return this;
    }
    public final ExportEntity<T> addColumn(ExcelExportEntity column){
        getSheetLast().getColumns().add(column);
        return this;
    }

    public final ExportEntity<T> addAll(ExcelExportEntity... columns){
        getSheetLast().getColumns().addAll(Arrays.asList(columns));
        return this;
    }

}
