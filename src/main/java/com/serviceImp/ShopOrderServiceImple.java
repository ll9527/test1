package com.serviceImp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.AdminProfitMapper;
import com.dao.ShopOrderGoodsMapper;
import com.dao.ShopOrderMapper;
import com.dao.UserMapper;
import com.entity.AdminProfit;
import com.entity.ShopOrder;
import com.entity.ShopOrderGoods;
import com.entity.User;
import com.service.ShopOrderService;

@Service
@Transactional
public class ShopOrderServiceImple implements ShopOrderService{
	@Autowired(required = false)
	private ShopOrderMapper shopOrderMapper;
	@Autowired(required = false)
	private ShopOrderGoodsMapper shopOrderGoodsMapper;
	@Autowired(required = false)
	private UserMapper userMapper;
	@Autowired(required = false)
	private AdminProfitMapper adminPM;
	
	@Override
	public Integer vipPay(ShopOrder shopOrder) {
		// TODO Auto-generated method stub
		if(shopOrderMapper.insertSelective(shopOrder)>0) {
			return 1;
		}
		return null;
	}

	@Override
	public Integer setPayStatus(String orderSn, Integer status) {
		ShopOrder so = shopOrderMapper.selectByOrderSn(orderSn);
		so.setOrderStatus(status);
		User user = userMapper.selectByPrimaryKey(so.getUserId());
		user.setIsVip(1);
		Integer code = shopOrderMapper.updateByPrimaryKeySelective(so);
		Integer code1 = userMapper.updateByPrimaryKeySelective(user);
		if(code==1 && code==1) {
			return 1;
		}
		return 0;
	}

	@Override
	public Integer selectVipMoney() {
		AdminProfit adminProfit = adminPM.selectAdminByVipMoney();
		Integer vipMoney = adminProfit.getVipMoney().multiply(new BigDecimal(100)).intValue();
		return vipMoney;
	}
//	根据操作码查询所有订单
	@Override
	public List selectAllByUserOrSeller(Integer status, Integer userId, Integer sellerId) {
		List list = new ArrayList();
		// 获取所有订单
		List<ShopOrder> orderList = shopOrderMapper.selectAllByUserOrSeller(status, userId, sellerId);
		for (ShopOrder shopOrder : orderList) {
			List<ShopOrderGoods> shopOrderGoodsList = shopOrderGoodsMapper.selectByOId(shopOrder.getId());
			Map map = new HashMap();
			map.put("shopOrder", shopOrder);
			map.put("shopOrderGoodsList", shopOrderGoodsList);
			list.add(map);
		}
		return list;
	}
	
	@Override
	public ShopOrder selectByPrimaryKey(Integer id) {
		return shopOrderMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public Integer closeOrder(Integer id) {
		ShopOrder shopOrder = shopOrderMapper.selectByPrimaryKey(id);
		shopOrder.setOrderStatus(-1);
		shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
		return 1;
	}

	@Override
	public int insertSelectiveRetKey(ShopOrder shopOrder) {
		return shopOrderMapper.insertSelectiveRetKey(shopOrder);
	}

	@Override
	public ShopOrder selectByOrderSn(String string) {
		return shopOrderMapper.selectByOrderSn(string);
	}

	@Override
	public Integer updateByPrimaryKeySelective(ShopOrder shopOrder) {
		return shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
	}
}
