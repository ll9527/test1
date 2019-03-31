package com.service;

import java.util.List;
import java.util.Map;

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
	/**
	 * 查询订单详情
	 * @param o
	 * @return
	 */
	Map<String, Object> selectOrderDetailsByid(Integer o);
	/**
	 * 用户确认收货
	 * @param id
	 * @param userId
	 * @param comment
	 * @return
	 */
	Map confirmG(Integer id, Integer userId, String comment);
	/**
	 * 查看用户或者商户所有订单
	 * @param userId
	 * @param sellerId
	 * @return
	 */
	List selectAllOrder(Integer userId, Integer sellerId);
	/**
	 * 商家确认退款
	 * @param o
	 * @param s
	 * @return
	 */
	Map refund(Integer o, Integer s);
}
