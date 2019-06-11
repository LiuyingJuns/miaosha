package com.miaosha.Utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelInputUtils {


    /**
     *处理xls结尾的excel
     * @param path
     * @return
     * @throws IOException
     */
    public static<T> List<List<String>> readXlsExcel(String path) throws IOException {

        //获取文件所在路径流
        InputStream inputStream = new FileInputStream(path);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

        List<List<String>> lists = new ArrayList<>();

        //获取工作簿的所有sheet表数量
        int size = workbook.getNumberOfSheets();

        //循环每一页，并处理当前页
        for(int numSheet =0;numSheet <size;numSheet ++){
           HSSFSheet sheet = workbook.getSheetAt(numSheet);
           if (sheet == null){
                continue;
           }

           //处理当前页，循环读取每一行
            for (int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
                //获取每一行
                HSSFRow row = sheet.getRow(rowNum);

               int minColIx = row.getFirstCellNum();
               int maxColIx = row.getLastCellNum();

               List<String> list = new ArrayList<>();

               //遍历该行，获取每个cell元素
                for (int colIx = minColIx;colIx<maxColIx;colIx++){
                    HSSFCell cell = row.getCell(colIx);
                    if(cell == null){
                        continue;
                    }
                    list.add(String.valueOf(cell));
                }
                lists.add(list);
            }
        }
        return lists;
    }

    public static<T> List<List<String>> readXlsxExcel(String path) throws IOException {
        //获取路径
        InputStream inputStream = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

        List<List<String>> lists = new ArrayList<>();

        int size = xssfWorkbook.getNumberOfSheets();
        //循环每一页，并处理当前页
        for(int numSheet =0;numSheet <size;numSheet ++){
            XSSFSheet sheet = xssfWorkbook.getSheetAt(numSheet);
            if (sheet == null){
                continue;
            }

            //处理当前页，循环读取每一行
            for (int rowNum=1;rowNum<sheet.getLastRowNum();rowNum++){
                XSSFRow xssfRow = sheet.getRow(rowNum);
                int minColIx = xssfRow.getFirstCellNum();
                int maxColIx = xssfRow.getLastCellNum();

               List<String> list = new ArrayList<>();

                for (int colIx=minColIx;colIx<maxColIx;colIx++){
                    XSSFCell xssfCell = xssfRow.getCell(colIx);
                    if (xssfCell == null){
                        continue;
                    }
                    list.add(String.valueOf(xssfCell));
                }
                lists.add(list);
            }
        }
        return lists;
    }

    /**
     * 改造poi默认的toString（）方法如下
     * @Title: getStringVal
     * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
     *               2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
     * @param @param cell
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String getStringVal(HSSFCell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case FORMULA:
                return cell.getCellFormula();
            case NUMERIC:
                cell.setCellType(CellType.STRING);
                return cell.getStringCellValue();
            case STRING:
                return cell.getStringCellValue();
            default:
                return "";
        }
    }

}
