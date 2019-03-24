package com.service;

import com.entity.SellerWithProductImg;

import java.util.List;

public interface SellerPimgService {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerWithProductImg record);

    int insertSelective(SellerWithProductImg record);

    SellerWithProductImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SellerWithProductImg record);

    int updateByPrimaryKey(SellerWithProductImg record);

    List<SellerWithProductImg> selectByPid(Integer productid);
}
