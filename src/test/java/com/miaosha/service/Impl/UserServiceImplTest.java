package com.miaosha.service.Impl;
import com.miaosha.error.BussinessException;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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


}
