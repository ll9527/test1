package com.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.dao.WxHttpRequestUtil;
import com.dao.WxPaySignUtil;
import com.entity.Address;
import com.entity.Product;
import com.entity.Seller;
import com.entity.SellerWithProductImg;
import com.entity.Shop;
import com.entity.ShopOrder;
import com.entity.ShopOrderGoods;
import com.entity.ShopProductSpecification;
import com.entity.WxOrderInfo;
import com.entity.WxOrderReturnInfo;
import com.entity.WxSignInfo;
import com.service.ProductService;
import com.service.ShopOrderGoodsService;
import com.service.ShopOrderService;
import com.service.ShopService;
import com.service.UserService;
import com.thoughtworks.xstream.XStream;

@RestController
@RequestMapping("/order")
public class OrderController {
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
//	@RequestMapping("/test1")
//	public void test1() {
//		ShopOrder shopOrder = new ShopOrder();
//		shopOrder.setSellerName("haha");
//		System.out.println(som.insertSelectiveRetKey(shopOrder));
//		System.out.println(shopOrder.getId());
//	}
	
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
			System.out.println("完成统一下单返回得："+result);
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
        System.out.println("接收到的报文：" + notityXml);
        //将支付结果转成map
        Map map = WxPaySignUtil.doXMLParse(notityXml);
 
        String returnCode = (String) map.get("return_code");
        System.out.println(map);
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = WxPaySignUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = WxPaySignUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = WxPaySignUtil.sign(validStr, PayConfigure.getKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了
            System.out.println("sign="+sign);
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
            	//数据库获取金额
            	Integer vipMoney = shopOrderService.selectVipMoney();
            	if(vipMoney == Integer.valueOf((String) map.get("total_fee"))) {
                /**此处添加自己的业务逻辑代码start**/
                  // bla bla bla....
            		System.out.println("此处添加自己的业务逻辑代码start"+(String)map.get("out_trade_no"));
            		Integer upStatus = shopOrderService.setPayStatus((String)map.get("out_trade_no"),1);
            		if(upStatus==1) {
            			//更改状态成功
            			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            		}else {
            			System.out.println("收到钱，改状态失败");
            		}
                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
//-----------------------------------------------------            	
            	}else {
            		//数据库获取订单金额，用商户订单号修改该订单金额为(int)map.get("total_fee")
            	}
//-----------------------------------------------------            	
            } else {
                System.out.println("微信支付回调失败!签名不一致");
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");
 
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
	public Map ordPay(String jscode, Integer userid, Integer productid, 
			Integer num, String version, Integer isPick, Integer addressid, Integer skuId) {
		
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
			BigDecimal totalPrice = sku.getPrice().multiply(new BigDecimal(num));
			Map<String, Object> map = userService.selectFreightAndCoupons(userid,productid);
			if(isPick == 2) {
				// 2是快递
				totalPrice = totalPrice.add(new BigDecimal(String.valueOf(map.get("freight"))));
			}
			totalPrice = totalPrice.subtract(new BigDecimal(String.valueOf(map.get("couponsMoney"))));
			wxOrderInfo.setTotal_fee(totalPrice.intValue()*100);//金额
			wxOrderInfo.setSpbill_create_ip(PayConfigure.getSpbill_create_ip());
			wxOrderInfo.setNotify_url(PayConfigure.getOrd_notify_url());
			wxOrderInfo.setTrade_type(PayConfigure.getTrade_type());
			wxOrderInfo.setOpenid(openid);
			// 签名
			wxOrderInfo.setSign(WxPaySignUtil.getSign(wxOrderInfo));
			// 发送参数给 微信，完成统一下单
			String result = WxHttpRequestUtil.sendPost(PayConfigure.getUrl(), wxOrderInfo);
			System.out.println("完成统一下单返回得："+result);
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
				if(shopOrderService.insertSelectiveRetKey(shopOrder) == 1) {
//					if(som.insertSelective(shopOrder) == 1) {
					// 创建商品订单表
					ShopOrderGoods sog = new ShopOrderGoods();
					sog.setGoodsId(productid);
					sog.setUserId(userid);
					sog.setAddTime(shopOrder.getAddTime());
					sog.setGoodsName(pro.getTitle());
					sog.setIsGroup(0);
					sog.setPrice(sku.getPrice());
					sog.setGoNum(num);
					sog.setTotalPrice(totalPrice);
					sog.setOrderId(shopOrder.getId());
//					sog.setOrderId(1);
					sog.setpVersion(version);
					shopOrderGoodsService.insertSelective(sog);
					
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
        System.out.println("接收到的报文：" + notityXml);
        //将支付结果转成map
        Map map = WxPaySignUtil.doXMLParse(notityXml);
 
        String returnCode = (String) map.get("return_code");
        System.out.println(map);
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = WxPaySignUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = WxPaySignUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = WxPaySignUtil.sign(validStr, PayConfigure.getKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了
            System.out.println("sign="+sign);
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
            	//数据库获取订单对象
            	ShopOrder shopOrder = shopOrderService.selectByOrderSn((String)(map.get("out_trade_no")));
//            	订单的金额和微信返回的金额对比
            	if(shopOrder.getTotalMoney().intValue() == Integer.valueOf((String) map.get("total_fee"))) {
                /**此处添加自己的业务逻辑代码start**/
                  // bla bla bla....
//            		支付成功，将订单改成待发货状态
            		shopOrder.setOrderStatus(1);
            		Integer upStatus = shopOrderService.updateByPrimaryKeySelective(shopOrder);
            		System.out.println("此处添加自己的业务逻辑代码start"+(String)map.get("out_trade_no"));
            		if(upStatus==1) {
            			//更改状态成功
            			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            		}else {
            			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            			System.out.println("收到钱，改状态失败");
            		}
                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
//-----------------------------------------------------            	
            	}else {
            		//数据库获取订单金额，用商户订单号修改该订单金额为(int)map.get("total_fee")
            		shopOrder.setTotalMoney(new BigDecimal((String)map.get("total_fee")));
            		resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
            				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            		System.out.println("订单金额不一样");
            	}
//-----------------------------------------------------            	
            } else {
            	resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
        				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                System.out.println("微信支付回调失败!签名不一致");
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");
 
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
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
	
//	取消订单
	@RequestMapping("/closeOrder")
	public Integer closeOrder(ShopOrder shopOrder) {
		return shopOrderService.closeOrder(shopOrder.getId());
	}
	
//	商家确认发货
	@RequestMapping("/sendGoods")
	public Map sendGoods(ShopOrder shopOrder) {
		return null;
	}
	
//	根据状态码查询的订单
	@RequestMapping("/selectAllByUserOrSeller")
	public List selectAllByUserOrSeller(@Param("status")Integer status, @Param("userId")Integer userId,
			@Param("sellerId")Integer sellerId) {
		List list = shopOrderService.selectAllByUserOrSeller(status, userId, sellerId);
		return list;
	}
}


