package com.miaosha.Excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcel {
    public static void main(String[] args) throws IOException {

//        File file = new File("C://Users//acer-pc//Desktop//miaosha//导入.xls");
//        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
//        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
//
//        int firstRowNum =  0;
//        //获取最后一行行号
//       int lastRowNumber = hssfSheet.getLastRowNum();
//        for (int i = firstRowNum;i<lastRowNumber;i++){
//               HSSFRow hssfRow = hssfSheet.getRow(i);
//
//               //获取当前行最后单元格序号
//           int lastCellNumber = hssfRow.getLastCellNum();
//            for (int j = 0;j<lastCellNumber;j++){
//               HSSFCell hssfCell = hssfRow.getCell(j);
//              String value =  hssfCell.getStringCellValue();
//                System.out.println("value:"+value);
//            }
//            System.out.println();
//        }


        File file = new File("C://Users//acer-pc//Desktop//miaosha//导入3.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        int firstRowNum = 0;
       int lastRowNum = hssfSheet.getLastRowNum();
        for (int i = firstRowNum;i<lastRowNum;i++){
           HSSFRow hssfRow = hssfSheet.getRow(i);

           int firstCellNum = 0;
          int lastCellNum = hssfRow.getLastCellNum();
          for (int j=firstCellNum;j<lastCellNum;j++){
             HSSFCell hssfCell = hssfRow.getCell(j);
             String vaule = hssfCell.getStringCellValue();
              System.out.print(vaule+"   ");
          }
            System.out.println();
        }
        hssfWorkbook.close();
    }


}
