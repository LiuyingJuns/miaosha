package com.miaosha.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品模型层
 */
public class ItemModel {

    private Integer id;

    /**
     * title 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String title;

    /**
     *  price 商品价格
     */
    @Min(value = 0,message = "商品价格必须大于0")
    private BigDecimal price;

    /**
     * stock 库存数量
     */
    @NotNull(message = "库存数量不能为空")
    private Integer stock;

    @NotBlank(message = "商品描述不能为空")
    private String description;

    /**
     *  sales 销售数量
     */
    private Integer sales;

    /**
     * imgUrl 商品图片
     */
    @NotNull(message = "商品图片不能玩为空")
    private String imgUrl;

    private PromoModel promoModel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }
}
