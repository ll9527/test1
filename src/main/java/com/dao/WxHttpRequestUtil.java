package com.dao;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;


 
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

public class WxHttpRequestUtil {
	//连接超时时间，默认10秒
    private static final int socketTimeout = 10000;
 
    //传输超时时间，默认30秒
    private static final int connectTimeout = 30000;
	/**
	 * post请求
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws KeyManagementException 
	 * @throws UnrecoverableKeyException 
	 */
	public static String sendPost(String url, Object xmlObj) throws ClientProtocolException, IOException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
		
 
        
		HttpPost httpPost = new HttpPost(url);
		//解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStreamForRequestPostData.alias("xml", xmlObj.getClass());
        //将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
 
        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);
 
        //设置请求器的配置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        httpPost.setConfig(requestConfig);
        
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        return result;
	}
	/**
	 * 获得小程序得openid和session_key
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws KeyManagementException 
	 * @throws UnrecoverableKeyException 
	 */
	public static Map getOpenid(String jscode) throws ClientProtocolException, IOException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
		
		String APPID = PayConfigure.getAppID();
		String SECRET = PayConfigure.getSecret();
		String JSCODE = jscode;
		
		String URL = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID
				+ "&secret="+SECRET
				+ "&js_code="+JSCODE
				+ "&grant_type=authorization_code";
        
		HttpGet httpGet = new HttpGet(URL);
 
 
        //设置请求器的配置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        httpGet.setConfig(requestConfig);
        //创建客户端，用get访问微信给得url
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        //或者微信url返回得数据
        String result = EntityUtils.toString(entity, "UTF-8");
//        将字符串数据转成json再转成map
        JSONObject  jsonObject = JSONObject.parseObject(result);
        Map<String,Object> map = (Map<String,Object>)jsonObject;
        return map;
	}
	/**
	 * 自定义证书管理器，信任所有证书
	 * @author pc
	 *
	 */
	public static class MyX509TrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
 
		}
		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
      }
}
