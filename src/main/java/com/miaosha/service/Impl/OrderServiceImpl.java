package com.miaosha.service.Impl;

import com.miaosha.dao.ItemStockDoMapper;
import com.miaosha.dao.OrderDoMapper;
import com.miaosha.dao.SequenceDoMapper;
import com.miaosha.dateobject.OrderDo;
import com.miaosha.dateobject.SequenceDo;
import com.miaosha.error.BussinessException;
import com.miaosha.error.EmBussinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.OrderService;
import com.miaosha.service.UserService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public OrderModel creatOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BussinessException {

        //1.校验用户id是否存在、商品id是否存在、库存数量是否足够
              UserModel userModel = userService.getUserById(userId);
                if(userModel == null){
                    throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"用户不存在");
                }

               ItemModel itemModel = itemService.getItem(itemId);
                if(itemModel == null){
                    throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"商品不存在");
                }

                if(amount<0 || amount> 99 ){
                    throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"请输入正确的购买数量");
                }
                //校验是否有营销商品
                if(promoId == null){
                    throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"该活动商品信息不存在");
                }

                //校验是否是正在进行中的活动商品
                if(itemModel.getPromoModel().getStatus().intValue()!=2){
                    throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"该活动商品没有进行");
                }

                //2.落单减库存
                boolean result = itemService.decreaseItemStock(itemId,amount);
                if(!result){
                    throw new BussinessException(EmBussinessError.STOCK_NOT_ENOUGH);
                }


                //3.订单入库
                OrderModel orderModel = new OrderModel();
                orderModel.setItemId(itemId);
                orderModel.setUserId(userModel.getId());
                orderModel.setPromoId(promoId);
                orderModel.setAmount(amount);

                if(promoId != null){
                    orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
                }else{
                    orderModel.setItemPrice(itemModel.getPrice());
                }

                orderModel.setItemAmountPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

                orderModel.setId(this.generateOrderNo());
                OrderDo orderDo = this.convertOrderDoFromOrderModel(orderModel);



                orderDoMapper.insertSelective(orderDo);

                //4.销量增加
                itemService.increaseSales(itemId,amount);

                //4.返回前端
                return orderModel;
    }

    private String generateOrderNo(){
        //订单前8位为下单的时间
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime localDateTime = LocalDateTime.now();
        String nowTime = localDateTime.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowTime);

        //订单后6位为自增序列
        int sequence = 0;
        SequenceDo sequenceDo = sequenceDoMapper.getSequenceByName("order_info");
        sequence = sequenceDo.getCurrentValues();
        sequenceDo.setCurrentValues(sequenceDo.getCurrentValues()+sequenceDo.getStep());
        sequenceDoMapper.updateByPrimaryKeySelective(sequenceDo);

        String sequenceStr = String.valueOf(sequence);
        for(int i = 0 ; i<6-sequenceStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        //订单最后两位为分库分表位，默认写死为00
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

    private OrderDo convertOrderDoFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel,orderDo);
        return orderDo;
    }

}
