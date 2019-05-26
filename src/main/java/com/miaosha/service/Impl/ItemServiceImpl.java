package com.miaosha.service.Impl;

import com.miaosha.dao.ItemDoMapper;
import com.miaosha.dao.ItemStockDoMapper;
import com.miaosha.dateobject.ItemDo;
import com.miaosha.dateobject.ItemStockDo;
import com.miaosha.error.BussinessException;
import com.miaosha.error.EmBussinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.PromoService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.PromoModel;
import com.miaosha.validation.ValidationResult;
import com.miaosha.validation.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = Exception.class,
        isolation = Isolation.DEFAULT,
        readOnly = false
)

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private PromoService promoService;

    private ItemDo convertItemDoFromItemModel(ItemModel itemModel) throws BussinessException {
        if(itemModel == null){
          return null;
        }
        ItemDo itemDo = new ItemDo();
        BeanUtils.copyProperties(itemModel,itemDo);

        //将itemmodel的price转化为double类型，传给itemdo
        itemDo.setPrice(itemModel.getPrice().doubleValue());
        return itemDo;
    }

    private ItemStockDo convertItemStockDoFromItemModel(ItemModel itemModel) throws BussinessException {
        if(itemModel == null){
            return null;
        }
        ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setStock(itemModel.getStock());
        itemStockDo.setItemId(itemModel.getId());
        return itemStockDo;
    }

    /**
     * 创建商品
     */
    @Transactional(
            rollbackFor = Exception.class,
            readOnly = false
    )
    @Override
    public ItemModel creatItem(ItemModel itemModel) throws BussinessException {
        //校验必填项
       ValidationResult validationResult = validator.validate(itemModel);
       if(validationResult.isError()){
           throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
       }
        //model -> ItemDo
       ItemDo itemDo = this.convertItemDoFromItemModel(itemModel);

        //进数据库
       itemDoMapper.insertSelective(itemDo);


       //model -> ItemStockDo
       itemModel.setId(itemDo.getId());
       ItemStockDo itemStockDo = this.convertItemStockDoFromItemModel(itemModel);

        //进数据库
        itemStockDoMapper.insertSelective(itemStockDo);

        //返回查询得到的item
        return this.getItem(itemModel.getId());



    }

    /**
     * 查询商品列表：将itemDoList中的每一个itemDo通过stream.map的方法，一一映射到itemModel中
     * @return
     */
    @Override
    public List<ItemModel> itemModelList() {
     List<ItemDo> itemDoList = itemDoMapper.selectList();
     List<ItemModel> itemModelList = itemDoList.stream().map(itemDo -> {
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
         ItemModel itemModel = null;
         try {
             itemModel = this.convertModerFromDateSource(itemDo,itemStockDo);
         } catch (BussinessException e) {
             e.printStackTrace();
         }
         return itemModel;
     }).collect(Collectors.toList());
     return itemModelList;
    }

    /**
     * 通过id获取商品信息
     * Item.id == ItemStock.ItemId
     * @param id
     * @return
     * @throws BussinessException
     */
    @Override
    public ItemModel getItem(Integer id) throws BussinessException {

     ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);

     ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());

     //dateobject->model
     ItemModel itemModel = this.convertModerFromDateSource(itemDo,itemStockDo);
        if(itemModel == null){
            return null;
        }

       PromoModel promoModel = promoService.getPromoItem(itemModel.getId());
        if(promoModel !=null && promoModel.getStatus().intValue()!=3){
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    /**
     * 减库存
     * @param itemId
     * @param amount
     * @return
     * @throws BussinessException
     */
    @Override
    public boolean decreaseItemStock(Integer itemId, Integer amount) throws BussinessException {
          int affectRow = itemStockDoMapper.decreaseStock(itemId,amount);
          if(affectRow > 0){
              return true;
          }else {
              return false;
          }

    }

    /**
     * 购买商品后，对应的销量增加
     * @param itemId
     * @param amount
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseSales(Integer itemId, Integer amount) {
        itemDoMapper.increaseSales(itemId,amount);


    }



    private ItemModel convertModerFromDateSource(ItemDo itemDo,ItemStockDo itemStockDo) throws BussinessException {
        ItemModel itemModel = new ItemModel();
        if(itemModel == null){
           return null;
        }
        BeanUtils.copyProperties(itemDo,itemModel);
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));
        itemModel.setStock(itemStockDo.getStock());
       return itemModel;
    }


}
