package com.entity;

public class AwardHistory {
    private Integer id;

    private Integer userId;

    private Integer adminProId;

    private Integer status;

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

    public Integer getAdminProId() {
        return adminProId;
    }

    public void setAdminProId(Integer adminProId) {
        this.adminProId = adminProId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}