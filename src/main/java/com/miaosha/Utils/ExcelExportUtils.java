package com.miaosha.Utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelExportUtils<T> {
    public static final Logger log = LoggerFactory.getLogger(ExcelExportUtils.class);
    /**
     * 数字正则表达式匹配
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^//d+(//.//d+)?$");

    public static<T> void exportExcel(String title,String firstRowTitle,String[] headers,Collection<T> dataset,OutputStream outputStream){
        exportExcel(title,firstRowTitle,headers,dataset,outputStream,"yyyy-mm-dd");
    }

    @SuppressWarnings("uncheck")
    public static <T> void exportExcel(String title, String firstRowTitle,String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成header样式
        HSSFCellStyle headerCellStyle = workbook.createCellStyle();
        // 设置样式
        //设置一个样式
        headerCellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 生成header字体
        HSSFFont headerFont = workbook.createFont();
        // 设置字体
        headerFont.setColor(HSSFColor.HSSFColorPredefined.VIOLET.getIndex());
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(HSSFColor.HSSFColorPredefined.VIOLET.getIndex());
        // 将字体应用到样式
        headerCellStyle.setFont(headerFont);

        // 生成body样式
        HSSFCellStyle bodyCellStyle = workbook.createCellStyle();
        bodyCellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        bodyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        bodyCellStyle.setBorderBottom(BorderStyle.THIN);
        bodyCellStyle.setBorderLeft(BorderStyle.THIN);
        bodyCellStyle.setBorderRight(BorderStyle.THIN);
        bodyCellStyle.setBorderTop(BorderStyle.THIN);
        bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成body字体
        HSSFFont bodyFont = workbook.createFont();
        bodyFont.setBold(true);
        bodyCellStyle.setFont(bodyFont);

        //合并首行
        HSSFRow row = sheet.createRow(0);
        HSSFCell hssfCell = row.createCell(0);
        hssfCell.setCellValue(firstRowTitle);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length-1));
        //生成表格标题
        row = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(headerCellStyle);
            HSSFRichTextString headerStr = new HSSFRichTextString(headers[i]);
            cell.setCellValue(headerStr);
        }

        //遍历导出数据集合，生成数据行
        Iterator<T> it = dataset.iterator();
        int index = 1;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            //利用反射，根据JavaBean属性的先后顺序，动态调用getXXX方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(bodyCellStyle);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    //调用get方法
                    Class cls = t.getClass();
                    Method method = cls.getMethod(getMethodName, new Class[]{});
                    Object value = method.invoke(t, new Object[]{});
                    //判断值类型，并转换成需要的格式
                    String textValue = null;
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else {
                        // 其他数据类型皆转换成String
                        textValue = value.toString();
                    }

                    //处理值，并填充到Cell
                    if (textValue != null) {
                        //数字统一用Double处理
                        Matcher matcher = NUMBER_PATTERN.matcher(textValue);
                        if (matcher.matches()) {
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richTextString = new HSSFRichTextString(textValue);
                            HSSFFont font = workbook.createFont();
                            font.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
                            richTextString.applyFont(font);
                            cell.setCellValue(richTextString);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }

        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
