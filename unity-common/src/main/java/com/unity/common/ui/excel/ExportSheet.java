package com.unity.common.ui.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.unity.common.base.IBaseEntity;
import com.unity.common.util.FieldConvert;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class ExportSheet<T extends IBaseEntity> {
    private List<ExcelExportEntity> columns;
    private String name;
    private List<?> entities;
    private ExportBase exportEntity;

    public final <T extends IBaseEntity> ExportSheet<T> sheet(){
        return exportEntity.sheet();
    }

    public static <T extends IBaseEntity> ExportSheet<T> newInstance(){
        return new ExportSheet();
    }

    public ExportSheet() {
        this.columns = new ArrayList<ExcelExportEntity>();
    }

    public final <T extends IBaseEntity> ExportSheet<T> name(String name){
        this.name = name;
        return (ExportSheet<T>)this;
    }

    public final <T extends IBaseEntity> ExportSheet<T> entities(List<Map<String,Object>> entities){
        this.entities = entities;
        return (ExportSheet<T>)this;
    }


    public final ExportSheet<T> init() {
        return this;
    }

//    public final <T extends IBaseEntity> ExportSheet<T> addColumn(SFunction<T,?> column, String title, int width){
//        return this.column(column,title,width);
//    }
//    public final <T extends IBaseEntity> ExportSheet<T> addColumn(SFunction<T,?> column, String title){
//        return this.column(column,title);
//    }

    public final <T extends IBaseEntity> ExportSheet<T> column(SFunction<T, ?> column, String title, int width) {
        SerializedLambda serializedLambda = LambdaUtils.resolve(column);
        return column(FieldConvert.getToField(serializedLambda.getImplMethodName()), title, width);
    }

    public final <T extends IBaseEntity> ExportSheet<T> column(SFunction<T, ?> column, String title) {
        return column(column, title, 20);
    }

    public final <T extends IBaseEntity> ExportSheet<T> column(String column, String title, int width) {
        column(new ExcelExportEntity(title, column, width));
        return (ExportSheet<T>)this;
    }

    public final <T extends IBaseEntity> ExportSheet<T> column(String column, String title) {
        column(column, title, 20);
        return (ExportSheet<T>)this;
    }

    public final <T extends IBaseEntity> ExportSheet<T> column(ExcelExportEntity column) {
        this.columns.add(column);
        return (ExportSheet<T>)this;
    }

    public final <T extends IBaseEntity> ExportSheet<T> addAll(ExcelExportEntity... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return (ExportSheet<T>)this;
    }

    public void export(String name, List<?> entities) throws IOException {
        this.setEntities(entities);
        exportEntity.export(name);
    }

    public final void export(String name) throws IOException {
        exportEntity.export(name);
    }
}
