package com.roadjava.schedule.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.roadjava.schedule.beans.Instance;
import com.roadjava.schedule.beans.InstanceStatus;
import com.roadjava.schedule.beans.InstanceType;
import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.instance.handler.InstanceHandler;
import com.roadjava.schedule.instance.handler.InstanceTaskListener;
import com.roadjava.schedule.instance.handler.InstanceTriggerListener;

@Component
public class ScheduleManager {
	private static Logger logger=LoggerFactory.getLogger(ScheduleManager.class);
	@Resource(name="scheduler")
  	private Scheduler scheduler;  //调度器：需要job和trigger才能调度任务
  /*
     增加一个任务
   */
  public  void addInstance(TaskInfo task) throws  Exception{
      //job
      String taskId = String.valueOf(task.getId());
	  JobDetail jobDetail= JobBuilder.newJob(InstanceHandler.class).withIdentity(
			taskId,AppConst.DEFAULT_INS_GROUP).build();//组名随便写
      //设置job运行需要用到的参数
      JobDataMap jobDataMap = jobDetail.getJobDataMap();
      jobDataMap.put("taskId", taskId);
      jobDataMap.put("cron", task.getCron());
      jobDataMap.put("parentIds", task.getParentIds());
      //trigger
      CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron());
      CronTrigger trigger=TriggerBuilder.newTrigger().
              withIdentity(AppConst.CRON_ID_PREFIX+taskId,AppConst.DEFAULT_CRON_GROUP)
              .withSchedule(cronScheduleBuilder).build();
      
      
      //任务分配并启动
      scheduler.scheduleJob(jobDetail,trigger);//TriggerState:NONE--NORMAL
  }
  //修改任务的调度规则方法一
   public  void updateInstance(TaskInfo task) throws  Exception{
	   String taskId=String.valueOf(task.getId());
       TriggerKey triggerKey = TriggerKey.triggerKey(AppConst.CRON_ID_PREFIX+taskId, AppConst.DEFAULT_CRON_GROUP);
       
       //NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
       //triggerKey没有与jobDetail关联，获取不到为NONE;正常调度为NORMAL
       TriggerState triggerState = scheduler.getTriggerState(triggerKey);
       System.out.println(triggerState);
       System.out.println(triggerState.name());
       
       CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
       String cronExpression = trigger.getCronExpression();
       if (!cronExpression.equals(task.getCron())){//cron表达式不相等
    	   //重新设置jobDetail的参数
    	   JobKey jobKey = JobKey.jobKey(taskId, AppConst.DEFAULT_INS_GROUP);
    	   JobDataMap jobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
    	   jobDataMap.put("taskId", taskId);
	       jobDataMap.put("cron", task.getCron());
	       jobDataMap.put("parentIds", task.getParentIds());
    	   //设置trigger
           CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron());
           CronTrigger newTrigger=TriggerBuilder.newTrigger().
        		   withIdentity(AppConst.CRON_ID_PREFIX+taskId,AppConst.DEFAULT_CRON_GROUP)
                   .withSchedule(cronScheduleBuilder).build();
           //修改原来与某个trigger相关的任务的触发trigger
            scheduler.rescheduleJob(triggerKey,newTrigger);
       }
   }

    //修改任务的调度规则方法二
    public  void updateInstance2(TaskInfo task) throws  Exception{
    	String taskId=String.valueOf(task.getId());
        TriggerKey triggerKey = TriggerKey.triggerKey(AppConst.CRON_ID_PREFIX+taskId, AppConst.DEFAULT_CRON_GROUP);
        CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        String cronExpression = trigger.getCronExpression();
        if (!cronExpression.equals(task.getCron())){//cron表达式不相等
            //删除该任务
        	removeInstance(task);
            //再新增
            addInstance(task);
        }
    }

    //删除一个任务:先删除trigger，再删除与trigger关联的job
    public  void removeInstance(TaskInfo task) throws  Exception{
    	String taskId=String.valueOf(task.getId());
       TriggerKey triggerKey = TriggerKey.triggerKey(AppConst.CRON_ID_PREFIX+taskId, AppConst.DEFAULT_CRON_GROUP);
       
       scheduler.pauseTrigger(triggerKey);//暂停触发器 TriggerState：NORMAL--PAUSED
       
       scheduler.unscheduleJob(triggerKey);//TriggerState:PAUSED--NONE
       JobKey jobKey = JobKey.jobKey(taskId, AppConst.DEFAULT_INS_GROUP);
       scheduler.deleteJob(jobKey);
    }
    
    public  void addInstance(Instance instance, Map<String, Instance> allActionMap){
    	//job
    	String id = String.valueOf(instance.getId());
    	JobDetail jobDetail= JobBuilder.newJob(InstanceHandler.class).withIdentity(
    			id,AppConst.DEFAULT_INS_GROUP).build();//组名随便写
    	//trigger
    	Trigger trigger=null;
    	if (StringUtils.isNotBlank(instance.getCron())) {//instance task_id=0时没有
        	trigger=TriggerBuilder.newTrigger().
        			withIdentity(AppConst.CRON_ID_PREFIX+id,AppConst.DEFAULT_CRON_GROUP)
        			.withSchedule(CronScheduleBuilder.cronSchedule(instance.getCron()))
        			.build();
		}else {
			trigger=TriggerBuilder.newTrigger().
        			withIdentity(AppConst.CRON_ID_PREFIX+id,AppConst.DEFAULT_CRON_GROUP)
        			.startNow()
        			.build();
		}
    	
    	
    	//设置job运行需要用到的参数
    	JobDataMap jobDataMap = jobDetail.getJobDataMap();
    	Date fireTimeAfter = trigger.getFireTimeAfter(new Date());
    	if (fireTimeAfter!=null) {//cron表达式不会再触发了就为null
    		instance.setNextFireTime(fireTimeAfter);
    		jobDataMap.put("crtAction", instance);
    		jobDataMap.put("allActionMap", allActionMap);
    		List<Instance> allSon=getAllSon(instance,allActionMap);
    		jobDataMap.put("allSon", allSon);
    		
    		//任务分配并启动
    		try {
    			KeyMatcher<JobKey> jobMatcher = KeyMatcher.keyEquals(jobDetail.getKey());
    			scheduler.getListenerManager().addJobListener(new InstanceTaskListener(id), jobMatcher);
    			
    			KeyMatcher<TriggerKey> triggerMatcher = KeyMatcher.keyEquals(trigger.getKey());
    			scheduler.getListenerManager().addTriggerListener(new InstanceTriggerListener(id),triggerMatcher);
    			scheduler.scheduleJob(jobDetail,trigger);//TriggerState:NONE--NORMAL
    		} catch (Exception e) {
    			logger.error("调度加入实例出错:",e);
    		}
		}else{
			instance.setStatus(InstanceStatus.INACTIVE.name());
		}
    }
    private List<Instance> getAllSon(Instance instance, Map<String, Instance> allActionMap) {
    	List<Instance> allSon=new ArrayList<>();
		String sonIds = instance.getSonIds();
		if (StringUtils.isNotBlank(sonIds)) {
			String[] sonIdsArr = sonIds.split(",");
			for(String sonId:sonIdsArr){
				allSon.add(allActionMap.get(sonId));
			}
			return allSon;
		}
		return null;
	}
	//修改任务的调度规则方法一
    public  void updateInstance(Instance instance) throws  Exception{
    	String id=String.valueOf(instance.getId());
    	TriggerKey triggerKey = TriggerKey.triggerKey(AppConst.CRON_ID_PREFIX+id, AppConst.DEFAULT_CRON_GROUP);
    	
    	//NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
    	//triggerKey没有与jobDetail关联，获取不到为NONE;正常调度为NORMAL
    	TriggerState triggerState = scheduler.getTriggerState(triggerKey);
    	System.out.println(triggerState);
    	System.out.println(triggerState.name());
    	
    	CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
    	String cronExpression = trigger.getCronExpression();
    	if (!cronExpression.equals(instance.getCron())){//cron表达式不相等
    		//重新设置jobDetail的参数
    		JobKey jobKey = JobKey.jobKey(id, AppConst.DEFAULT_INS_GROUP);
    		JobDataMap jobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
    		jobDataMap.put("taskId", id);
    		jobDataMap.put("cron", instance.getCron());
    		jobDataMap.put("parentIds", instance.getParentIds());
    		//设置trigger
    		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(instance.getCron());
    		CronTrigger newTrigger=TriggerBuilder.newTrigger().
    				withIdentity(AppConst.CRON_ID_PREFIX+id,AppConst.DEFAULT_CRON_GROUP)
    				.withSchedule(cronScheduleBuilder).build();
    		//修改原来与某个trigger相关的任务的触发trigger
    		scheduler.rescheduleJob(triggerKey,newTrigger);
    	}
    }
    
    //修改任务的调度规则方法二
    public  void updateInstance2(Instance instance) throws  Exception{
    	String id=String.valueOf(instance.getId());
    	TriggerKey triggerKey = TriggerKey.triggerKey(AppConst.CRON_ID_PREFIX+id, AppConst.DEFAULT_CRON_GROUP);
    	CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
    	String cronExpression = trigger.getCronExpression();
    	if (!cronExpression.equals(instance.getCron())){//cron表达式不相等
    		//删除该任务
    		removeInstance(instance);
    		//再新增
    		addInstance(instance,null);
    	}
    }
    
    //删除一个任务:先删除trigger，再删除与trigger关联的job
    public  void removeInstance(Instance instance) throws  Exception{
    	String id=String.valueOf(instance.getId());
    	TriggerKey triggerKey = TriggerKey.triggerKey(AppConst.CRON_ID_PREFIX+id, AppConst.DEFAULT_CRON_GROUP);
    	
    	scheduler.pauseTrigger(triggerKey);//暂停触发器 TriggerState：NORMAL--PAUSED
    	
    	scheduler.unscheduleJob(triggerKey);//TriggerState:PAUSED--NONE
    	JobKey jobKey = JobKey.jobKey(id, AppConst.DEFAULT_INS_GROUP);
    	scheduler.deleteJob(jobKey);
    }
}
