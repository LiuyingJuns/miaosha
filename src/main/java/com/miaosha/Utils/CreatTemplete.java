package com.miaosha.Utils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;

public class CreatTemplete {
    public static void main(String[] args) throws JDOMException, IOException {

        //获取解析xml路径
       String path = System.getProperty("user.dir")+"/src/main/resources/Student.xml";
        System.out.println(path);

        File file = new File(path);


        SAXBuilder builder = new SAXBuilder();

        //  //使用saxbuild进行解析xml;返回的是document对象
        Document parse = builder.build(file);

        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet hssfSheet = workbook.createSheet("sheet0");

        //获取xml根节点
        Element element = parse.getRootElement();

        //获取xml文件中的<excel id="student" code="student" name="学生信息导入">
       String templete = element.getAttribute("name").getValue();

       int colunm = 0;
       int row =0;
        //设置列宽
        //获取<colgroup>节点
       Element colgroup = element.getChild("colgroup");

       setColunmWidth(hssfSheet,colgroup);
    }

    private static void setColunmWidth(HSSFSheet hssfSheet, Element colgroup) {
    }
}
