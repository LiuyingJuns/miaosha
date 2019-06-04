package com.miaosha.dateobject;

import java.math.BigDecimal;

public class InvitationUserDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invitation_user_info.userid
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    private String userid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invitation_user_info.user_name
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invitation_user_info.parentid
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    private String parentid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invitation_user_info.partent_name
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    private String parentName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invitation_user_info.user_money
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    private BigDecimal userMoney;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invitation_user_info.partent_moeny
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    private BigDecimal parentMoney;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invitation_user_info.userid
     *
     * @return the value of invitation_user_info.userid
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public String getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invitation_user_info.userid
     *
     * @param userid the value for invitation_user_info.userid
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invitation_user_info.user_name
     *
     * @return the value of invitation_user_info.user_name
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invitation_user_info.user_name
     *
     * @param userName the value for invitation_user_info.user_name
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invitation_user_info.parentid
     *
     * @return the value of invitation_user_info.parentid
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public String getParentid() {
        return parentid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invitation_user_info.parentid
     *
     * @param parentid the value for invitation_user_info.parentid
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invitation_user_info.partent_name
     *
     * @return the value of invitation_user_info.partent_name
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invitation_user_info.partent_name
     *
     * @param parentName the value for invitation_user_info.partent_name
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public void setParentName(String parentName) {
        this.parentName = parentName == null ? null : parentName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invitation_user_info.user_money
     *
     * @return the value of invitation_user_info.user_money
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public BigDecimal getUserMoney() {
        return userMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invitation_user_info.user_money
     *
     * @param userMoney the value for invitation_user_info.user_money
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invitation_user_info.partent_moeny
     *
     * @return the value of invitation_user_info.partent_moeny
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public BigDecimal getParentMoeny() {
        return parentMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invitation_user_info.partent_moeny
     *
     * @param partentMoeny the value for invitation_user_info.partent_moeny
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    public void setParentMoeny(BigDecimal parentMoeny) {
        this.parentMoney = parentMoeny;
    }
}