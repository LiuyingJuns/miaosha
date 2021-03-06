package com.miaosha.service.model;

import java.math.BigDecimal;

public class OrderModel {

    /**
     * 订单编号
     */
    private String id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 商品ID
     */
    private Integer itemId;

    private Integer promoId;

    /**
     * 商品数量
     */
    private Integer amount;

    /**
     * 商品单价
     */
    private BigDecimal itemPrice;

    /**
     * 商品总价
     */
    private BigDecimal itemAmountPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemAmountPrice() {
        return itemAmountPrice;
    }

    public void setItemAmountPrice(BigDecimal itemAmountPrice) {
        this.itemAmountPrice = itemAmountPrice;
    }
}
