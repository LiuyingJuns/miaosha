package com.miaosha.error;

/**
 * 包装器业务异常处理器
 */
public class BussinessException extends Exception implements CommonError {

    private CommonError commonError;


    /**
     * 直接接收EmBussinessError的传参用户构造业务异常
     * @param commonError
     */
    public BussinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    /**
     * 接收自定义errorMsg的方式构造业务异常
     * @return
     */

    public BussinessException(CommonError commonError,String errorMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrorMsg(errorMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.commonError.setErrorMsg(errorMsg);
        return this;

    }
}
