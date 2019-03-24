package com.entity;

import java.math.BigDecimal;

public class AdminProfit {
    private Integer id;

    private String password;

    private String percentClass;

    private Double shopPercent;

    private BigDecimal sellerApplyMoney;

    private BigDecimal vipMoney;

    private BigDecimal bonusPools;

    private Integer poolsPersent;

    private BigDecimal discountAmount;

    private Integer discountPersent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPercentClass() {
        return percentClass;
    }

    public void setPercentClass(String percentClass) {
        this.percentClass = percentClass == null ? null : percentClass.trim();
    }

    public Double getShopPercent() {
        return shopPercent;
    }

    public void setShopPercent(Double shopPercent) {
        this.shopPercent = shopPercent;
    }

    public BigDecimal getSellerApplyMoney() {
        return sellerApplyMoney;
    }

    public void setSellerApplyMoney(BigDecimal sellerApplyMoney) {
        this.sellerApplyMoney = sellerApplyMoney;
    }

    public BigDecimal getVipMoney() {
        return vipMoney;
    }

    public void setVipMoney(BigDecimal vipMoney) {
        this.vipMoney = vipMoney;
    }

    public BigDecimal getBonusPools() {
        return bonusPools;
    }

    public void setBonusPools(BigDecimal bonusPools) {
        this.bonusPools = bonusPools;
    }

    public Integer getPoolsPersent() {
        return poolsPersent;
    }

    public void setPoolsPersent(Integer poolsPersent) {
        this.poolsPersent = poolsPersent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getDiscountPersent() {
        return discountPersent;
    }

    public void setDiscountPersent(Integer discountPersent) {
        this.discountPersent = discountPersent;
    }
}