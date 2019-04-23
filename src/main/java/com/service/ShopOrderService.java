package com.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.entity.ShopOrder;
import com.entity.ShopOrderGroup;

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

	int insertSelectiveRetKey(ShopOrderGroup shopOrderGroup);
	/**
	 * 用团购订单单号查询该订单下的所有子订单
	 * @param groupOid
	 * @return
	 */
	List<ShopOrder> selectByGroupOid(Integer groupOid);
	/**
	 * 用商品id查询所有该商品团购订单
	 * @param p 商品id
	 * @return
	 */
	List selectGroupByP(Integer p);
	/**
	 * 用用户id查该用户的已付款团购订单
	 * @param u
	 * @return
	 */
	List selectGroupByU(Integer u);
	/**
	 * 查看超过15天 没确认收货的订单
	 * @return
	 */
	List<ShopOrder> selectAllOrderByTime();
	/**
	 * 用团购订单id查询团购订单
	 * @param group_oid
	 * @return
	 */
	ShopOrderGroup selectGroupByKey(Integer id);
	/**
	 * 更新团购父订单
	 * @param shopOrderGroup
	 * @return
	 */
	int updateGroupSelective(ShopOrderGroup shopOrderGroup);
	/**
	 * 获得商家二维码
	 * @param sel
	 * @return
	 */
	String getQrCode(Integer sel);
	/**
	 * 二维码付款成功更改状态
	 * @param string
	 * @return
	 */
	Integer upQrCodePay(String string);
	/**
	 * 今日销量
	 * @return
	 */
	BigDecimal selectSales();
	/**
	 * 总销量
	 * @return
	 */
	BigDecimal selectAllSales();
	/**
	 * 更新奖金
	 */
	void upBonus();
	/**
	 * 获取用户的代发货，待收货的订单数量
	 * @param userId
	 * @return
	 */
	Map getUserOrderNum(Integer userId);
	/**
	 * 获取商家的代发货，待收货的订单数量
	 * @param userId
	 * @return
	 */
	Map getSOrderNum(Integer sssssss);
}
