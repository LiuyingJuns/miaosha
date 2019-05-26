package com.miaosha.service.Impl;

import com.miaosha.dao.PromoDoMapper;
import com.miaosha.dateobject.PromoDo;
import com.miaosha.service.PromoService;
import com.miaosha.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDoMapper promoDoMapper;

    @Override
    public PromoModel getPromoItem(Integer itemId) {

        //通过itemID查出营销商品信息
        PromoDo promoDo = promoDoMapper.selectByItemId(itemId);

     //  database->model
       PromoModel promoModel = this.convertPromoModelFromPromoDo(promoDo);

       //开始时间在当前时间之后，活动未开始；结束时间在当前时间之前，活动已结束
       if(promoModel.getStart_date().isAfterNow()){
           promoModel.setStatus(1);
       }else if(promoModel.getEnd_date().isBeforeNow()){
           promoModel.setStatus(3);
       }else {
           promoModel.setStatus(2);
       }
        return promoModel;
    }


    private PromoModel convertPromoModelFromPromoDo(PromoDo promoDo){
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDo,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDo.getPromoItemPrice()));
        promoModel.setStart_date(new DateTime(promoDo.getStartDate()));
        promoModel.setEnd_date(new DateTime(promoDo.getEndDate()));
        return promoModel;
    }
}
