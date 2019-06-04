package com.miaosha.dao;

import com.miaosha.dateobject.InvitationUserDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvitationUserDOMapperTest {
    @Autowired
    private InvitationUserDOMapper invitationUserDOMapper;

    @Test
    public void insert() {
        InvitationUserDO invitationUserDO = new InvitationUserDO();
        invitationUserDO.setUserid("2");
        invitationUserDO.setParentid("1");
        invitationUserDO.setUserName("secondUser");
        invitationUserDO.setUserMoney(new BigDecimal(666));
        invitationUserDO.setParentName("firstParent");
        if(invitationUserDO != null){
            int result = invitationUserDOMapper.insert(invitationUserDO);
            Assert.assertNotNull(result);
        }

    }

    @Test
    public void insertSelective() {
    }


    @Test
    public void selectUserByPartentName(){
        InvitationUserDO invitationUserDO = new InvitationUserDO();
        invitationUserDO.setParentid("1");
        List<InvitationUserDO> invitationUserDOList = invitationUserDOMapper.selectUserByParentid(invitationUserDO);
        Assert.assertNotNull(invitationUserDOList);
    }

}