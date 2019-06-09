package com.miaosha.service;

import com.miaosha.error.BussinessException;
import com.miaosha.service.model.UserModel;

import java.util.List;

public interface UserService {
    /**通过id获取用户信息*/
    UserModel getUserById(Integer id);

    /**注册*/
    void register(UserModel userModel) throws BussinessException;

    /**登录*/
    void login(String telphone,String password) throws BussinessException;

    /***校验手机号密码*/
    UserModel validateLogin(String telphone, String password) throws BussinessException;

    /**更换手机号*/
    void changePhone(Integer id, String telphone) throws BussinessException;

    /**更换密码*/
    void changePassword(UserModel userModel);

    /**找回密码*/
    void findPassword(UserModel userModel) throws BussinessException;

    /**查询用户列表*/
    List<UserModel> selectUserList();

    /**邀请注册*/
    UserModel inviteRegister(UserModel userModel);

    /**批量删除用户*/
    int batchDeleteUser(List<Integer> ids);

    /**导出用户数据*/
    void exportUsers();

    /**导入用户数据*/
    void inputUsers();
}
