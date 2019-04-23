package com.dao;

public class PayConfigure {
		// 商户支付秘钥
		private static String key = "yiciyuan88YICIYUAN88yiciyuan8832";
		//商户号
		private static String mch_id = "1524977831";
		// 小程序的secret
		private static String secret = "b24036ed2a54f376fe06b9ff1eb02656";
		//小程序ID	
		private static String appID = "wxc9a34122a017efdc";
		//终端IP	
		private static String spbill_create_ip = "127.0.0.1";
		//交易类型
		private static String trade_type = "JSAPI";
		//通知地址	，vip支付的回调地址
		private static String notify_url = "https://gongshop.com.cn/order/payCallback";
		//订单支付的回调地址
		private static String ord_notify_url = "https://gongshop.com.cn/order/pricePayCallback";
		//订单支付的回调地址
		private static String qr_notify_url = "https://gongshop.com.cn/order/qrPayCallback";
		//统一下单API接口链接
	    private static String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
	    

		public static String getQr_notify_url() {
			return qr_notify_url;
		}

		public static void setQr_notify_url(String qr_notify_url) {
			PayConfigure.qr_notify_url = qr_notify_url;
		}

		public static String getOrd_notify_url() {
			return ord_notify_url;
		}

		public static void setOrd_notify_url(String ord_notify_url) {
			PayConfigure.ord_notify_url = ord_notify_url;
		}

		public static String getUrl() {
			return url;
		}

		public static void setUrl(String url) {
			PayConfigure.url = url;
		}

		public static String getTrade_type() {
			return trade_type;
		}

		public static void setTrade_type(String trade_type) {
			PayConfigure.trade_type = trade_type;
		}

		public static String getNotify_url() {
			return notify_url;
		}

		public static void setNotify_url(String notify_url) {
			PayConfigure.notify_url = notify_url;
		}

		public static String getSpbill_create_ip() {
			return spbill_create_ip;
		}

		public static void setSpbill_create_ip(String spbill_create_ip) {
			PayConfigure.spbill_create_ip = spbill_create_ip;
		}

		public static String getSecret() {
			return secret;
		}
	 
		public static void setSecret(String secret) {
			PayConfigure.secret = secret;
		}
	 
		public static String getKey() {
			return key;
		}
	 
		public static void setKey(String key) {
			PayConfigure.key = key;
		}
	 
		public static String getAppID() {
			return appID;
		}
	 
		public static void setAppID(String appID) {
			PayConfigure.appID = appID;
		}
	 
		public static String getMch_id() {
			return mch_id;
		}
	 
		public static void setMch_id(String mch_id) {
			PayConfigure.mch_id = mch_id;
		}

}
