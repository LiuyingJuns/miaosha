package com.miaosha.dao;

import com.miaosha.dateobject.InvitationUserDO;

import java.util.List;

public interface InvitationUserDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invitation_user_info
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    int insert(InvitationUserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invitation_user_info
     *
     * @mbg.generated Sun Jun 02 23:36:01 CST 2019
     */
    int insertSelective(InvitationUserDO record);

    List<InvitationUserDO> selectUserByParentid(InvitationUserDO invitationUserDO);

    InvitationUserDO selectUserByUserid(String userid);
}