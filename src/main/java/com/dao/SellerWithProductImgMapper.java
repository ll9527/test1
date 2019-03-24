package com.dao;

import com.entity.ClassWithProduct;
import com.entity.SellerWithProductImg;

import java.util.List;

/*
  商家关联所属商品
* */

public interface SellerWithProductImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerWithProductImg record);

    int insertSelective(SellerWithProductImg record);

    SellerWithProductImg selectByPrimaryKey(Integer id);
    
    /**
     * 用商品id查询商户id
     * @param productid
     * @return
     */
    SellerWithProductImg selectByPId(Integer productid);
    
    int updateByPrimaryKeySelective(SellerWithProductImg record);

    int updateByPrimaryKey(SellerWithProductImg record);

    List<SellerWithProductImg> selectByPid(Integer productid);
}