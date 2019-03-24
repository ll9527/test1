package com.entity;

public class Version1 {
    private Integer id;

    private Integer productId;

    private String productName;

    private String version1Name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getVersion1Name() {
        return version1Name;
    }

    public void setVersion1Name(String version1Name) {
        this.version1Name = version1Name == null ? null : version1Name.trim();
    }
}