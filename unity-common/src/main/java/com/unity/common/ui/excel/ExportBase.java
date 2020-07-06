package com.unity.common.ui.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import com.google.common.collect.Lists;
import com.unity.common.base.IBaseEntity;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.util.Encodes;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class ExportBase {

    private HttpServletResponse res;
    protected TemplateExportParams params;
    private List<ExportSheet<? extends IBaseEntity>> sheets = Lists.newArrayList();

    public ExportBase(HttpServletResponse res){
        this.res=res;
    }

    public List<ExportSheet<? extends IBaseEntity>> getSheets(){
        return this.sheets;
    }

    protected ExportSheet<? extends IBaseEntity> getSheetLast(){
        return getSheets().get(getSheets().size()-1);
    }


    public final <J extends IBaseEntity> ExportSheet<J> sheet(){
        ExportSheet<J> sheet = new ExportSheet<J>();
        sheet.setExportEntity(this);
        sheets.add(sheet);
        return (ExportSheet<J>)sheet;
    }

    public void export(String name, List<?> entities) throws IOException {


        if(this.params==null){
            //响应头信息
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/ms-excel; charset=UTF-8");
            res.setHeader("Content-disposition", "attachment; filename=" +  Encodes.urlEncode(name) + ".xls");
            Workbook workbook = null;
            ExportParams parms = new ExportParams(name, name);
            parms.setStyle(ExcelExportStyler.class);
            List<ExcelExportEntity> entityList = this.sheets.get(0).getColumns();
            workbook = ExcelExportUtil.exportExcel(parms, entityList, entities);
            workbook.write(res.getOutputStream());
        }
        else{
            throw UnityRuntimeException.newInstance().message("参数不正确").build();
        }
//        FileOutputStream output=new FileOutputStream("d:\\workbook.xls");
//        workbook.write(output);
//        output.flush();
//        output.close();

    }

    public void export(String name) throws IOException {

        if(this.sheets.size()>0){
            //响应头信息
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/ms-excel; charset=UTF-8");
            res.setHeader("Content-disposition", "attachment; filename=" +  Encodes.urlEncode(name) + ".xls");
            Workbook workbook =null;

            for(ExportSheet sheet:this.sheets){
                ExportParams parms = new ExportParams(sheet.getName(), sheet.getName());
                parms.setStyle(ExcelExportStyler.class);
                if(workbook==null) {
                    workbook = getWorkbook(parms.getType(),sheet.getEntities().size());
                }

                exportExcel(workbook, parms, sheet.getColumns(), sheet.getEntities());

            };

            workbook.write(res.getOutputStream());
        }
        else{
            throw UnityRuntimeException.newInstance().message("至少导出一个Sheet").build();
        }
    }

    private static Workbook getWorkbook(ExcelType type, int size) {
        if (ExcelType.HSSF.equals(type)) {
            return new HSSFWorkbook();
        } else if (size < 100000) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }

    public static Workbook exportExcel(Workbook workbook,ExportParams entity, List<ExcelExportEntity> entityList,
                                       Collection<?> dataSet) {
        //Workbook workbook = getWorkbook(entity.getType(),dataSet.size());
        new ExcelExportService().createSheetForMap(workbook, entity, entityList, dataSet);
        return workbook;
    }

    public void export(String name, Map m) throws IOException {

        if(this.params==null){
            throw UnityRuntimeException.newInstance().message("参数不正确").build();
        }
        else{
            //响应头信息
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/ms-excel; charset=UTF-8");
//            res.setHeader("Content-disposition", "attachment; filename=" +  Encodes.urlEncode(name) + ".xls");
            res.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("gb2312"), "ISO-8859-1") + ".xls");

            Workbook workbook = null;
            workbook = ExcelExportUtil.exportExcel(this.params,m);
            workbook.write(res.getOutputStream());
        }
    }


    public void exportError(String name,Exception ex){

        try {
            name =name+"*导出错误*";
            //响应头信息
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/ms-excel; charset=UTF-8");
            //res.setHeader("Content-disposition", "attachment; filename=" +  Encodes.urlEncode(name) + ".xls");
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
