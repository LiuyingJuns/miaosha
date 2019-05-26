package com.miaosha.controller;

import com.miaosha.error.BussinessException;
import com.miaosha.error.EmBussinessError;
import com.miaosha.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
   // public static final String CONTENT_TYPE_FORMED2="application/json;charset=utf-8";

    /**
     * 定义exceptionHandle解决未被controller层吸收的exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseDate = new HashMap<>();

        if(ex instanceof BussinessException){
            BussinessException bussinessException = (BussinessException) ex;
            responseDate.put("errorCode",bussinessException.getErrorCode());
            responseDate.put("errorMsg",bussinessException.getErrorMsg());
        }
        else {
            responseDate.put("errorCode", EmBussinessError.PARAMETER_UNKNOW_ERROR.getErrorCode());
            responseDate.put("errorMsg",EmBussinessError.PARAMETER_UNKNOW_ERROR.getErrorMsg());
        }

        return CommonReturnType.creat(responseDate,"fail");
    }
}
