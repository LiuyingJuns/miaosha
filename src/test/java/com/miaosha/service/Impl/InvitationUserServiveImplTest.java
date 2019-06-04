package com.miaosha.service.Impl;

import com.miaosha.error.BussinessException;
import com.miaosha.service.InvitationUserModel;
import com.miaosha.service.InvitationUserServive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvitationUserServiveImplTest {
    @Autowired
    private InvitationUserServive invitationUserServive;

    @Test
    public void register() throws BussinessException {
        InvitationUserModel invitationUserModel = new InvitationUserModel();
        invitationUserModel.setUserid("5");
        invitationUserModel.setUserName("第五个注册的用户");
        invitationUserModel.setParentid("1");
        invitationUserModel.setParentName("firstUser");
        if (invitationUserModel != null){
            invitationUserServive.register(invitationUserModel);
        }

    }

    @Test
    public void invitationUser() {
    }
}