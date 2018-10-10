package com.roadjava.schedule.instance.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.roadjava.schedule.beans.Instance;
import com.roadjava.schedule.beans.InstanceStatus;
import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.component.ActionThread;
import com.roadjava.schedule.listeners.SpringBeanFactory;
import com.roadjava.schedule.utils.AppConst;
import com.roadjava.schedule.utils.ScheduleManager;
/*
 * triggerFired
vetoJobExecution
jobToBeExecuted
testjobMon Sep 10 22:25:45 CST 2018
jobWasExecuted
triggerComplete
 */

public class InstanceTaskListener implements JobListener{
	private static Logger logger=LoggerFactory.getLogger(InstanceTaskListener.class);
	private String name;
	
	public InstanceTaskListener(){	}
	public InstanceTaskListener(String name){
		this.name=name;
	}
	@Override
	public String getName() {
		return "InstanceTaskListener"+name;
	}
	//Scheduler 在 JobDetail 将要被执行时调用这个方法。
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		System.out.println("jobToBeExecuted");
	}
	private void setCrtExecInstanceStatus(JobExecutionContext context,String status) {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Map<String, Instance> allActionMap = (Map<String, Instance>) jobDataMap.get("allActionMap");
		String instanceId = jobDetail.getKey().getName();
		Instance instance = allActionMap.get(instanceId);
		instance.setStatus(status);
		//持久化到db
		ThreadPoolTaskExecutor threadPoolExecutor = SpringBeanFactory.getBean("executor", ThreadPoolTaskExecutor.class);
		threadPoolExecutor.execute(new ActionThread(Long.valueOf(instanceId), status));
	}
	//Scheduler 在 JobDetail 即将被执行，但又被 TriggerListener 否决了时调用这个方法。
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		System.out.println("jobExecutionVetoed");
		setCrtExecInstanceStatus(context,InstanceStatus.SUSPEND.name());
	}
	//Scheduler 在 JobDetail 被执行之后调用这个方法。
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		System.out.println("jobWasExecuted");
	}

}
