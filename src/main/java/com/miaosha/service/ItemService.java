package com.miaosha.service;

import com.miaosha.error.BussinessException;
import com.miaosha.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    /**
     * 创建商品
     * @param itemModel
     * @return
     * @throws BussinessException
     */
    public ItemModel creatItem(ItemModel itemModel) throws BussinessException;

    /**
     * 商品列表
     * @return
     */
    public List<ItemModel> itemModelList();

    /**
     * 商品详情页
     * @param itemId
     * @return
     * @throws BussinessException
     */
    public ItemModel getItem(Integer itemId) throws BussinessException;


    /**
     * 减掉库存
     * @param itemId
     * @param amount
     * @return
     * @throws BussinessException
     */
    boolean decreaseItemStock(Integer itemId,Integer amount) throws BussinessException;

    void increaseSales(Integer itemId,Integer amount);


}
