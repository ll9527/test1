package com.utils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SchedulerQuartzJob2 implements Job {

	private void before(){
        System.out.println("Job2任务开始执行");
    }
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		before();
        System.out.println("Job2开始："+System.currentTimeMillis());
        // TODO 业务
        System.out.println("Job2结束："+System.currentTimeMillis());
        after();
	}

	private void after(){
        System.out.println("Job2任务开始执行after");
    }
}
