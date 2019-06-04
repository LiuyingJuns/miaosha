package com.miaosha.service.Impl;

import com.miaosha.dao.InvitationUserDOMapper;
import com.miaosha.dateobject.InvitationUserDO;
import com.miaosha.error.BussinessException;
import com.miaosha.error.EmBussinessError;
import com.miaosha.service.InvitationUserModel;
import com.miaosha.service.InvitationUserServive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class InvitationUserServiveImpl implements InvitationUserServive {
    private static final Logger log = LoggerFactory.getLogger(InvitationUserServiveImpl.class);
    @Autowired
    private InvitationUserDOMapper invitationUserDOMapper;

    @Transactional(rollbackFor = Exception.class,readOnly = false,isolation = Isolation.DEFAULT)
    @Override
    public void register(InvitationUserModel invitationUserModel) throws BussinessException {
        //判断用户是否已注册
        //通过用户id查询客户信息
        String userid = invitationUserModel.getUserid();
        InvitationUserDO invitationUserDO = invitationUserDOMapper.selectUserByUserid(userid);
        if (invitationUserDO != null){
            log.info("用户已存在,请重新注册"+invitationUserDO);
            throw new BussinessException(EmBussinessError.USER_IS_EXIST);
        }
        //用户注册
        InvitationUserDO invitationUserDO1 = new InvitationUserDO();
        BeanUtils.copyProperties(invitationUserModel,invitationUserDO1);
        invitationUserDO1.setUserid(userid);

        //用户获得奖励,默认金额设置为：20
        invitationUserDO1.setUserMoney(new BigDecimal(20));

        //用户注册入库
        invitationUserDOMapper.insertSelective(invitationUserDO1);
    }


    @Override
    public InvitationUserModel invitationUser() {
        return null;
    }

    @Override
    public List<InvitationUserModel> selectUserByParentid(InvitationUserModel invitationUserModel) {
        return null;
    }

    /**
    @Override
    public List<InvitationUserModel> selectUserByParentid(InvitationUserModel invitationUserModel) {
        //判断传入的父id用户是否存在
        String parentid = invitationUserModel.getUserid();
        invitationUserDOMapper.selectUserByUserid()
        //
        return null;
    }
    */
}


