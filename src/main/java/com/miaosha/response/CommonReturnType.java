package com.miaosha.response;

/**
 * 返回正确的信息
 */
public class CommonReturnType {
    /**
     * 表明对应请求的返回处理结果为success或者fail
     */
    private String status;

    /**
     * 若status=success，则data返回前段需要的json数据
     * 若status=fail，则返回通用的状态码
     */
    private Object data;

    /**
     * 定义一个通用的创建的方法
     * @param result
     * @return
     */
    public static CommonReturnType creat(Object result){
        return CommonReturnType.creat(result,"success");
    }

    public static CommonReturnType creat(Object result,String status){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setData(result);
        commonReturnType.setStatus(status);
        return commonReturnType;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
