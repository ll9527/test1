package com.dao;

import java.math.BigDecimal;
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
	/**
	 * 查询所有未付款订单
	 * @return
	 */
	List<ShopOrder> selectByWaitPay();
	/**
	 * 查询用户或者商户的所有订单
	 * @param userId
	 * @param sellerId
	 * @return
	 */
	List<ShopOrder> selectAllOrder(Integer userId, Integer sellerId);
	/**
	 * 用团购订单单号查询该订单下的所有子订单
	 * @param groupOid
	 * @return
	 */
	List<ShopOrder> selectByGroupOid(Integer groupOid);
	/**
	 * 用用户id查该用户的已付款团购订单
	 * @param u
	 * @return
	 */
	List<ShopOrder> selectGroupByU(Integer u);
	/**
	 * 查看超过15天的订单
	 * @return
	 */
	List<ShopOrder> selectAllOrderByTime();
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
	 * 查看前第七天的成交金额
	 * @return
	 */
	BigDecimal select7DaysBeforeMoney();
	/**
	 * 用userid查最后的订单
	 * @param id
	 * @return
	 */
	List<ShopOrder> selectUserLastOrder(Integer id);
}