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
import com.service.ShopOrderService;
import com.service.UserService;
import com.totalshop.TotalshopApplication;
@DisallowConcurrentExecution
/**
 * 作废超过24小时的团购订单
 * @author l
 *
 */
public class SchedulerQuartzJob3 implements Job {
	
	@Autowired
	private OrderController orderController;
	@Autowired
	private ShopOrderService shopOrderService;
	@Autowired
	private ShopOrderGroupMapper shopOrderGroupMapper;
	@Autowired
	private UserService userService;
	//订单有效时间1天1 * 24 * 60 * 60 * 1000
    public static final long EFFTIVE_TIME = 86280000;
    private Logger logger = LoggerFactory.getLogger(TotalshopApplication.class);
    
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Queue<ShopOrderGroup> queue = new LinkedList<>();
		logger.info("Job3开始："+new Date());
//        获得过期的团购父订单
		List<ShopOrderGroup> shopOrderGroupList = shopOrderGroupMapper.selectAllByTime();
		if(!shopOrderGroupList.isEmpty()) {
			for (ShopOrderGroup shopOrderGroup : shopOrderGroupList) {
				queue.offer(shopOrderGroup);
			}
		}
		// 获取队列的头元素,开始检测头订单是否失效
		ShopOrderGroup element = queue.peek();
        while (element != null) {
            //时间差值
            Long diff = this.checkOrder(element);
            if (diff != null && diff >= EFFTIVE_TIME) {
            	logger.info("Job3s");
            	logger.info("开始关闭订单:" + element.getId() + "下单时间:" + element.getAddTime());
            	ShopOrderGroup shopOrderGroup = shopOrderGroupMapper.selectByPrimaryKey(element.getId());
            	if(shopOrderGroup.getStatus() == 0) {
            		shopOrderGroup.setStatus(-1);
            		shopOrderGroupMapper.updateByPrimaryKeySelective(shopOrderGroup);
					List<ShopOrder> shopOrderList = shopOrderService.selectByGroupOid(shopOrderGroup.getId());
					for (ShopOrder shopOrder : shopOrderList) {
	//					直接执行退款的方法   开始
	    				User user = userService.selectByPrimaryKey(shopOrder.getUserId());
	    				user.setMoney(user.getMoney().add(shopOrder.getTotalMoney()));
	    				userService.updateByPrimaryKeySelective(user);
	//    				直接执行退款的方法   结束
	//    				改订单状态
	    				shopOrder.setOrderStatus(6);
	    				shopOrderService.updateByPrimaryKeySelective(shopOrder);
					}
            	}
                // 弹出队列
                queue.poll();
                // 取下一个元素
                element = queue.peek();
            } else if (diff < EFFTIVE_TIME) {
                try {
                	logger.info("等待检测订单" + element.getId() + "下单时间" + element.getAddTime() + "已下单"
                            + diff / 1000 + "秒");
                    //线程等待
                    Thread.sleep(EFFTIVE_TIME - diff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    logger.info("拼团订单过期定时任务出现问题");
                }
            }
        }
				
				
		
        logger.info("Job3结束："+new Date());
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
