package com.qiein.jupiter.util;

import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;
/**
 * @author: yyx
 * @Date: 2018-8-29
 */
public class ExcelExportStyle extends AbstractExcelExportStyler implements IExcelExportStyler {
    public ExcelExportStyle(Workbook workbook) {
        super.createStyles(workbook);
    }

    @Override
    public CellStyle getHeaderStyle(short headerColor) {
        CellStyle titleStyle = this.workbook.createCellStyle();
        Font font = this.workbook.createFont();
        font.setFontHeightInPoints((short)24);
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(headerColor);
        titleStyle.setAlignment((short)2);
        titleStyle.setVerticalAlignment((short)1);
        return titleStyle;
    }

    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle titleStyle = this.workbook.createCellStyle();
        titleStyle.setFillForegroundColor(color);
        titleStyle.setAlignment((short)2);
        titleStyle.setVerticalAlignment((short)1);
        titleStyle.setFillPattern((short)1);
        titleStyle.setWrapText(true);
        return titleStyle;
    }

    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        //转为RGB码
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderBottom((short)1);
        style.setBorderTop((short)1);
//        style.setFillForegroundColor((short)41);
        style.setAlignment((short)2);
        style.setVerticalAlignment((short)1);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }

        return style;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft((short)1);
        style.setBorderRight((short)1);
        style.setBorderBottom((short)1);
        style.setBorderTop((short)1);
        style.setAlignment((short)2);
        style.setVerticalAlignment((short)1);
        style.setDataFormat(STRING_FORMAT);
//        style.setFillForegroundColor((short)13);
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (isWarp) {
            style.setWrapText(true);
        }

        return style;
    }

}
