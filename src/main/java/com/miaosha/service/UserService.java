package com.miaosha.service;

import com.miaosha.error.BussinessException;
import com.miaosha.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BussinessException;

    UserModel login(String telphone,String password) throws BussinessException;

    UserModel validateLogin(String telphone, String password) throws BussinessException;

    void changePhone(Integer id, String telphone) throws BussinessException;

    void changePassword(UserModel userModel);

    void findPassword(UserModel userModel);

    UserModel inviteRegister(UserModel userModel);
}
