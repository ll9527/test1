package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.ShopOrderMapper;
import com.entity.ShopOrder;
import com.service.ShopOrderService;

@DisallowConcurrentExecution
public class SchedulerQuartzJob1 implements Job {
	
	@Autowired
	private ShopOrderMapper shopOrderMapper;
	//订单有效时间15分钟15 * 60 * 1000
    public static final long EFFTIVE_TIME = 960000;
//    private Logger logger = Logger.getLogger(SchedulerQuartzJob1.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("失效订单检测任务开始执行!");
        Queue<ShopOrder> queue = new LinkedList<>();

        // 在每次启动Job时去数据库查找失效订单,并加入到队列中(从数据库中查询，此处使用假数据)
        List<ShopOrder> list = shopOrderMapper.selectByWaitPay();
        if (!list.isEmpty()) {
            for (ShopOrder o : list) {
                queue.offer(o);
            }
        }
        // 获取队列的头元素,开始检测头订单是否失效
        ShopOrder element = queue.peek();
        while (element != null) {
            //时间差值
            Long diff = this.checkOrder(element);
            if (diff != null && diff >= EFFTIVE_TIME) {
                System.out.println("开始关闭订单" + element.getId() + "下单时间" + element.getAddTime());
                ShopOrder shopOrder = shopOrderMapper.selectByPrimaryKey(element.getId());
                if(shopOrder.getOrderStatus() == 0) {
                	shopOrder.setOrderStatus(-1);
//                更改订单状态
                	shopOrderMapper.updateByPrimaryKeySelective(shopOrder);
                }
                // 弹出队列
                queue.poll();
                // 取下一个元素
                element = queue.peek();
            } else if (diff < EFFTIVE_TIME) {
                try {
                    System.out.println("等待检测订单" + element.getId() + "下单时间" + element.getAddTime() + "已下单"
                            + diff / 1000 + "秒");
                    //线程等待
                    Thread.sleep(EFFTIVE_TIME - diff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("订单过期定时任务出现问题");
                }
            }
        }
	}
	
    /**
     * 获取订单的下单时间和现在的时间差
     * 
     * @param shopOrder
     * @return
     *
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
