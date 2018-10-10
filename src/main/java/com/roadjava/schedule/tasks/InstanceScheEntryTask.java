package com.roadjava.schedule.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.roadjava.schedule.beans.Instance;
import com.roadjava.schedule.listeners.SpringBeanFactory;
import com.roadjava.schedule.utils.ScheduleManager;

public class InstanceScheEntryTask implements Job{
	private ScheduleManager scheduleManager;
	
	public  InstanceScheEntryTask(){
		scheduleManager=SpringBeanFactory.getBean("scheduleManager", ScheduleManager.class);
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		List<Instance> instances = (List<Instance>) jobDataMap.get("data");
		Map<String, Instance> allActionMap=makeMapFromList(instances);
		for(Instance ins:instances){
			scheduleManager.addInstance(ins,allActionMap);
		}
	}
	private Map<String, Instance> makeMapFromList(List<Instance> instances) {
		Map<String, Instance> allActionMap=new HashMap<>();
		for(Instance instance:instances){
			allActionMap.put(String.valueOf(instance.getId()), instance);
		}
		return allActionMap;
	}
}
