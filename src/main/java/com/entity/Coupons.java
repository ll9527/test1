package com.entity;

import java.util.Date;

public class Coupons {
    private Integer id;

    private Integer userId;

    private Integer isadmin;

    private Integer productId;

    private Integer sellerId;

    private Integer preferentialMoney;
    
    private Integer orderId;
    
    private Integer onDelete;

    private Date addTime;

    private Date overTime;

    public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOnDelete() {
		return onDelete;
	}

	public void setOnDelete(Integer onDelete) {
		this.onDelete = onDelete;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Integer isadmin) {
        this.isadmin = isadmin;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getPreferentialMoney() {
        return preferentialMoney;
    }

    public void setPreferentialMoney(Integer preferentialMoney) {
        this.preferentialMoney = preferentialMoney;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }
}