package lzzschedule;

import java.lang.reflect.Field;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import com.google.common.base.CaseFormat;
import com.roadjava.schedule.beans.BaseBean;
import com.roadjava.schedule.beans.Instance;
import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.instance.handler.InstanceHandler;
import com.roadjava.schedule.instance.handler.InstanceTaskListener;
import com.roadjava.schedule.instance.handler.InstanceTriggerListener;

public class Test {
	public static void main(String[] args) throws Exception{
//		System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "test_abc"));
//		System.out.println(Test.class.getSuperclass());
//		System.out.println(Instance.class.newInstance() instanceof BaseBean  );
//		System.out.println(Instance.class.newInstance() instanceof TaskInfo  );
//		System.out.println(Object.class.getSuperclass());
	}
	private static void b() throws SchedulerException {
		Scheduler scheduler=new StdSchedulerFactory().getScheduler();
		  JobDetail jobDetail= JobBuilder.newJob(TestJob.class).withIdentity("1").build();
	      //trigger
		  SimpleScheduleBuilder ssb = SimpleScheduleBuilder.simpleSchedule();
	      Trigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().
	              withIdentity("t1").startAt(new Date(new Date().getTime()+1000))
	              //.withSchedule(ssb.withIntervalInSeconds(2).withRepeatCount(5))
	              .build();
	      
	      scheduler.start();
	      //任务分配并启动
	      scheduler.scheduleJob(jobDetail,trigger);//TriggerState:NONE--NORMAL
	}
	public static void a() throws Exception{
		Scheduler scheduler=new StdSchedulerFactory().getScheduler();
		scheduler.start();
		  JobDetail jobDetail= JobBuilder.newJob(TestJob.class).withIdentity("1").build();
	      //trigger
	      CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5  *  * *  9 ? 2018");
	      CronTrigger trigger=TriggerBuilder.newTrigger().
	              withIdentity("t1")
	              .withSchedule(cronScheduleBuilder).build();
	      
	      System.out.println(trigger.getFireTimeAfter(new Date()));//null
	      
	      
	      KeyMatcher<JobKey> jobMatcher = KeyMatcher.keyEquals(jobDetail.getKey());
		  scheduler.getListenerManager().addJobListener(new InstanceTaskListener(), jobMatcher);
	      
		  KeyMatcher<TriggerKey> triggerMatcher = KeyMatcher.keyEquals(trigger.getKey());
			scheduler.getListenerManager().addTriggerListener(new InstanceTriggerListener(),triggerMatcher);
	      //任务分配并启动
	      scheduler.scheduleJob(jobDetail,trigger);//TriggerState:NONE--NORMAL
//	      Thread.sleep(2000);
//	      for(int i=0;i<10;i++){
//	    	  scheduler.triggerJob(JobKey.jobKey("1"));
//	    	  Thread.sleep(2000);
//	      }
	}
	public static void c(){
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5  *  * *  9 ? 2018");
		 CronTrigger trigger=TriggerBuilder.newTrigger().
	              withIdentity("t1")
	              .withSchedule(cronScheduleBuilder).build();
		System.out.println(trigger.getFireTimeAfter(new Date()));//null
	}
	public static void testReflect() throws Exception{
		Class<?> clz=Instance.class;
		Field[] fields = clz.getFields();
		for(Field f:fields){
			System.out.println(f.getName());
		}
		Field[] declaredFields = clz.getDeclaredFields();
		for(Field f:declaredFields){
			System.out.println(f);
		}
		System.out.println("---");
		Field declaredField = clz.getDeclaredField("taskId");
		
		System.out.println(declaredField);
		
		Class<?> superclass = clz.getSuperclass();
		Field declaredField2 = superclass.getDeclaredField("createTime");
		System.out.println(declaredField2);
		System.out.println(declaredField2.getName());
		declaredField2.setAccessible(true);
		declaredField2.set(superclass.newInstance(),new Date() );
	}
	
}
