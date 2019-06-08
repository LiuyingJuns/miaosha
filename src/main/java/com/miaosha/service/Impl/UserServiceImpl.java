package com.miaosha.service.Impl;

import com.miaosha.Excel.ExcelExportUtils;
import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dateobject.UserDO;
import com.miaosha.dateobject.UserPasswordDO;
import com.miaosha.error.BussinessException;
import com.miaosha.error.EmBussinessError;
import com.miaosha.service.UserService;
import com.miaosha.service.model.ExportUserModel;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
    @Transactional
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

    /**更换手机号*/
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

    /**更换密码*/
    @Override
    public void  changePassword(UserModel userModel) {
       //获取用户信息
        UserDO userDO = new UserDO();
        userDO.setId(userModel.getId());

        //获取用户密码信息
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setUserId(userDO.getId());
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());

       int i = userPasswordDOMapper.updateUserPassword(userPasswordDO);
       if (i>0){
           System.out.println("更新成功");
       }

    }

    /**
     * 找回密码
     * @param userModel
     */
    @Override
    public void findPassword(UserModel userModel) throws BussinessException {
        //校验手机号是否存在
        String telphone = userModel.getTelphone();
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if (StringUtils.isEmpty(userDO.getTelphone())){
            throw new BussinessException(EmBussinessError.USER_NOT_EXIST);
        }
        //获取用户;userid
        int userid = userModel.getId();

        //校验新密码和旧密码是否一致
        String password = userModel.getEncrptPassword();
       UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userid);
       if (!StringUtils.equals(password,userPasswordDO.getEncrptPassword())){
           log.info("新旧密码不一致");
           throw new BussinessException(EmBussinessError.USER_PASS_NOT_SAME);
       }

    }

    /**查询用户列表*/
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

       for(UserDO userDO : userDOList){
           UserModel userModel = new UserModel();
           BeanUtils.copyProperties(userDO,userModel);
           userModelList.add(userModel);
       }

      userModelList = userDOList.stream().map(userDO -> {
           UserModel userModel = new UserModel();
           BeanUtils.copyProperties(userDO,userModel);
           return userModel;
       }).collect(Collectors.toList());
        return userModelList;
    }

    /**邀请注册
     * user_id,  username,  parent_id,  account，  parent_account
     *   1.1                     1         10           100
     *   1.2                     1.1       5             10
     * */
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

    /**批量删除两种方式，一种遍历传入进来的list进行单个删除，.01一种是将传入的list进入mybatis中进行foreach删除*/
    @Override
    public int batchDeleteUser(List<Integer> ids) {
        for(Integer id : ids){
           int result = userDOMapper.deleteByPrimaryKey(id);
            if (result>0){
                log.info("批量删除成功"+result);
            }
        }
//        int result = userDOMapper.batchDeleteUser(ids);
//       if (result>0){
//           log.info("批量删除成功"+result);
//       }
    return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void exportUsers() {
        //设置表格标题名称
        String title = "用户";
        //设置表格列名数组
        String[] headers = new String[]{"用户id","用户姓名","性别","年龄","手机号","注册码","第三方","密码"};
        //设置第一行合并内容
        String firstRowTitle = "我是刘英俊啊";
        //查出用户数据
        List<UserDO> userDOList = userDOMapper.selectUserList();

        List<UserModel> userModelList = new ArrayList<>();
        //查出用户密码
        for (UserDO userDO : userDOList){
            Integer userid = userDO.getId();
            UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userid);


            UserModel userModel = new UserModel();
            userModel.setId(userDO.getId());
            userModel.setName(userDO.getName());
            if (userDO.getGender()==0){
                userModel.setGenders("男");
            }
            else if(userDO.getGender()==1){
                userModel.setGenders("女");
            }

            userModel.setAge(userDO.getAge());
            userModel.setTelphone(userDO.getTelphone());
            userModel.setRegisterMode(userDO.getRegisterMode());
            userModel.setThirdPartyId(userDO.getThirdPartyId());

            if (userPasswordDO.getEncrptPassword()!=null){
                userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
            }
            userModelList.add(userModel);
        }

       List<ExportUserModel> exportUserModels = userModelList.stream().map(
                userModel -> {
                    ExportUserModel exportUserModel = new ExportUserModel();
                    exportUserModel.setId(userModel.getId());
                    exportUserModel.setAge(userModel.getAge());
                    exportUserModel.setGenders(userModel.getGenders());
                    exportUserModel.setName(userModel.getName());
                    exportUserModel.setTelphone(userModel.getTelphone());
                    exportUserModel.setRegisterMode(userModel.getRegisterMode());
                    exportUserModel.setThirdPartyId(userModel.getThirdPartyId());
                    exportUserModel.setEncrptPassword(userModel.getEncrptPassword());
                    return exportUserModel;
                }
        ).collect(Collectors.toList());

        //创建输入流
        try {
            OutputStream outputStream = new FileOutputStream("E://a.xls");
            ExcelExportUtils.exportExcel(title,firstRowTitle,headers,exportUserModels,outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }


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
