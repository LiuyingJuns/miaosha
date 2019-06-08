package com.miaosha.service.Impl;
import com.miaosha.error.BussinessException;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    public static  int id = 7;
    public static  String telphone = "15222222222";
    public static  String password = "[B@328151c2";
    @Test
    public void getUserById() {
    }

    @Test
    public void register() {
    }

    @Test
    public void login() throws BussinessException {
        userService.login(telphone,password);

    }

    @Test
    public void validateLogin() {
    }

    @Test
    public void changePhone() throws BussinessException {
        userService.changePhone(7,telphone);
    }

    @Test
    public void selectUserList(){
       List<UserModel> userModelList =  userService.selectUserList();
        Assert.assertNotNull(userModelList);
    }

    @Test
    public void changePassword(){
        UserModel userModel = new UserModel();
        userModel.setId(7);
        userModel.setEncrptPassword("123456aaaaaaaaaaa");
         userService.changePassword(userModel);

    }

    @Test
    public void batchDeleteUser(){
        int id1 = 14;
        int id2 = 15;


        List<Integer> ids = new ArrayList<>();

        ids.add(id1);
        ids.add(id2);

        int result = userService.batchDeleteUser(ids);
        Assert.assertNotNull(result);
    }

    @Test
    public void exportExcel(){
       userService.exportUsers();
    }


    @Test
    public void exportExcel2(){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("呵呵");

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(10);
        cell.setCellValue("我是刘英俊");
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));
        try {
            OutputStream outputStream = new FileOutputStream("E://a.xls");
            workbook.write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
