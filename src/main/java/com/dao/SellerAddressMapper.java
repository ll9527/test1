package com.dao;

import com.entity.SellerAddress;

import java.util.List;

public interface SellerAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerAddress record);

    int insertSelective(SellerAddress record);

    SellerAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SellerAddress record);

    int updateByPrimaryKey(SellerAddress record);

    List<SellerAddress> selectByUserId(Integer sellerid);

    int deleteBySellerId(Integer sellerid);

    List<SellerAddress> selectBySellerid(Integer sellerid);
}