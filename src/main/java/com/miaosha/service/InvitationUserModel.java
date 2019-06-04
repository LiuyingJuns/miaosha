package com.miaosha.service;

import java.math.BigDecimal;

public class InvitationUserModel  {
    private String userid;

    private String userName;

    private String parentid;

    private String parentName;

    private BigDecimal userMoney;

    private BigDecimal parentMoney;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public BigDecimal getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    public BigDecimal getParentMoney() {
        return parentMoney;
    }

    public void setParentMoney(BigDecimal parentMoney) {
        this.parentMoney = parentMoney;
    }
}
