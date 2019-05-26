package com.miaosha.validation;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class ValidationResult {

    private  boolean isError = false;

    private Map<String,String> errorMsgMap = new HashMap<>();

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public Map<String, String> getErrorMsg() {
        return errorMsgMap;
    }

    public void setErrorMsg(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    public String getErrMsg(){

      return StringUtils.join(errorMsgMap.values().toArray(),",");
    }
}
