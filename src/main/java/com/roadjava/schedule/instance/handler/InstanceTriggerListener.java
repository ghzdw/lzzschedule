package com.roadjava.schedule.instance.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.roadjava.schedule.beans.Instance;
import com.roadjava.schedule.beans.InstanceStatus;
import com.roadjava.schedule.component.ActionThread;
import com.roadjava.schedule.listeners.SpringBeanFactory;
import com.roadjava.schedule.service.InstanceService;
import com.roadjava.schedule.service.impl.InstanceServiceImpl;
import com.roadjava.schedule.utils.AppConst;

public class InstanceTriggerListener implements TriggerListener{
	private static Logger logger=LoggerFactory.getLogger(InstanceTriggerListener.class);
	private String name;
	
	public InstanceTriggerListener(){	}
	public InstanceTriggerListener(String name){
		this.name=name;
	}
	@Override
	public String getName() {
		return "InstanceTriggerListener"+name;
	}
	/*
	 * trigger触发，与该trigger关联的jobdetail将要执行execute方法
	 */
	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		System.out.println("triggerFired");
		setCrtExecInstanceStatus(context,InstanceStatus.RUNNING.name());
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
	/*
	 * 在 Trigger 触发后，与该trigger关联的jobdetail将要被执行时由 Scheduler 调用这个方法。
	 * 假如这个方法返回 true，这个 Job 将不会为此次 Trigger 触发而得到执行。
	 */
	@SuppressWarnings("unchecked")
	@Override 
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		System.out.println("vetoJobExecution");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		//判断当前实例的所有父级是不是都执行完成了?是:则正常执行;否:不执行
		Instance crtAction = (Instance) jobDataMap.get("crtAction");
		Map<String, Instance> allActionMap = (Map<String, Instance>) jobDataMap.get("allActionMap");
		boolean vetoExec=false;//默认执行
		String parentIds = crtAction.getParentIds();
		if (StringUtils.isBlank(parentIds)) 
			return vetoExec;
		else{
			String[] parentIdsArr = parentIds.split(",");
			for(String parentIdOfCrtIns:parentIdsArr){
				Instance parentIns = allActionMap.get(parentIdOfCrtIns);
				if (!parentIns.getStatus().equals(InstanceStatus.AUTO_SUCC.name())) {
					vetoExec=true;
					break;
				}
			}
		}
		return vetoExec;
	}
	/*
	 * scheduler 调用这个方法是在 Trigger错过触发时。
	 */
	@Override
	public void triggerMisfired(Trigger trigger) {
		System.out.println("triggerMisfired");
	}
	/*
	 * Trigger 被触发并且完成了 Job 的执行时，Scheduler 调用这个方法。
	 * 这不是说这个 Trigger 将不再触发了，而仅仅是当前 Trigger 的触发(并且紧接着的 Job 执行) 结束时。
	 * 这个 Trigger也许还要在将来触发多次的。
	 */
	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		System.out.println("triggerComplete");
		//设置当前实例的状态
		setCrtExecInstanceStatus(context, InstanceStatus.AUTO_SUCC.name());
		//判断当前实例的所有子级是不是因为自己而错过了执行?是:在这里触发它们;否:不做任何事
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		List<Instance> allSon=(List<Instance>) jobDataMap.get("allSon");
		if (allSon!=null) {//=null即没有子任务
			for(Instance ins:allSon){
				if (ins.getNextFireTime().getTime()<new Date().getTime()) {//下次执行事件<当前事件
					JobKey jobKey=JobKey.jobKey(String.valueOf(ins.getId()), AppConst.DEFAULT_INS_GROUP);
					try {
						JobDataMap sonData = context.getScheduler().getJobDetail(jobKey).getJobDataMap();
						
						context.getScheduler().triggerJob(jobKey,sonData);//立即执行,手工调起调度，不影响原有的配置
					} catch (SchedulerException e) {
						logger.error("调度延时任务出错:",e);
					}
				}
			}
		}
	}

}
