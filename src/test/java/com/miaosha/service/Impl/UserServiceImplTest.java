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

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    public static  int id = 7;
    public static  String telphone = "15222222222";
    public static  String password = "[B@328151c21";
    @Test
    public void getUserById() {
    }

    @Test
    public void register() {
    }

    @Test
    public void login() throws BussinessException {
       UserModel userModel = userService.login(telphone,password);
        Assert.assertNotNull(userModel);
    }

    @Test
    public void validateLogin() {
    }

    @Test
    public void changePhone() throws BussinessException {
        userService.changePhone(7,telphone);
    }

    @Test
    public void getUserById1() {
    }

    @Test
    public void register1() {
    }

    @Test
    public void validateLogin1() {
    }

    @Test
    public void changePhone1() throws BussinessException {
        userService.changePhone(id,telphone);
    }
}
