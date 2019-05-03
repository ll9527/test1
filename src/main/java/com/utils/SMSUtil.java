package com.utils;

import java.io.IOException;

import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

/**
 * 验证码工具类
 * @author l
 *
 */
public class SMSUtil {
	// 短信应用 SDK AppID
	static int appid = 1400205096; // 1400开头
	// 短信应用 SDK AppKey
	static String appkey = "d048f7be6181a55751979b3002e5e653";
	// 短信模板 ID，需要在短信应用中申请
	static int templateId = 322414; // NOTE: 这里的模板 ID`7839`只是一个示例，真实的模板 ID 需要在短信控制台中申请
	// templateId7839对应的内容是"您的验证码是: {1}"
	// 签名
	static String smsSign = "共店小程序"; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台申请。
	
	/**
	 * 发送随机4位验证码
	 * @param tel
	 */
	public static String sMSCode(String tel) {
    	// 需要发送短信的手机号码
    	String[] phoneNumbers = {tel};
    	try {
    		String  randomNum = (int)((Math.random()*9+1)*1000)+"";
    	    String[] params = {randomNum,"60"};// 数组具体的元素个数和模板中变量个数必须一致，例如示例中 templateId:5678 对应一个变量，参数数组中元素个数也必须是一个
    	    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
    	    SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0],
    	        templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
//    	    System.out.println(result);
    	    return randomNum;
    	} catch (HTTPException e) {
    	    // HTTP 响应码错误
    	    e.printStackTrace();
    	} catch (JSONException e) {
    	    // JSON 解析错误
    	    e.printStackTrace();
    	} catch (IOException e) {
    	    // 网络 IO 错误
    	    e.printStackTrace();
    	}
		return null;
	}
}
