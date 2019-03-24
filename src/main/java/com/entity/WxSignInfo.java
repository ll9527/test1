package com.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 签名实体类
 * @author l
 *
 */
public class WxSignInfo {
	 
		private String appId;//小程序ID	
		private String timeStamp;//时间戳	
		private String nonceStr;//随机串	
		@XStreamAlias("package")
		private String prepay_id;//统一下单接口返回的 prepay_id 参数值
		private String signType;//签名方式
		
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public String getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getNonceStr() {
			return nonceStr;
		}
		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}
		public String getPrepay_id() {
			return prepay_id;
		}
		public void setPrepay_id(String prepay_id) {
			this.prepay_id = prepay_id;
		}
		public String getSignType() {
			return signType;
		}
		public void setSignType(String signType) {
			this.signType = signType;
		}
		
		
		
}
