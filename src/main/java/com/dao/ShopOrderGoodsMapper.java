package com.dao;

import java.util.List;

import com.entity.ShopOrderGoods;

public interface ShopOrderGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopOrderGoods record);

    int insertSelective(ShopOrderGoods record);

    ShopOrderGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopOrderGoods record);

    int updateByPrimaryKey(ShopOrderGoods record);

	List<ShopOrderGoods> selectByOId(Integer id);
}