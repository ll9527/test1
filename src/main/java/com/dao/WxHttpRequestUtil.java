package com.dao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.entity.AdminProfit;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.totalshop.WebConfig;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.UUID;

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
	
	public static String getQrCode(String ACCESS_TOKEN,String scene,Integer sel) throws ClientProtocolException, IOException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
//		String ACCESS_TOKEN = "20_zURtcVUFYx2I-PFFgQ1l6Be4c8CfYvph8ilyqDpRcZZ6BnFmYJvFywxT73WecFHYx0AolCiZVQn98vsX0QfcbDzXKtPRipKGYsdsXnOF7rjVHoHrMYONBg4QC-tHiJgrLuF19ymHF9QGdGAyRLVfACAWQA";
		String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+ACCESS_TOKEN;
		HttpPost httpPost = new HttpPost(url);
		JSONObject obj = new JSONObject();
//		obj.put("access_token", ACCESS_TOKEN);
		obj.put("scene", scene);
		obj.put("page", "pages/sellerManager/qrPay");
        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		String body = JSON.toJSONString(obj);
        StringEntity postEntity = new StringEntity(body);
        httpPost.addHeader("Content-Type", "application/json");
        postEntity.setContentType("image/png");
        httpPost.setEntity(postEntity);
 
        //设置请求器的配置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        httpPost.setConfig(requestConfig);
        
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        InputStream inputStream = entity.getContent();
        
        OutputStream outputStream = null;
        String realPath = WebConfig.realPath;
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 自定义的文件名称
        String uuid = UUID.randomUUID().toString();
        String trueFileName = sel+"sellerid"+uuid + "." + "png";
        // 设置存放图片文件的路径
        String path = realPath + trueFileName;
        try {
        	outputStream = new FileOutputStream(path);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("文件路径不存在");
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
//        String result = EntityUtils.toString(entity, "UTF-8");
        return trueFileName;
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
