package com.roadjava.schedule.instance.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import com.roadjava.schedule.beans.Instance;

/*
 */
public class InstanceHandler implements Job {
    @Override
    public void execute(JobExecutionContext context) {
       JobDetail jobDetail = context.getJobDetail();
	   JobDataMap jobDataMap = jobDetail.getJobDataMap();
	   Instance ins= (Instance) jobDataMap.get("crtAction");
	   Map<String, Instance> allActionMap= (Map<String, Instance>) jobDataMap.get("allActionMap");
	   List<Instance> allSon= (List<Instance>) jobDataMap.get("allSon");
       try {
    	   System.out.println("当前执行实例:"+ins.getId()+"正在执行逻辑...");
    	   Random random=new Random();
    	   int nextInt = random.nextInt(5);
		 Thread.sleep(1000*60*nextInt);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//       printInfo(context, jobDetail);
    }

	private void printInfo(JobExecutionContext context, JobDetail jobDetail) {
		//打印一些参数信息
		   CronTrigger trigger= (CronTrigger) context.getTrigger();
		   System.out.println("-----jobDetail--------");
		   //true:不允许该jobDetail并发执行
		   System.out.println(jobDetail.isConcurrentExectionDisallowed());
		   //未关联trigger的jobDetail是否移除调度
		   System.out.println(jobDetail.isDurable());
		   System.out.println(jobDetail.isPersistJobDataAfterExecution());
		   System.out.println("-----trigger--------");
		   System.out.println(trigger.getCronExpression());
		   /*
		    *  cron:0/10  44  13  9  9 ? 2018
		    *  summary:
				seconds: 0,10,20,30,40,50
				minutes: 44
				hours: 13
				daysOfMonth: 9
				months: 9
				daysOfWeek: ?
				lastdayOfWeek: false
				nearestWeekday: false
				NthDayOfWeek: 0
				lastdayOfMonth: false
				years: 2018
		    */
		   System.out.println(trigger.getExpressionSummary());
		   System.out.println(trigger.getMisfireInstruction());//空白
		   System.out.println(trigger.getPriority());
		   System.out.println(trigger.mayFireAgain());//还会执行则为true，不会再执行了则为false
		   System.out.println(trigger.getEndTime());//总是返回null
		   System.out.println(trigger.getFinalFireTime());//总是返回null
		   System.out.println(trigger.getFireTimeAfter(new Date()));//下次触发事件，>传入的日期
		   System.out.println(trigger.getNextFireTime());//下次触发时间
		   System.out.println(trigger.getPreviousFireTime());//上次触发时间
		   System.out.println(trigger.getStartTime());//该jobDetail加入调度的时间
		   System.out.println(trigger.getTimeZone());
	}
}
