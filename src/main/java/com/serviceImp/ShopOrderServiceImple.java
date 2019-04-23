package com.serviceImp;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.AdminProfitMapper;
import com.dao.CouponsMapper;
import com.dao.ProductCommentFreightMapper;
import com.dao.ReferrerMapper;
import com.dao.SellerMapper;
import com.dao.SellerWithProductImgMapper;
import com.dao.ShopOrderGoodsMapper;
import com.dao.ShopOrderGroupMapper;
import com.dao.ShopOrderMapper;
import com.dao.UserMapper;
import com.dao.WxHttpRequestUtil;
import com.entity.AdminProfit;
import com.entity.Coupons;
import com.entity.ProductCommentFreight;
import com.entity.Referrer;
import com.entity.Seller;
import com.entity.SellerWithProductImg;
import com.entity.ShopOrder;
import com.entity.ShopOrderGoods;
import com.entity.ShopOrderGroup;
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
	@Autowired(required = false)
	private SellerMapper sellerMapper;
	@Autowired(required = false)
	private SellerWithProductImgMapper sellerWithProductImgMapper;
	@Autowired(required = false)
	private CouponsMapper couponsMapper;
	@Autowired(required = false)
	private ProductCommentFreightMapper pcfm;
	@Autowired(required = false)
	private ReferrerMapper referrerMapper;
	@Autowired(required = false)
	private ShopOrderGroupMapper shopOrderGroupMapper;
	
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
		if(code==1 && code1==1) {
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
		if(shopOrder.getOrderStatus()==0) {
			shopOrder.setOrderStatus(-1);
			shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
			return 1;
		}
		return 0;
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

	@Override
	public Map<String, Object> selectOrderDetailsByid(Integer o) {
		Map<String, Object> map = new HashMap<String, Object>();
		ShopOrder shopOrder = shopOrderMapper.selectByPrimaryKey(o);
		List<ShopOrderGoods> shopOrderGoods = shopOrderGoodsMapper.selectByOId(o);
		List<Map<String, Object>> shopOrderGoodsAndSellerList = new ArrayList<>();
		for (ShopOrderGoods item : shopOrderGoods) {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			Integer sellerId = sellerWithProductImgMapper.selectByPId(item.getGoodsId()).getSellerId();
			Seller seller = sellerMapper.selectByPrimaryKey(sellerId);
			itemMap.put("seller", seller);
			itemMap.put("shopOrderGoods", item);
			shopOrderGoodsAndSellerList.add(itemMap);
		}
		Coupons coupons = couponsMapper.selectByOId(o);
		if(coupons != null) {
			map.put("coupons", coupons);
		}
		map.put("shopOrder", shopOrder);
		map.put("shopOrderGoodsAndSellerList", shopOrderGoodsAndSellerList);
		return map;
	}

	@Override
	public Map confirmG(Integer id, Integer userId, String comment) {
		Map map = new HashMap<>();
		ShopOrder shopOrder = shopOrderMapper.selectByPrimaryKey(id);
		if(shopOrder.getUserId() == userId) {
//			订单为待收货状态
			if(shopOrder.getOrderStatus() == 2) {
//				修改订单状态
				shopOrder.setOrderStatus(3);
				shopOrder.setEndTime(new Date());
				shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
//				插入评论
				ShopOrderGoods shopOrderGoods = shopOrderGoodsMapper.selectByOId(id).get(0);
				ProductCommentFreight pcf = new ProductCommentFreight();
				pcf.setProductId(shopOrderGoods.getGoodsId());
				pcf.setComment(comment);
				pcfm.insertSelective(pcf);
//				商家余额加入金额
				Seller se = sellerMapper.selectByPrimaryKey(shopOrder.getSellerId());
				User seller = userMapper.selectByPrimaryKey(se.getUserId());
//				平台扣点比例
				BigDecimal shopPercent = new BigDecimal(adminPM.selectAdminByVipMoney().getShopPercent().toString());
				BigDecimal money = shopOrder.getTotalMoney().subtract(shopOrder.getTotalMoney().multiply(shopPercent));
				seller.setMoney(seller.getMoney().add(money));
				userMapper.updateByPrimaryKeySelective(seller);
//				用户积分增加
				User user = userMapper.selectByPrimaryKey(shopOrder.getUserId());
				user.setScore(user.getScore()+shopOrder.getTotalMoney().intValue());
				userMapper.updateByPrimaryKeySelective(user);
//				推荐人积分增加
				Referrer ref = referrerMapper.selectByUId(user.getId());
				if(ref !=null) {
					User referrer = userMapper.selectByPrimaryKey(ref.getReferrerId());
					referrer.setScore(referrer.getScore()+shopOrder.getTotalMoney().intValue()/2);
					userMapper.updateByPrimaryKeySelective(referrer);
				}
				map.put("status", 200);
			}else {
				map.put("status", 400);
			}
		}else {
			map.put("status", 400);
		}
		return map;
	}

	@Override
	public List selectAllOrder(Integer userId, Integer sellerId) {
		List list = new ArrayList();
		// 获取所有订单
		List<ShopOrder> orderList = shopOrderMapper.selectAllOrder(userId, sellerId);
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
	public Map refund(Integer o, Integer s) {
		Map map = new HashMap();
		ShopOrder shopOrder = shopOrderMapper.selectByPrimaryKey(o);
		Seller se = sellerMapper.selectByPrimaryKey(s);
		User seller = userMapper.selectByPrimaryKey(se.getUserId());
		User user = userMapper.selectByPrimaryKey(shopOrder.getUserId());
//		更改user的总金额
		user.setMoney(user.getMoney().add(shopOrder.getTotalMoney()));
		int isok = userMapper.updateByPrimaryKeySelective(user);
//		改订单状态
		shopOrder.setOrderStatus(5);
		int isok2 = shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
		if(isok == 1 && isok2 == 1) {
			map.put("status", 200);
		}else {
			map.put("status", 400);
		}
		return map;
	}

	@Override
	public int insertSelectiveRetKey(ShopOrderGroup shopOrderGroup) {
		return shopOrderGroupMapper.insertSelective(shopOrderGroup);
	}

	@Override
	public List<ShopOrder> selectByGroupOid(Integer groupOid) {
		return shopOrderMapper.selectByGroupOid(groupOid);
	}

	@Override
	public List selectGroupByP(Integer p) {
		List list = new ArrayList<>();
//		用商品id查团购订单
		List<ShopOrderGroup> groupList = shopOrderGroupMapper.selectByPid(p);
		if(!groupList.isEmpty()) {
			for (ShopOrderGroup shopOrderGroup : groupList) {
				Map<String, Object> map = new HashMap<String, Object>();
				Integer orderNum = shopOrderMapper.selectByGroupOid(shopOrderGroup.getId()).size();
				if(orderNum > 0 && orderNum < 3) {
					map.put("shopOrderGroup", shopOrderGroup);
					map.put("orderNum", orderNum);
					list.add(map);
				}
			}
		}
		return list;
	}

	@Override
	public List selectGroupByU(Integer u) {
		List list = new ArrayList();
		// 获取所有订单
		List<ShopOrder> orderList = shopOrderMapper.selectGroupByU(u);
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
	public List<ShopOrder> selectAllOrderByTime() {
		return shopOrderMapper.selectAllOrderByTime();
	}

	@Override
	public ShopOrderGroup selectGroupByKey(Integer id) {
		return shopOrderGroupMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateGroupSelective(ShopOrderGroup shopOrderGroup) {
		return shopOrderGroupMapper.updateByPrimaryKeySelective(shopOrderGroup);
	}

	@Override
	public String getQrCode(Integer sel) {
		String ACCESS_TOKEN = adminPM.selectAdminByVipMoney().getPercentClass();
		String scene = sel.toString();
		try {
			return WxHttpRequestUtil.getQrCode(ACCESS_TOKEN, scene, sel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Integer upQrCodePay(String string) {
		ShopOrder so = shopOrderMapper.selectByOrderSn(string);
		so.setOrderStatus(9);
		so.setEndTime(new Date());
		Integer userId = sellerMapper.selectByPrimaryKey(so.getSellerId()).getUserId();
		User seller = userMapper.selectByPrimaryKey(userId);
//		平台扣点比例
		BigDecimal shopPercent = new BigDecimal(adminPM.selectAdminByVipMoney().getShopPercent().toString());
		BigDecimal money = so.getTotalMoney().subtract(so.getTotalMoney().multiply(shopPercent));
		seller.setMoney(seller.getMoney().add(money));
		
//		用户积分增加
		User user = userMapper.selectByPrimaryKey(so.getUserId());
		user.setScore(user.getScore()+so.getTotalMoney().intValue());
		userMapper.updateByPrimaryKeySelective(user);
//		推荐人积分增加
		Referrer ref = referrerMapper.selectByUId(user.getId());
		if(ref !=null) {
			User referrer = userMapper.selectByPrimaryKey(ref.getReferrerId());
			referrer.setScore(referrer.getScore()+so.getTotalMoney().intValue()/2);
			userMapper.updateByPrimaryKeySelective(referrer);
		}
		
		Integer code = shopOrderMapper.updateByPrimaryKeySelective(so);
		Integer code1 = userMapper.updateByPrimaryKeySelective(seller);
		if(code==1 && code1==1) {
			return 1;
		}
		return 0;
	}

	@Override
	public BigDecimal selectSales() {
		return shopOrderMapper.selectSales();
	}

	@Override
	public BigDecimal selectAllSales() {
		return shopOrderMapper.selectAllSales();
	}

	@Override
	public void upBonus() {
		BigDecimal money = shopOrderMapper.select7DaysBeforeMoney();
		AdminProfit ap = adminPM.selectAdminByVipMoney();
		if(money != null) {
			ap.setDiscountAmount(new BigDecimal(ap.getDiscountPersent().toString()).divide(new BigDecimal("100")).multiply(money));
			ap.setBonusPools(new BigDecimal(ap.getPoolsPersent().toString()).divide(new BigDecimal("100")).multiply(money));
		}else {
			ap.setDiscountAmount(new BigDecimal(0));
			ap.setBonusPools(new BigDecimal(0));
		}
		adminPM.updateByPrimaryKeySelective(ap);
	}

	@Override
	public Map getUserOrderNum(Integer userId) {
		Map map = new HashMap();
		int withTheDelivery = shopOrderMapper.selectAllByUserOrSeller(1, userId, null).size();
		int forTheGoods = shopOrderMapper.selectAllByUserOrSeller(2, userId, null).size();
		map.put("withTheDelivery", withTheDelivery);
		map.put("forTheGoods", forTheGoods);
		return map;
	}
	@Override
	public Map getSOrderNum(Integer sellerId) {
		Map map = new HashMap();
		int withTheDelivery = shopOrderMapper.selectAllByUserOrSeller(1, null, sellerId).size();
		int forTheGoods = shopOrderMapper.selectAllByUserOrSeller(4, null, sellerId).size();
		map.put("withTheDelivery", withTheDelivery);
		map.put("forTheGoods", forTheGoods);
		return map;
	}
}
