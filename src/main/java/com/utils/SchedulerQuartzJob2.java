package com.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.controller.OrderController;
import com.dao.ShopOrderMapper;
import com.entity.ShopOrder;
import com.service.ShopOrderService;
import com.totalshop.TotalshopApplication;
@DisallowConcurrentExecution
/**
 * 15天自动确认收货。
 */
public class SchedulerQuartzJob2 implements Job {
	
	@Autowired
	private OrderController orderController;
	@Autowired
	private ShopOrderService shopOrderService;
	//订单有效时间15天15 * 24 * 60 * 60 * 1000
    public static final long EFFTIVE_TIME = 1295100000;
    private Logger logger = LoggerFactory.getLogger(TotalshopApplication.class);
    
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Job2开始："+new Date());
        
        List<ShopOrder> orderList = shopOrderService.selectAllOrderByTime();
        if(orderList.isEmpty()) {
        	return;
        }
        for (ShopOrder shopOrder : orderList) {
        	Long diff = this.checkOrder(shopOrder);
        	if(diff != null && diff >= EFFTIVE_TIME) {
        		shopOrderService.confirmG(shopOrder.getId(), shopOrder.getUserId(), "用户未作出评价");
        	}
		}
        logger.info("Job2结束："+new Date());
	}
	
	/**
     * 获取订单的下单时间和现在的时间差
     * @param shopOrder
     * @return
     */
    public Long checkOrder(ShopOrder shopOrder) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long diff = null;
        if (shopOrder != null) {
            Date createTime = shopOrder.getAddTime();
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
