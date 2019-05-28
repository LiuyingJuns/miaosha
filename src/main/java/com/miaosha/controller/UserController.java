package com.miaosha.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaosha.Utils.JwtUtils;
import com.miaosha.controller.viewobject.UserVO;
import com.miaosha.error.BussinessException;
import com.miaosha.error.EmBussinessError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

/**
 * @CrossOrigin：解决ajax在谷歌浏览器跨域请求的问题
 */
@Controller
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 用户获取OTP短信接口
     * @param telphone
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOtp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType getOtp(@RequestParam(name="telphone") String telphone){
        /**
         * 按照一定的规则生成OTP验证码
         */
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        /**
         * 将OTP验证码同用户手机号进行关联，使用httpsession的方式绑定用户的手机号和OTPCODE
         */
        httpServletRequest.getSession().setAttribute(telphone,otpCode);

        System.out.println("telphone:"+ telphone+";"+"otpcode:"+otpCode);

        return CommonReturnType.creat(null);
    }

    /**
     * 用户注册的接口
     * @param name
     * @param age
     * @param gender
     * @param telphone
     * @param otpCode
     * @param password
     * @return
     * @throws BussinessException
     */
    @ResponseBody
    @RequestMapping(value = "/register" ,method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType register(@RequestParam(name = "name") String name,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "gender") Byte gender,
                                     @RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "password") String password
                                     ) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
            //校验otpCode是否一致
           String inOtpCode = (String) httpServletRequest.getSession().getAttribute(telphone);
       if(!StringUtils.equals(otpCode,inOtpCode)){
           throw  new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不一致");
       }


        //实现用户注册
       UserModel userModel = new UserModel();
       userModel.setTelphone(telphone);

       userModel.setAge(age);
       userModel.setGender(gender);
       userModel.setName(name);

        //密码加密
        userModel.setEncrptPassword(this.EncodedByMd5(password));

        userService.register(userModel);

        return CommonReturnType.creat(null);

    }

    /**
     * 密码加密的方法
     * @param string
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String EncodedByMd5(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest MD5 = MessageDigest.getInstance("MD5");
        Base64 base64 = new Base64();

        byte[] newStr = base64.encode(MD5.digest(string.getBytes("utf-8")));
       return String.valueOf(newStr);


    }



    /**
     * 用户登录的接口
     * @param telphone
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @ResponseBody
    @RequestMapping(value = "/login" ,method = {RequestMethod.POST} ,consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType userLogin(
            @RequestParam(name="telphone") String telphone,
            @RequestParam(name="password") String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, BussinessException {

        //先判断手机号存不存在
       if(StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
           throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"手机号码或者密码不能为空");
       }

       //判断手机号和密码在数据库的数据是否一致
       UserModel userModel = userService.validateLogin(telphone,this.EncodedByMd5(password));
       UserVO userVO =  this.convertFromUserModel(userModel);


       //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userVO);
        return CommonReturnType.creat(userVO);
    }

    /**
     * 用户token登录的接口
     */

    @ResponseBody
    @RequestMapping(value = "/loginWithToken",method = RequestMethod.POST)
    public CommonReturnType userlogins(@RequestBody Map<String,String> map) throws BussinessException {
        String username = map.get("username");
        String password = map.get("password");

        //验证身份是否正确
        UserModel userModel = userService.validateLogin(username,password);
        if (userModel != null){
        UserModel userModel1 = userService.getUserById(userModel.getId());
            //返回token
            String token = JwtUtils.sign(username, String.valueOf(userModel1.getId()));
            if (token != null){
                return CommonReturnType.creat(token);
            }
        }
        //返回空的信息
        return null;
    }





    /**
     *通过id获取用户信息的接口
     * @param id
     * @return
     * @throws BussinessException
     */
    @ResponseBody
    @RequestMapping(value = "/get")
    public CommonReturnType getUserById(@RequestParam(name = "id") Integer id) throws BussinessException {

        //调用service服务获取对应的用户id，并返给前端
       UserModel userModel = userService.getUserById(id);

       //若获取的对应用户的信息不存在
        if(userModel == null){
            // userModel.setEncrptPassword("52515");
            throw new BussinessException(EmBussinessError.USER_NOT_EXIST);

        }

       //将核心领域中的模型用户对象转化为可供前端使用的viewobject
       UserVO userVO =convertFromUserModel(userModel);
       return CommonReturnType.creat(userVO);
    }

    /**
     * 视图层能看到的数据
     * @param userModel
     * @return
     */
    private UserVO convertFromUserModel(UserModel userModel){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }





}
