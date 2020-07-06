package com.unity.common.ui.excel;

import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerBorderImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

public class ExcelExportStyler extends ExcelExportStylerBorderImpl implements IExcelExportStyler {
    public ExcelExportStyler(Workbook workbook) {
        super(workbook);
    }

    @Override
    public CellStyle getHeaderStyle(short color) {
        CellStyle style = super.getHeaderStyle(color);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex()); // 填充的背景颜色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND); // 填充图案
        return style;
    }

    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle style = super.getTitleStyle(color);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // 填充的背景颜色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
