package com.service;

import java.util.List;

import com.entity.ShopOrder;

public interface ShopOrderService {

	Integer vipPay(ShopOrder shopOrder);

	Integer setPayStatus(String orderSn, Integer status);

	Integer selectVipMoney();
	/**
	 * 用id查询订单
	 * @param id
	 * @return
	 */
	public ShopOrder selectByPrimaryKey(Integer id);
	/**
	 * 根据操作码查询用户或商户的所有订单
	 * @param status
	 * @param userId
	 * @param sellerId
	 * @return
	 */
	List selectAllByUserOrSeller(Integer status, Integer userId, Integer sellerId);
	/**
	 * 删除订单
	 * @param id
	 * @return
	 */
	public Integer closeOrder(Integer id);
	/**
	 * 插入数据返回主键
	 * @param shopOrder
	 * @return
	 */
	int insertSelectiveRetKey(ShopOrder shopOrder);
	/**
	 * 根据订单uuid查询订单
	 * @param string
	 * @return
	 */
	ShopOrder selectByOrderSn(String string);
	/**
	 * 更新数据
	 * @param shopOrder
	 * @return
	 */
	Integer updateByPrimaryKeySelective(ShopOrder shopOrder);
}
