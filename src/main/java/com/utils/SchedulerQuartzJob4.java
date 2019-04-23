package com.utils;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.controller.OrderController;
import com.dao.AdminProfitMapper;
import com.dao.PayConfigure;
import com.dao.ShopOrderGroupMapper;
import com.dao.ShopOrderMapper;
import com.entity.AdminProfit;
import com.entity.ShopOrder;
import com.entity.ShopOrderGroup;
import com.entity.User;
import com.service.ShopOrderService;
import com.service.UserService;
import com.totalshop.TotalshopApplication;
@DisallowConcurrentExecution
/**
 * 每两小时获得微信access_token
 * @author l
 *
 */
public class SchedulerQuartzJob4 implements Job {
	
	@Autowired
	private AdminProfitMapper adminProfitMapper;
	
	//有效时间2小时 2 * 60 * 60 * 1000
    public static final long EFFTIVE_TIME = 7200000;
    private Logger logger = LoggerFactory.getLogger(TotalshopApplication.class);
    
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String APPID = PayConfigure.getAppID();
		String SECRET = PayConfigure.getSecret();
		String grant_type = "client_credential";
		
		String URL = "https://api.weixin.qq.com/cgi-bin/token?appid="+APPID
				+ "&secret="+SECRET
				+ "&grant_type="+grant_type;
        
		HttpGet httpGet = new HttpGet(URL);
 
 
        //设置请求器的配置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(30000).build();
        httpGet.setConfig(requestConfig);
        //创建客户端，用get访问微信给得url
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			//访问微信url返回的数据
			String result = EntityUtils.toString(entity, "UTF-8");
//        将字符串数据转成json再转成map
			JSONObject  jsonObject = JSONObject.parseObject(result);
			Map<String,Object> map = (Map<String,Object>)jsonObject;
//			把access_token存入数据库中
			AdminProfit adminProfit = adminProfitMapper.selectByPrimaryKey(1);
			adminProfit.setPercentClass(map.get("access_token").toString());
			adminProfitMapper.updateByPrimaryKeySelective(adminProfit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * 获取订单的下单时间和现在的时间差
     * @param shopOrder
     * @return
     */
    public Long checkOrder(ShopOrderGroup shopOrderGroup) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long diff = null;
        if (shopOrderGroup != null) {
            Date createTime = shopOrderGroup.getAddTime();
            try {
                diff = sdf.parse(sdf.format(date)).getTime() - sdf.parse(sdf.format(createTime)).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // 返回值为毫秒
        return diff;
    }
}
