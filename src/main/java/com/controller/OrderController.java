package com.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.AddressMapper;
import com.dao.PayConfigure;
import com.dao.SellerMapper;
import com.dao.SellerWithProductImgMapper;
import com.dao.ShopOrderGoodsMapper;
import com.dao.ShopOrderMapper;
import com.dao.ShopProductSpecificationMapper;
import com.dao.UserMapper;
import com.dao.WxHttpRequestUtil;
import com.dao.WxPaySignUtil;
import com.entity.Address;
import com.entity.Coupons;
import com.entity.Product;
import com.entity.Seller;
import com.entity.SellerWithProductImg;
import com.entity.Shop;
import com.entity.ShopOrder;
import com.entity.ShopOrderGoods;
import com.entity.ShopOrderGroup;
import com.entity.ShopProductSpecification;
import com.entity.User;
import com.entity.WxOrderInfo;
import com.entity.WxOrderReturnInfo;
import com.entity.WxSignInfo;
import com.mysql.fabric.xmlrpc.base.Data;
import com.service.ProductService;
import com.service.ShopOrderGoodsService;
import com.service.ShopOrderService;
import com.service.ShopService;
import com.service.UserService;
import com.thoughtworks.xstream.XStream;
import com.totalshop.TotalshopApplication;

@RestController
@RequestMapping("/order")
public class OrderController {
	protected static final Logger logger = LoggerFactory.getLogger(TotalshopApplication.class);
	@Autowired
	ShopOrderService shopOrderService;
	@Autowired
	ProductService productService;
	@Autowired
	ShopService shopService;
	@Autowired
	UserService userService;
//	@Autowired
//	private ShopOrderMapper som;
	@Autowired
	private ShopOrderGoodsService shopOrderGoodsService;
//	@Autowired
//	private ShopOrderGoodsMapper sogm;
	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private SellerWithProductImgMapper swpm;
	@Autowired
	private SellerMapper sellerMapper;
	@Autowired
	private ShopProductSpecificationMapper spsm;
	
	/**
	 * 测试接口
	 */
	@RequestMapping("/test1")
	public void test1(String a,Integer b) {
		if(b == null) {
			System.out.println(a);
		}
	}
	
	/**
	 * vip统一下单接口
	 * @return
	 */
	@RequestMapping("/vipPay")
	public Map vipPay(String jscode, Integer userid) {
		
		try {
//			用jscode 请求微信url获得openid
			Map msgMap = WxHttpRequestUtil.getOpenid(jscode);
			String openid = (String)msgMap.get("openid");
			if(openid == null) {
//				拿不到openid
				return null;
			}
			WxOrderInfo wxOrderInfo = new WxOrderInfo();
			wxOrderInfo.setAppid(PayConfigure.getAppID());
			wxOrderInfo.setMch_id(PayConfigure.getMch_id());
			wxOrderInfo.setNonce_str(UUID.randomUUID().toString().trim().replaceAll("-", ""));
			wxOrderInfo.setSign_type("MD5");
			wxOrderInfo.setBody("共店-会员");// 商品名称
			wxOrderInfo.setOut_trade_no(UUID.randomUUID().toString().trim().replaceAll("-", ""));
			// 从数据库获得开通会员金额
			Integer vipMoney = shopOrderService.selectVipMoney();
			
			wxOrderInfo.setTotal_fee(vipMoney);//金额
			wxOrderInfo.setSpbill_create_ip(PayConfigure.getSpbill_create_ip());
			wxOrderInfo.setNotify_url(PayConfigure.getNotify_url());
			wxOrderInfo.setTrade_type(PayConfigure.getTrade_type());
			wxOrderInfo.setOpenid(openid);
			// 签名
			wxOrderInfo.setSign(WxPaySignUtil.getSign(wxOrderInfo));
			// 发送参数给 微信，完成统一下单
			String result = WxHttpRequestUtil.sendPost(PayConfigure.getUrl(), wxOrderInfo);
			logger.info("完成统一下单返回得："+result);
			XStream xStream = new XStream();
			// 将微信得xml返回值转回对象
			xStream.alias("xml", WxOrderReturnInfo.class);
			WxOrderReturnInfo returnInfo = (WxOrderReturnInfo) xStream.fromXML(result);

			// 二次签名
			Map payInfo = new HashMap();
			if ("SUCCESS".equals(returnInfo.getReturn_code())
					&& returnInfo.getReturn_code().equals(returnInfo.getResult_code())) {
				WxSignInfo signInfo = new WxSignInfo();
				signInfo.setAppId(PayConfigure.getAppID());
				long time = System.currentTimeMillis() / 1000;
				signInfo.setTimeStamp(String.valueOf(time));
				signInfo.setNonceStr(UUID.randomUUID().toString().trim().replaceAll("-", ""));
				signInfo.setPrepay_id("prepay_id=" + returnInfo.getPrepay_id());
				signInfo.setSignType("MD5");
				// 生成签名
				String sign1 = WxPaySignUtil.getSign(signInfo);
				payInfo.put("timeStamp", signInfo.getTimeStamp());
				payInfo.put("nonceStr", signInfo.getNonceStr());
				payInfo.put("package", signInfo.getPrepay_id());
				payInfo.put("signType", signInfo.getSignType());
				payInfo.put("paySign", sign1);
				payInfo.put("status", 200);
				payInfo.put("msg", "统一下单成功!");
//-----------------------------------------------------------------
				// 此处可以写唤起支付前的业务逻辑
//					创建订单
				ShopOrder shopOrder = new ShopOrder();
				shopOrder.setOrderSn(wxOrderInfo.getOut_trade_no());
				shopOrder.setAddTime(new Date());
				shopOrder.setUserId(userid);
				shopOrder.setTotalMoney((new BigDecimal(wxOrderInfo.getTotal_fee()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
				shopOrder.setMark("会员费用");
				shopOrder.setOrderStatus(0);
				if(shopOrderService.vipPay(shopOrder) != null) {
					return payInfo;
				}
				return null;
				// 业务逻辑结束
//-----------------------------------------------------------------
				
			}
//			payInfo.put("status", 500);
//			payInfo.put("msg", "统一下单失败!");
			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * vip支付成功回调接口
	 */
	@RequestMapping("/payCallback")
	public void payCallback(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        logger.info("接收到的报文：" + notityXml);
        //将支付结果转成map
        Map map = WxPaySignUtil.doXMLParse(notityXml);
 
        String returnCode = (String) map.get("return_code");
        logger.info(map.toString());
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = WxPaySignUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = WxPaySignUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = WxPaySignUtil.sign(validStr, PayConfigure.getKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了
            logger.info("sign="+sign);
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
            	//数据库获取金额
            	int vipMoney = shopOrderService.selectVipMoney();
//            	logger.info("vipMoney="+vipMoney+"total_fee="+Integer.valueOf((String) map.get("total_fee")));
            	if(vipMoney == Integer.valueOf((String) map.get("total_fee"))) {
                /**此处添加自己的业务逻辑代码start**/
                  // bla bla bla....
            		logger.info("此处添加自己的业务逻辑代码start"+(String)map.get("out_trade_no"));
//            		更改订单状态，更改用户成会员
            		Integer upStatus = shopOrderService.setPayStatus((String)map.get("out_trade_no"),1);
            		if(upStatus==1) {
            			//更改状态成功
            			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            		}else {
            			logger.info("收到钱，改状态失败");
            		}
                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
//-----------------------------------------------------            	
            	}else {
            		//数据库获取订单金额，用商户订单号修改该订单金额为(int)map.get("total_fee")
            	}
//-----------------------------------------------------            	
            } else {
            	logger.info("微信支付回调失败!签名不一致");
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        logger.info(resXml);
        logger.info("微信支付回调数据结束");
 
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
	
	/**
	 * 商品原价统一下单接口
	 * @return
	 */
	@RequestMapping("/ordPay")
	public Map ordPay(String jscode, Integer userid, Integer productid, Integer isGroup,
			Integer num, String version, Integer isPick, Integer addressid, Integer skuId, Integer groupOid) {
		
		try {
//			用jscode 请求微信url获得openid
			Map msgMap = WxHttpRequestUtil.getOpenid(jscode);
			String openid = (String)msgMap.get("openid");
			if(openid == null) {
//				拿不到openid
				return null;
			}
			//用商品id查询商品
			Product pro = productService.selectByPrimaryKey(productid);
			//组装统一下单数据
			WxOrderInfo wxOrderInfo = new WxOrderInfo();
			wxOrderInfo.setAppid(PayConfigure.getAppID());
			wxOrderInfo.setMch_id(PayConfigure.getMch_id());
			wxOrderInfo.setNonce_str(UUID.randomUUID().toString().trim().replaceAll("-", ""));
			wxOrderInfo.setSign_type("MD5");
			
			Shop shopClass = shopService.selectByPrimaryKey(pro.getCid());
			wxOrderInfo.setBody("共店-"+shopClass.getClassName());// 商品
			// 数据库的订单号
			wxOrderInfo.setOut_trade_no(UUID.randomUUID().toString().trim().replaceAll("-", ""));
//			sku
			ShopProductSpecification sku = spsm.selectByPrimaryKey(skuId);
			// 商品总价
			BigDecimal totalPrice;
			if(isGroup == 1) {
				totalPrice = sku.getGroupPrice().multiply(new BigDecimal(num));
			}else {
				totalPrice = sku.getPrice().multiply(new BigDecimal(num));
			}
//			获取运费和优惠券金额
			Map<String, Object> map = userService.selectFreightAndCoupons(userid,productid);
			if(isPick == 2) {
				// 2是快递
				totalPrice = totalPrice.add(new BigDecimal(String.valueOf(map.get("freight"))));
			}
			totalPrice = totalPrice.subtract(new BigDecimal(String.valueOf(map.get("couponsMoney"))));
			wxOrderInfo.setTotal_fee(totalPrice.multiply(new BigDecimal("100")).intValue());//金额
			wxOrderInfo.setSpbill_create_ip(PayConfigure.getSpbill_create_ip());
			wxOrderInfo.setNotify_url(PayConfigure.getOrd_notify_url());
			wxOrderInfo.setTrade_type(PayConfigure.getTrade_type());
			wxOrderInfo.setOpenid(openid);
			// 签名
			wxOrderInfo.setSign(WxPaySignUtil.getSign(wxOrderInfo));
			// 发送参数给 微信，完成统一下单
			String result = WxHttpRequestUtil.sendPost(PayConfigure.getUrl(), wxOrderInfo);
			logger.info("完成统一下单返回得："+result);
			XStream xStream = new XStream();
			// 将微信得xml返回值转回对象
			xStream.alias("xml", WxOrderReturnInfo.class);
			WxOrderReturnInfo returnInfo = (WxOrderReturnInfo) xStream.fromXML(result);

			// 二次签名
			Map payInfo = new HashMap();
			if ("SUCCESS".equals(returnInfo.getReturn_code())
					&& returnInfo.getReturn_code().equals(returnInfo.getResult_code())) {
				WxSignInfo signInfo = new WxSignInfo();
				signInfo.setAppId(PayConfigure.getAppID());
				long time = System.currentTimeMillis() / 1000;
				signInfo.setTimeStamp(String.valueOf(time));
				signInfo.setNonceStr(UUID.randomUUID().toString().trim().replaceAll("-", ""));
				signInfo.setPrepay_id("prepay_id=" + returnInfo.getPrepay_id());
				signInfo.setSignType("MD5");
				// 生成签名
				String sign1 = WxPaySignUtil.getSign(signInfo);
				payInfo.put("timeStamp", signInfo.getTimeStamp());
				payInfo.put("nonceStr", signInfo.getNonceStr());
				payInfo.put("package", signInfo.getPrepay_id());
				payInfo.put("signType", signInfo.getSignType());
				payInfo.put("paySign", sign1);
				payInfo.put("status", 200);
				payInfo.put("msg", "统一下单成功!");
//-----------------------------------------------------------------
				// 此处可以写唤起支付前的业务逻辑
				
//					创建订单
				ShopOrder shopOrder = new ShopOrder();
				shopOrder.setOrderSn(wxOrderInfo.getOut_trade_no());
				shopOrder.setAddTime(new Date());
				shopOrder.setIsGroup(isGroup);
				

				// 收货地址
				Address address = addressMapper.selectByPrimaryKey(addressid);
				shopOrder.setUserAddress(address.getAddress());
				shopOrder.setUserName(address.getUserName());
				shopOrder.setTel(Long.valueOf(address.getTel()));
				shopOrder.setUserId(userid);
				//用商品id查询商家
				Integer sellerId = swpm.selectByPId(productid).getSellerId();
				shopOrder.setSellerId(sellerId);
				Seller seller = sellerMapper.selectByPrimaryKey(sellerId);
				shopOrder.setSellerName(seller.getTitleName());
				
				
				shopOrder.setTotalMoney((new BigDecimal(wxOrderInfo.getTotal_fee()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
				shopOrder.setOrderStatus(0);
				shopOrder.setIsPick(isPick);
				shopOrder.setPrepay_id(signInfo.getPrepay_id());
				
				if(isGroup == 1) {
//					如果拼团订单的id为空则代表自己开团，创建拼团订单
					if(groupOid != -1) {
						ShopOrderGroup shopOrderGroup = shopOrderService.selectGroupByKey(groupOid);
						if(shopOrderGroup.getStatus() == -1) {
							return null;
						}
//						拼团订单不为空，检查该订单是否有三个锁定订单
						Integer itemNum = shopOrderService.selectByGroupOid(groupOid).size();
						if(itemNum > 0 && itemNum < 3) {
							shopOrder.setGroup_oid(groupOid);
							shopOrder.setOrderStatus(7);
						}else {
							return null;
//							作废该订单
//							ShopOrderGroup shopOrderGroup = new ShopOrderGroup();
//							shopOrderGroup.setAddTime(new Date());
//							shopOrderService.insertSelectiveRetKey(shopOrderGroup);
//							shopOrder.setGroup_oid(shopOrderGroup.getId());
//							shopOrder.setOrderStatus(-1);
						}
					}else {
//						自己开团，创建拼团订单
						ShopOrderGroup shopOrderGroup = new ShopOrderGroup();
						shopOrderGroup.setAddTime(new Date());
						shopOrderGroup.setProductId(productid);
						shopOrderGroup.setStatus(0);
						shopOrderService.insertSelectiveRetKey(shopOrderGroup);
						shopOrder.setGroup_oid(shopOrderGroup.getId());
						shopOrder.setOrderStatus(7);
					}
				}
				if(shopOrderService.insertSelectiveRetKey(shopOrder) == 1) {
//					if(som.insertSelective(shopOrder) == 1) {
					// 创建商品订单表
					ShopOrderGoods sog = new ShopOrderGoods();
					sog.setGoodsId(productid);
					sog.setUserId(userid);
					sog.setAddTime(shopOrder.getAddTime());
					sog.setGoodsName(pro.getTitle());
					sog.setIsGroup(isGroup);
					if(isGroup == 1) {
						sog.setPrice(sku.getGroupPrice());
					}else {
						sog.setPrice(sku.getPrice());
					}
					sog.setGoNum(num);
					sog.setTotalPrice(totalPrice);
					sog.setOrderId(shopOrder.getId());
					sog.setpVersion(version);
					shopOrderGoodsService.insertSelective(sog);
//					更改优惠券的状态
					if(map.get("coupons") != null) {
						Coupons coupons = (Coupons)map.get("coupons");
						coupons.setOnDelete(1);
						coupons.setOrderId(shopOrder.getId());
						userService.updateCoupons(coupons);
					}
					return payInfo;
				}
				return null;
				// 业务逻辑结束
//-----------------------------------------------------------------
				
			}
//			payInfo.put("status", 500);
//			payInfo.put("msg", "统一下单失败!");
			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 商品原价支付成功回调接口
	 */
	@RequestMapping("/pricePayCallback")
	public void pricePayCallback(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        logger.info("接收到的报文：" + notityXml);
        //将支付结果转成map
        Map map = WxPaySignUtil.doXMLParse(notityXml);
 
        String returnCode = (String) map.get("return_code");
        logger.info(map.toString());
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = WxPaySignUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = WxPaySignUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = WxPaySignUtil.sign(validStr, PayConfigure.getKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了
            logger.info("sign="+sign);
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
            	//数据库获取订单对象
            	ShopOrder shopOrder = shopOrderService.selectByOrderSn((String)(map.get("out_trade_no")));
//            	订单的金额和微信返回的金额对比
            	if(shopOrder.getTotalMoney().multiply(new BigDecimal("100")).intValue() == Integer.valueOf((String) map.get("total_fee"))) {
                /**此处添加自己的业务逻辑代码start**/
                  // bla bla bla....
//            		支付成功，将订单改成待发货状态
            		if(shopOrder.getIsGroup() == 1) {
            			ShopOrderGroup shopOrderGroup = shopOrderService.selectGroupByKey(shopOrder.getGroup_oid());
            			if(shopOrderGroup.getStatus() == -1) {
//            				如果团购父订单是作废的状态
//            				则退款给该用户
//            				直接执行退款的方法   开始
            				User user = userService.selectByPrimaryKey(shopOrder.getUserId());
            				user.setMoney(user.getMoney().add(shopOrder.getTotalMoney()));
            				userService.updateByPrimaryKeySelective(user);
//            				直接执行退款的方法   结束
//            				改订单状态
            				shopOrder.setOrderStatus(6);
            				shopOrderService.updateByPrimaryKeySelective(shopOrder);
            				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            				BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            		        out.write(resXml.getBytes());
            		        out.flush();
            		        out.close();
            		        return;
            			}
//            			如果是订单是团购订单，则锁定
            			shopOrder.setOrderStatus(8);
//            			如果团购订单满足3个人，将所有订单的状态改成1 待发货
            			List<ShopOrder> orderList = shopOrderService.selectByGroupOid(shopOrder.getGroup_oid());
            			if(orderList.size() >= 2) {
            				shopOrder.setOrderStatus(1);
//            				ShopOrderGroup shopOrderGroup = shopOrderService.selectGroupByKey(shopOrder.getGroup_oid());
//            				该拼团父订单的状态为成功
            				shopOrderGroup.setStatus(1);
            				shopOrderService.updateGroupSelective(shopOrderGroup);
            				for (ShopOrder item : orderList) {
								item.setOrderStatus(1);
								shopOrderService.updateByPrimaryKeySelective(item);
							}
            			}
            		}else {
            			shopOrder.setOrderStatus(1);
            		}
            		Integer upStatus = shopOrderService.updateByPrimaryKeySelective(shopOrder);
            		logger.info("此处添加自己的业务逻辑代码start"+(String)map.get("out_trade_no"));
            		if(upStatus==1) {
            			//更改状态成功
            			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            		}else {
            			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            			logger.info("收到钱，改状态失败");
            		}
                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
//-----------------------------------------------------            	
            	}else {
            		//数据库获取订单金额，用商户订单号修改该订单金额为(int)map.get("total_fee")
            		shopOrder.setTotalMoney(new BigDecimal((String)map.get("total_fee")));
            		shopOrderService.updateByPrimaryKeySelective(shopOrder);
            		resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
            				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            		
            		logger.info("订单金额不一样");
            	}
//-----------------------------------------------------            	
            } else {
            	resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
        				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            	logger.info("微信支付回调失败!签名不一致");
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        logger.info(resXml);
        logger.info("微信支付回调数据结束");
 
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
//	继续支付
	@RequestMapping("/againPay")
	public Map againPay(ShopOrder shopOrder){
		ShopOrder order = shopOrderService.selectByPrimaryKey(shopOrder.getId());
		Map payInfo = new HashMap();
		if(order.getPrepay_id() == null && order.getPrepay_id() == "") {
			payInfo.put("status", 400);
			return payInfo;
		}
		WxSignInfo signInfo = new WxSignInfo();
		signInfo.setAppId(PayConfigure.getAppID());
		long time = System.currentTimeMillis() / 1000;
		signInfo.setTimeStamp(String.valueOf(time));
		signInfo.setNonceStr(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		signInfo.setPrepay_id(order.getPrepay_id());
		signInfo.setSignType("MD5");
		// 生成签名
		String sign1;
		try {
			sign1 = WxPaySignUtil.getSign(signInfo);
			payInfo.put("timeStamp", signInfo.getTimeStamp());
			payInfo.put("nonceStr", signInfo.getNonceStr());
			payInfo.put("package", signInfo.getPrepay_id());
			payInfo.put("signType", signInfo.getSignType());
			payInfo.put("paySign", sign1);
			payInfo.put("status", 200);
			return payInfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			payInfo.put("status", 400);
			return payInfo;
		}
	}
	/**
	 * 二维码付款
	 * @param jscode
	 * @param userid
	 * @param sel
	 * @param mon
	 * @return
	 */
	@RequestMapping("/qrPay")
	public Map qrPay(String jscode, Integer userid, Integer sel, String mon) {
		
		try {
//			用jscode 请求微信url获得openid
			Map msgMap = WxHttpRequestUtil.getOpenid(jscode);
			String openid = (String)msgMap.get("openid");
			if(openid == null) {
//				拿不到openid
				return null;
			}
			WxOrderInfo wxOrderInfo = new WxOrderInfo();
			wxOrderInfo.setAppid(PayConfigure.getAppID());
			wxOrderInfo.setMch_id(PayConfigure.getMch_id());
			wxOrderInfo.setNonce_str(UUID.randomUUID().toString().trim().replaceAll("-", ""));
			wxOrderInfo.setSign_type("MD5");
			wxOrderInfo.setBody("共店-充值");// 商品名称
			wxOrderInfo.setOut_trade_no(UUID.randomUUID().toString().trim().replaceAll("-", ""));
			// 从数据库获得开通会员金额
			Integer vipMoney = new BigDecimal(mon).multiply(new BigDecimal(100)).intValue();
			
			wxOrderInfo.setTotal_fee(vipMoney);//金额
			wxOrderInfo.setSpbill_create_ip(PayConfigure.getSpbill_create_ip());
			wxOrderInfo.setNotify_url(PayConfigure.getQr_notify_url());
			wxOrderInfo.setTrade_type(PayConfigure.getTrade_type());
			wxOrderInfo.setOpenid(openid);
			// 签名
			wxOrderInfo.setSign(WxPaySignUtil.getSign(wxOrderInfo));
			// 发送参数给 微信，完成统一下单
			String result = WxHttpRequestUtil.sendPost(PayConfigure.getUrl(), wxOrderInfo);
			logger.info("完成统一下单返回得："+result);
			XStream xStream = new XStream();
			// 将微信得xml返回值转回对象
			xStream.alias("xml", WxOrderReturnInfo.class);
			WxOrderReturnInfo returnInfo = (WxOrderReturnInfo) xStream.fromXML(result);

			// 二次签名
			Map payInfo = new HashMap();
			if ("SUCCESS".equals(returnInfo.getReturn_code())
					&& returnInfo.getReturn_code().equals(returnInfo.getResult_code())) {
				WxSignInfo signInfo = new WxSignInfo();
				signInfo.setAppId(PayConfigure.getAppID());
				long time = System.currentTimeMillis() / 1000;
				signInfo.setTimeStamp(String.valueOf(time));
				signInfo.setNonceStr(UUID.randomUUID().toString().trim().replaceAll("-", ""));
				signInfo.setPrepay_id("prepay_id=" + returnInfo.getPrepay_id());
				signInfo.setSignType("MD5");
				// 生成签名
				String sign1 = WxPaySignUtil.getSign(signInfo);
				payInfo.put("timeStamp", signInfo.getTimeStamp());
				payInfo.put("nonceStr", signInfo.getNonceStr());
				payInfo.put("package", signInfo.getPrepay_id());
				payInfo.put("signType", signInfo.getSignType());
				payInfo.put("paySign", sign1);
				payInfo.put("status", 200);
				payInfo.put("msg", "统一下单成功!");
//-----------------------------------------------------------------
				// 此处可以写唤起支付前的业务逻辑
//					创建订单
				ShopOrder shopOrder = new ShopOrder();
				shopOrder.setOrderSn(wxOrderInfo.getOut_trade_no());
				shopOrder.setAddTime(new Date());
				shopOrder.setUserId(userid);
				shopOrder.setSellerId(sel);
				shopOrder.setPrepay_id(signInfo.getPrepay_id());
				shopOrder.setTotalMoney((new BigDecimal(wxOrderInfo.getTotal_fee()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
				shopOrder.setMark("二维码付款");
				shopOrder.setOrderStatus(0);
				if(shopOrderService.insertSelectiveRetKey(shopOrder) > 0) {
					return payInfo;
				}
				return null;
				// 业务逻辑结束
//-----------------------------------------------------------------
			}
//			payInfo.put("status", 500);
//			payInfo.put("msg", "统一下单失败!");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	二维码付款成功回调方法
	@RequestMapping("/qrPayCallback")
	public void qrPayCallback(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        logger.info("接收到的报文：" + notityXml);
        //将支付结果转成map
        Map map = WxPaySignUtil.doXMLParse(notityXml);
 
        String returnCode = (String) map.get("return_code");
        logger.info(map.toString());
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = WxPaySignUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = WxPaySignUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = WxPaySignUtil.sign(validStr, PayConfigure.getKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了
            logger.info("sign="+sign);
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
            	//数据库获取金额
            	ShopOrder shopOrder = shopOrderService.selectByOrderSn((String)(map.get("out_trade_no")));
            	if(shopOrder.getTotalMoney().multiply(new BigDecimal("100")).intValue() == Integer.valueOf((String) map.get("total_fee"))) {
                /**此处添加自己的业务逻辑代码start**/
                  // bla bla bla....
            		logger.info("此处添加自己的业务逻辑代码start"+(String)map.get("out_trade_no"));
//            		更改订单状态，把金额给商家
            		Integer upStatus = shopOrderService.upQrCodePay((String)map.get("out_trade_no"));
            		if(upStatus==1) {
            			//更改状态成功
            			logger.info("收款成功，改状态成功");
            			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            		}else {
            			logger.info("收到钱，改状态失败");
            			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            		}
                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
//-----------------------------------------------------            	
            	}else {
            		//数据库获取订单金额，用商户订单号修改该订单金额为(int)map.get("total_fee")
            	}
//-----------------------------------------------------            	
            } else {
            	logger.info("微信支付回调失败!签名不一致");
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        logger.info(resXml);
        logger.info("微信支付回调数据结束");
 
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
	
//	取消订单
	@RequestMapping("/closeOrder")
	public Integer closeOrder(ShopOrder shopOrder) {
		return shopOrderService.closeOrder(shopOrder.getId());
	}
	
//	待发货状态取消订单和退款
	@RequestMapping("/orderRefund")
	public Map<String, Object> orderRefund(ShopOrder shopOrder, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer isOK = 0;
		ShopOrder so = shopOrderService.selectByPrimaryKey(shopOrder.getId());
		if(so.getOrderStatus() == 1) {
//			直接执行退款的方法   开始
			User user = userService.selectByPrimaryKey(so.getUserId());
			user.setMoney(user.getMoney().add(so.getTotalMoney()));
			userService.updateByPrimaryKeySelective(user);
//			直接执行退款的方法   结束
//			改订单状态
			so.setOrderStatus(6);
			isOK = shopOrderService.updateByPrimaryKeySelective(so);
		}else if(so.getOrderStatus() == 2) {
//			退货退款订单
			so.setOrderStatus(4);
			isOK = shopOrderService.updateByPrimaryKeySelective(so);
		}
		if(isOK == 1) {
			map.put("status", 200);
		}else {
			map.put("status", 400);
		}
		return map;
	}
	
//	用户确认收货
	@RequestMapping("/confirmG")
	public Map confirmG(ShopOrder shopOrder, String comment) {
		return shopOrderService.confirmG(shopOrder.getId(), shopOrder.getUserId(), comment);
	}
	
//	商家确认发货
	@RequestMapping("/sendGoods")
	public Map sendGoods(Integer o, String sn) {
		Map<String, Object> map = new HashMap<String, Object>();
		ShopOrder shopOrder = shopOrderService.selectByPrimaryKey(o);
		if(shopOrder.getOrderStatus() == 1) {
//			改订单状态为已发货
			shopOrder.setOrderStatus(2);
			shopOrder.setExpressSn(sn);
			Integer isOK = shopOrderService.updateByPrimaryKeySelective(shopOrder);
			if(isOK == 1) {
				map.put("status", 200);
			}else {
				map.put("status", 400);
			}
		}
		return map;
	}
	
//	商家确认退款
	@RequestMapping("/refund")
	public Map refund(Integer o, Integer s) {
		Map map = shopOrderService.refund(o, s);
		return map;
	}
	
//	根据状态码查询的订单
	@RequestMapping("/selectAllByUserOrSeller")
	public List selectAllByUserOrSeller(Integer status, Integer userId,
			Integer sellerId) {
		List list = shopOrderService.selectAllByUserOrSeller(status, userId, sellerId);
		return list;
	}
	
//	查看用户或者商户所有订单
	@RequestMapping("/selectAllOrder")
	public List selectAllOrder(Integer userId, Integer sellerId) {
		List list = shopOrderService.selectAllOrder(userId, sellerId);
		return list;
	}
	
//	查询订单详情
	@RequestMapping("/selectOrderDetailsByid")
	public Map<String, Object> selectOrderDetailsByid(Integer o) {
		Map<String, Object> map = shopOrderService.selectOrderDetailsByid(o);
		return map;
	}
	
//	查询商品id查询所有该商品团购订单
	@RequestMapping("/selectGroupByP")
	public List selectGroupByP(Integer p) {
		return shopOrderService.selectGroupByP(p);
	}
	
//	用用户id查该用户的已付款团购订单
	@RequestMapping("/selectGroupByU")
	public List selectGroupByU(Integer u) {
		return shopOrderService.selectGroupByU(u);
	}
	
//	获得商家二维码
	@RequestMapping("/getQrCode")
	public String getQrCode(Integer sel) {
		return shopOrderService.getQrCode(sel);
	}
//	获取用户的代发货，待收货的订单数量
	@RequestMapping("/getUserOrderNum")
	public Map getUserOrderNum(Integer uuuuu) {
		return shopOrderService.getUserOrderNum(uuuuu);
	}
//	获取用户的代发货，待收货的订单数量
	@RequestMapping("/getSOrderNum")
	public Map getSOrderNum(Integer sssssss) {
		return shopOrderService.getSOrderNum(sssssss);
	}
}


