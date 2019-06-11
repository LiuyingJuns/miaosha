package com.miaosha.Utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class IputOtputExcel_ {
    public static void main(String[] args) throws IOException {

//        //Excel内容
//        String[] title = {"id","name","sex"};
//
//        //创建excel工作簿
//        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
//
//        //创建Excel工作表
//        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
//
//        //创建第一行
//       HSSFRow hssfRow = hssfSheet.createRow(0);
//
//        //引用一个单元格
//        HSSFCell hssfCell = null;
//
//        //插入第一行数据id,name,sex
//        for (int i = 0 ;i<title.length;i++){
//            //创建i个单元格
//            hssfCell = hssfRow.createCell(i);
//
//            //单元格赋予值
//            hssfCell.setCellValue(title[i]);
//        }
//
//        //追加数据
//        for (int i = 0;i<10;i++){
//           HSSFRow hssfRow1 = hssfSheet.createRow(i);
//           hssfCell = hssfRow1.createCell(0);
//           hssfCell.setCellValue("a"+i);
//
//            hssfCell = hssfRow1.createCell(1);
//            hssfCell.setCellValue("lili"+i);
//
//            hssfCell = hssfRow1.createCell(2);
//            hssfCell.setCellValue("男"+i);
//
//        }
//
//        //创建文件
//        File file = new File("C://Users//acer-pc//Desktop//miaosha//导入2.xls");
//
//        FileOutputStream outputStream = new FileOutputStream(file);
//
//        //将工作簿写入到输出流中
//        hssfWorkbook.write(outputStream);
//        outputStream.close();
        String[] title = {"name","age","gender"};
        HSSFWorkbook workbook = new HSSFWorkbook();
       HSSFSheet hssfSheet = workbook.createSheet();
      HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = null;
        for (int i = 0;i<title.length;i++){
            hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(title[i]);
        }

        for (int i = 1;i<10;i++){
           HSSFRow hssfRow1 = hssfSheet.createRow(i);

          HSSFCell hssfCell1 = hssfRow1.createCell(0);
          hssfCell1.setCellValue("liu"+i);

            HSSFCell hssfCell2 = hssfRow1.createCell(1);
            hssfCell2.setCellValue("20"+i);

            HSSFCell hssfCell3 = hssfRow1.createCell(2);
            hssfCell3.setCellValue("男"+i);
        }
        File file = new File("C://Users//acer-pc//Desktop//miaosha//导入3.xls");
        OutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }
}
