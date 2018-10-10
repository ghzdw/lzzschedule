package com.roadjava.schedule.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.roadjava.schedule.beans.Instance;
import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.service.InstanceService;
import com.roadjava.schedule.service.TaskInfoService;
import com.roadjava.schedule.utils.DateUtil;

@Component
public class InsGenerateTask{
	private static Logger logger=LoggerFactory.getLogger(InsGenerateTask.class);
	@Resource
	private InstanceService instanceService;
	@Resource
	private TaskInfoService taskService;
	@Resource(name="scheduler")
	private Scheduler scheduler;
	public void generateIns(){
	 //任务分配并启动
      try {	
		  Date now =new Date();	
		  Map<Long, TaskInfo> tasksMap = generateInsByAuto(now);
		  /*
		   * id taskid      parent_id      sonIds
		   * 7   0                                  
		   * 8   1(0)             0
		   * 9   2(0)             0
		   * 10  3(pid:2,1)       8,9
		   */
		  List<Instance> justIns = updateInstanceParentId(now, tasksMap);
		  setSonIds(justIns);
		  
		  
		  JobDetail jobDetail= JobBuilder.newJob(InstanceScheEntryTask.class).withIdentity(
				  DateUtil.getPatternDateString("yyyy-MM-dd", now)+"ENTRY").build();
	      //设置job运行需要用到的参数
	      JobDataMap jobDataMap = jobDetail.getJobDataMap();
	      jobDataMap.put("data", justIns);
	
	//      String nextDayZero=DateUtil.getPatternDateString("yyyy-MM-dd", DateUtils.addDays(new Date(), 1))+" 00:00:00";
	      String nextDayZero=DateUtil.getPatternDateString("yyyy-MM-dd", DateUtils.addDays(new Date(), 0))+" 13:03:55";
	      
//	      SimpleScheduleBuilder ssb = SimpleScheduleBuilder.simpleSchedule();
	      Trigger trigger = TriggerBuilder.newTrigger().
				      withIdentity(DateUtil.getPatternDateString("yyyy-MM-dd", now)+"TRIGGER")
				      .startAt(DateUtil.getPatternDate(null, nextDayZero))
//				      .withSchedule(ssb.withRepeatCount(1).withIntervalInSeconds(2))
				      .build();
    	  scheduler.scheduleJob(jobDetail,trigger);//TriggerState:NONE--NORMAL
		} catch (Exception e) {
			logger.error("初始化实例出错:",e);
			throw new RuntimeException("初始化实例出错");
		}
	}
	//设置sonIds
	private void setSonIds(List<Instance> justIns) {
		for(Instance instance:justIns){
			String instanceId=String.valueOf(instance.getId());
			List<String> sonInsIdList=new ArrayList<>();
			for(Instance i:justIns){
				if (StringUtils.isNotBlank(i.getParentIds())) {
					String[] parentIdsArr = i.getParentIds().split(",");
					 for(String parentId:parentIdsArr){
						 if (instanceId.equals(parentId)) {//人家的父级id就是我的id，说明人家是我的子节点
						     sonInsIdList.add(i.getId()+""); 	
						 }
					 }
				}
			 }
			 if (sonInsIdList.size()>0) {
				  instance.setSonIds(StringUtils.join(sonInsIdList,","));
			 }
		  }
	}
	//查询出来batchAdd插入的用于明天运行的实例，此时就有了id。设置parentIds之后更新db
	private List<Instance> updateInstanceParentId(Date now, Map<Long, TaskInfo> tasksMap) {
		List<Instance> justIns=instanceService.selectInsJustAdd(now);
		  for(Instance instance:justIns){
			  TaskInfo relationTask = tasksMap.get(instance.getTaskId());
			  String parentIds = relationTask.getParentIds();
			  if (parentIds!=null) {//=null即没有父任务
				  String[] parentIdsArr = parentIds.split(",");
				  List<String> parentInsIdList=new ArrayList<>();
				  for(String parentId:parentIdsArr){
					  for(Instance i:justIns){
						 if (String.valueOf(i.getTaskId()).equals(parentId)) {
							 parentInsIdList.add(String.valueOf(i.getId()));
						 }  
					  }
				  }
				  if (parentInsIdList.size()>0) {
					  instance.setParentIds(StringUtils.join(parentInsIdList,","));
				  }
			  }
		  }
		  instanceService.batchUpdate(justIns);
		return justIns;
	}
	//获取所有的任务用于生成明天将要运行的实例
	private Map<Long, TaskInfo> generateInsByAuto(Date now) {
		  List<TaskInfo> tasks2Instance=taskService.getAvailableTasks();
		  List<Instance> instances=new ArrayList<>();
		  Map<Long, TaskInfo> tasksMap=new HashMap<>();//taskId--task
		  for(TaskInfo task:tasks2Instance){
			  tasksMap.put(task.getId(), task);
			  Instance instance=new Instance();
			  instance.setTaskId(task.getId());
			  instance.setBizDate(DateUtil.getPatternDateString("yyyy-MM-dd", now));
			  instance.setCreateTime(now);
			  instance.setModifyTime(now);
			  instances.add(instance);
		  }
		  instanceService.batchAdd(instances);
		  return tasksMap;
	}
}
