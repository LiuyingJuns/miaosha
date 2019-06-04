package com.miaosha.error;

public enum EmBussinessError implements CommonError {
    /**
     * 通用错误类型10001
     */
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),

    PARAMETER_UNKNOW_ERROR(10002,"未知错误"),

    /**
     * 定义20000开头的为用户信息相关错误定义
     */
    USER_NOT_EXIST(20001,"用户不存在"),

    USER_LOGIN_FAIL(20002,"手机号或密码错误"),

    USER_NOT_LOGIN(20003,"用户未登录"),

    USER_PASS_NOT_SAME(20004,"新旧密码一致"),
    /**
     * 定义30000开头的为商品信息相关错误定义
     */
    STOCK_NOT_ENOUGH(30001,"库存不足"),

    USER_IS_EXIST(30002,"用户已存在");
    private int errorCode;
    private String errorMsg;

    EmBussinessError(int errorCode,String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }





    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
         this.errorMsg = errorMsg;
         return this;
    }
}
