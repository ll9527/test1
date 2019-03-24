package com.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ShopOrderGoods {
    private Integer id;

    private Integer goodsId;

    private Integer userId;

    private Date addTime;

    private String goodsName;

    private Integer isGroup;

    private BigDecimal price;

    private BigDecimal groupPrice;

    private Integer goNum;

    private BigDecimal totalPrice;

    private Integer orderId;

    private String pVersion;
    
    private String image;

    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Integer getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Integer isGroup) {
        this.isGroup = isGroup;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(BigDecimal groupPrice) {
        this.groupPrice = groupPrice;
    }

    public Integer getGoNum() {
        return goNum;
    }

    public void setGoNum(Integer goNum) {
        this.goNum = goNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getpVersion() {
        return pVersion;
    }

    public void setpVersion(String pVersion) {
        this.pVersion = pVersion == null ? null : pVersion.trim();
    }
}