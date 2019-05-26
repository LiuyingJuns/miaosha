package com.miaosha.controller;

import com.miaosha.controller.viewobject.ItemVO;
import com.miaosha.error.BussinessException;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.ItemService;
import com.miaosha.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemController extends BaseController{

    @Autowired
    private ItemService itemService;

    /**
     * 创建商品
     * @param title
     * @param price
     * @param stock
     * @param description
     * @param imgUrl
     * @return
     * @throws BussinessException
     */
    @ResponseBody
    @RequestMapping(value = "/creat" ,method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType creatItem(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "price")BigDecimal price,
            @RequestParam(name = "stock") Integer stock,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "imgUrl") String imgUrl
            ) throws BussinessException {

        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModel1 = itemService.creatItem(itemModel);

        ItemVO itemVO = this.convertItemVoFromItemModel(itemModel1);

       return CommonReturnType.creat(itemVO);
    }

    private ItemVO convertItemVoFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        if(itemModel.getPromoModel()!=null){
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setPromoStartDate(itemModel.getPromoModel().getStart_date().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            itemVO.setPromoItemStatus(0);
        }

        return itemVO;
    }


    /**
     * 通过Id浏览商品
     */
    @ResponseBody
    @RequestMapping(value = "/getItem",method = {RequestMethod.GET})
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) throws BussinessException {
       ItemModel itemModel = itemService.getItem(id);
       ItemVO itemVO = this.convertItemVoFromItemModel(itemModel);
       return CommonReturnType.creat(itemVO);
    }


    /**
     * 将itemModelList中的ItemModel通过stream.map的方式，映射到ItemVo中来
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/selectItemByList",method = {RequestMethod.GET},headers = "api-version=1.0")
    public CommonReturnType selectItemByList(){

       List<ItemModel> itemModelList = itemService.itemModelList();
      List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
           ItemVO itemVO = this.convertItemVoFromItemModel(itemModel);
           return itemVO;
       }).collect(Collectors.toList());
       return CommonReturnType.creat(itemVOList);
    }
}


