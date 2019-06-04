package com.miaosha.Excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class ExcelExportUtils<T> {
    public static final Logger log = LoggerFactory.getLogger(ExcelExportUtils.class);
    /**
     * @param title 表格标题名
     * @param headers 表格列名数组
     * @param dataset  导出的数据集合
     * @param out 输出流
     * @param pattern 时间格式
     */
    public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern){
        //声明工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //声明表格
        HSSFSheet sheet = workbook.createSheet(title);

        //设置默认宽度，15字节
        sheet.setDefaultColumnWidth(15);

        //生成表格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        //设置一个样式
        cellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.VIOLET.getIndex());
        font.setFontHeight((short) 2000);
        font.setBold(true);

        //应用字体到当前样式
        cellStyle.setFont(font);

        //生成另外一个样式
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

        //生成另外一个字体
       HSSFFont font2 = workbook.createFont();
        font2.setBold(true);

        //将字体应用到当前样式
        cellStyle2.setFont(font2);

        //声明画图的管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        //定义注释的大小和位置
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,0,0,0,(short)4,2,(short)6,5));

        //设置注释内容
        comment.setString(new HSSFRichTextString("可以在poi中添加注释"));

        //设置注释作者
        comment.setAuthor("刘英俊");

        //表格标题行
        //创建第一行
        HSSFRow row = sheet.createRow(0);
        for (int i=0;i<headers.length;i++){
               HSSFCell cell = row.createCell(i);
               cell.setCellStyle(cellStyle);
               HSSFRichTextString textString = new HSSFRichTextString(headers[i]);
               cell.setCellValue(textString);
        }

        //遍历集合数据，填充数据行
        Iterator<T> iterator = dataset.iterator();
        int index = 0;
        while (iterator.hasNext()){
            index++;
            HSSFRow hssfRow = sheet.createRow(index);
           T t = (T)iterator.next();

           //利用反射，根据javabean先后顺序，动态调用get方法得到属性值
           Field[] fields = t.getClass().getDeclaredFields();
           for (int i=0;i<fields.length;i++){
              HSSFCell hssfCell = row.createCell(i);
              hssfCell.setCellStyle(cellStyle2);
              Field fields1 = fields[i];
              String fieldName = fields1.getName();
              String getMethodName = "get"+fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);

              //调用get方法
              Class cls = t.getClass();
               try {
                   Method method = cls.getMethod(getMethodName,new Class[]{});
                   Object value = method.invoke(t,new Object[]{});
                   //判断值类型，并转换成需要的格式
                   String textValue = null;
                   if(value instanceof Date){
                       Date date = (Date) value;
                       SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                       textValue = dateFormat.format(date);
                   }else {
                       //其它数据类型都转换为string
                      textValue = value.toString();
                   }
               } catch (NoSuchMethodException e) {
                   e.printStackTrace();
                   log.error(e.getMessage());
               } catch (IllegalAccessException e) {
                   e.printStackTrace();
                   e.getMessage();

               } catch (InvocationTargetException e) {
                   e.printStackTrace();
                   e.getMessage();
                   log.error(e.getMessage());
               }
           }
        }
    }

}

