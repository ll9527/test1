package com.dao;

import java.util.List;

import com.entity.ShopOrderGroup;

public interface ShopOrderGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopOrderGroup record);

    int insertSelective(ShopOrderGroup record);

    ShopOrderGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopOrderGroup record);

    int updateByPrimaryKey(ShopOrderGroup record);
    /**
     * 用商品id查该商品的团购订单
     * @param p 商品id
     * @return
     */
	List<ShopOrderGroup> selectByPid(Integer p);
	/**
	 * 查没拼团成功的订单
	 * @return
	 */
	List<ShopOrderGroup> selectAllByTime();
}