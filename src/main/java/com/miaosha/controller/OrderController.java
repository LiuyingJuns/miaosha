package com.miaosha.controller;


import com.miaosha.error.BussinessException;
import com.miaosha.error.EmBussinessError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.OrderService;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;
    @ResponseBody
    @RequestMapping(value = "/creatOrder",method = RequestMethod.POST)
    public CommonReturnType creatOrder(
                                        @RequestParam(name = "userId") Integer userId ,
                                       @RequestParam(name = "itemId") Integer itemId ,
                                       @RequestParam(name = "promoId" ,required = false) Integer promoId,
                                       @RequestParam(name = "amount") Integer amount) throws BussinessException {

      Boolean isLogin =(Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
      if(isLogin ==null || !isLogin.booleanValue()){
          throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"用户未登录");
      }

     UserModel userModel =(UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
      OrderModel orderModel = orderService.creatOrder(userModel.getId(),itemId,promoId,amount);
       return CommonReturnType.creat(orderModel);
    }

}
