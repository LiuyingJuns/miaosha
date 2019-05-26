package com.miaosha.service;

import com.miaosha.error.BussinessException;
import com.miaosha.service.model.OrderModel;

public interface OrderService {

    /**
     * 创建订单
     * @return
     */
    public OrderModel creatOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BussinessException;
}
