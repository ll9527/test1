package com.dao;

import java.util.List;

import com.entity.ShopOrder;

public interface ShopOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopOrder record);

    int insertSelective(ShopOrder record);

    int insertSelectiveRetKey(ShopOrder record);
    
    ShopOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopOrder record);

    int updateByPrimaryKey(ShopOrder record);

    ShopOrder selectByOrderSn(String orderSn);

	List<ShopOrder> selectAllByUserOrSeller(Integer status, Integer userId, Integer sellerId);
}