package com.miaosha.service;

import com.miaosha.error.BussinessException;

import java.util.List;

public interface InvitationUserServive {
    /**注册送奖励*/
    void register(InvitationUserModel invitationUserModel) throws BussinessException;

    /**邀请注册送奖励*/
    InvitationUserModel invitationUser();

    /**通过父id查所有的子用户*/
    List<InvitationUserModel> selectUserByParentid(InvitationUserModel invitationUserModel);
}
