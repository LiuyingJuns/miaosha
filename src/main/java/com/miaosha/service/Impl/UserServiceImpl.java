package com.miaosha.service.Impl;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dateobject.UserDO;
import com.miaosha.dateobject.UserPasswordDO;
import com.miaosha.error.BussinessException;
import com.miaosha.error.EmBussinessError;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import com.miaosha.validation.ValidationResult;
import com.miaosha.validation.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    public static final Logger log = Logger.getLogger(UserServiceImpl.class);
    /**
     * 通过id获取用户信息
     * @param id
     * @return
     */
    @Override
    public UserModel getUserById(Integer id){
       UserDO userDO = userDOMapper.selectByPrimaryKey(id);
       UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataSource(userDO,userPasswordDO);
    }

    /**
     * 短信注册的方法
     * @param userModel
     * @throws BussinessException
     */
    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class},
            readOnly = false,
            isolation = Isolation.DEFAULT
    )
    @Override
    public void register(UserModel userModel) throws BussinessException {
        if(userModel == null){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"重要参数缺失");
        }
//        if(StringUtils.isEmpty(userModel.getName())
//                ||userModel.getGender()==0
//                ||userModel.getAge()==null
//                ||StringUtils.isEmpty(userModel.getEncrptPassword())
//                ||StringUtils.isEmpty(userModel.getTelphone())
//
//        ){
//            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"请检查必填项");
//        }
       ValidationResult validationResult = validator.validate(userModel);
        if(validationResult.isError()){
            throw  new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR, validationResult.getErrMsg());
        }


        UserDO userDO = this.convertFromUserModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateFormatFlagsException ex){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"手机号不能重复添加");
        }


        //将userDo的id赋给userModel，不然userpasswordDo会缺少id
        // 并且要在userdomapper的insertselective里面
        // 设置keyProperty="id" useGeneratedKeys="true"

        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = this.convertFromUserPasswordModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);




        return;
    }



    /** 用户登录*/
    @Override
    public void login(String telphone,String password) throws BussinessException {
        //先判断用户是否存在
       UserDO userDO = userDOMapper.selectByTelphone(telphone);
       if (userDO == null){
           throw new BussinessException(EmBussinessError.USER_NOT_EXIST);
       }
       //存在，则校验手机号密码是否一致
       Boolean result = this.validateUserTelphone(telphone,password);

    }

    private Boolean validateUserTelphone(String telphone,String password) throws BussinessException {
        //先通过手机号查出用户信息
       UserDO userDO = userDOMapper.selectByTelphone(telphone);
       if (userDO == null){
           log.error("用户不存在"+userDO);
           throw new BussinessException(EmBussinessError.USER_NOT_EXIST);
       }
       //查出用户密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

       //校验用户密码是否一致
       if (!StringUtils.equals(userPasswordDO.getEncrptPassword(),password)){
           log.error("用户手机号密码错误"+password);
           System.out.println("用户手机号或密码错误");
       }else {
           log.info("用户登录成功"+"telphone:"+telphone+"password:"+password);
           return true;
       }
       return true;
    }

    /**
     * 验证用户密码和手机号是否一致
     * @param telphone
     * @param password
     */
    @Override
    public UserModel validateLogin(String telphone, String password) throws BussinessException {
       UserDO userDO = userDOMapper.selectByTelphone(telphone);
       if(userDO == null){
          return null;
       }

      UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

       UserModel userModel = this.convertFromDataSource(userDO,userPasswordDO);

       //判断传入的密码是否和数据库的密码一致
        if(!StringUtils.equals(password,userModel.getEncrptPassword())){
            System.out.println("用户手机号或密码错误");
        }
        return userModel;
    }

    @Override
    public void changePhone(Integer id,String telphone) throws BussinessException {
        //先查询是否存在该手机号
       UserDO userDO = userDOMapper.selectByPrimaryKey(id);
       if (userDO == null){
           throw new BussinessException(EmBussinessError.USER_NOT_EXIST);
       }

       //如果存在该手机号，那么更换手机号码
       userDO.setTelphone(telphone);
      int result = userDOMapper.updateByTelphone(userDO);
      if(result > 0){
          System.out.println("更新成功");
      }

    }

    @Override
    public void changePassword(UserModel userModel) {

    }

    @Override
    public void findPassword(UserModel userModel) {

    }

    @Override
    public List<UserModel> selectUserList() {
       List<UserDO> userDOList = userDOMapper.selectUserList();
       List<UserModel> userModelList = new ArrayList<>();
       for (int i=0;i<userDOList.size();i++){
          UserDO userDO = userDOList.get(i);
          UserModel userModel = new UserModel();
          BeanUtils.copyProperties(userDO,userModel);
          userModelList.add(userModel);
       }
        return userModelList;
    }

    @Override
    public UserModel inviteRegister(UserModel userModel) {
        return null;
    }

    /**
     * 将实体类的属性封装到模型层，实体类的属性不与service层关联
     * @param userDO
     * @param userPasswordDO
     * @return
     */
    private UserModel convertFromDataSource(UserDO userDO ,UserPasswordDO userPasswordDO){
        UserModel userModel = new UserModel();
        if(userModel == null){
            return null;
        }
        BeanUtils.copyProperties(userDO,userModel);
        if(userModel !=null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }


        return userModel;
    }

    /**
     * 将usermodel得到的数据封装至userdo层
     * @param userModel
     * @return
     * @throws BussinessException
     */
    private UserDO convertFromUserModel(UserModel userModel) throws BussinessException {
        if(userModel ==null){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"重要参数缺失");
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserPasswordDO convertFromUserPasswordModel(UserModel userModel) throws BussinessException {
        if(userModel ==null){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"重要参数缺失");
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }



}
