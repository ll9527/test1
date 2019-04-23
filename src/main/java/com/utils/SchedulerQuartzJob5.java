package com.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.controller.OrderController;
import com.dao.ShopOrderGroupMapper;
import com.dao.ShopOrderMapper;
import com.entity.ShopOrder;
import com.entity.ShopOrderGroup;
import com.entity.User;
import com.service.SellerService;
import com.service.SellerVisitService;
import com.service.ShopOrderService;
import com.service.UserService;
import com.totalshop.TotalshopApplication;
@DisallowConcurrentExecution
/**
 * 作废超过24小时的团购订单
 * @author l
 *
 */
public class SchedulerQuartzJob5 implements Job {
	
	@Autowired
	private ShopOrderService shopOrderService;
	@Autowired
	private SellerVisitService sellerVisitService;
	@Autowired
	private UserService userService;
    private Logger logger = LoggerFactory.getLogger(TotalshopApplication.class);
    
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Job5开始："+new Date());
		userService.removeScore();
		shopOrderService.upBonus();
		userService.sendVipMoney();
		sellerVisitService.clearSellerVisit();
        logger.info("Job5结束："+new Date());
	}

}
