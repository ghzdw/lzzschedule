package com.roadjava.schedule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.controller.TaskInfoController;
import com.roadjava.schedule.dao.TaskInfoDao;
import com.roadjava.schedule.service.TaskInfoService;
import com.roadjava.schedule.utils.AppConst;
import com.roadjava.schedule.utils.ScheduleManager;
import com.roadjava.schedule.vo.ResVo;
import com.roadjava.schedule.vo.TableGridVo;

@Service
public class TaskInfoServiceImpl implements TaskInfoService{
	private static Logger logger=LoggerFactory.getLogger(TaskInfoServiceImpl.class);
	@Resource
	private TaskInfoDao taskDao;
	@Resource
	private ScheduleManager taskManager;
	@Override
	public ResVo addTask(TaskInfo task) {
		if (StringUtils.isBlank(task.getParentIds())) {
			task.setParentIds("0");
		 }
		 Integer insert = taskDao.insert(task);
		 ResVo resVo=new ResVo();
		 if (insert!=1) {
			resVo.setStatus(AppConst.ERROR);
		}else {
			resVo.setStatus(AppConst.SUCCESS);
		}
		 return resVo;
	}
	@Override
	public TableGridVo getTasks() {
		List<TaskInfo> tasks = taskDao.selectPage(new RowBounds(0, 10), null);
		Integer total = taskDao.selectCount(null);
		TableGridVo tableGridVo=new TableGridVo(total, tasks);
		return tableGridVo;
	}
	@Override
	public ResVo queryTaskById(String taskId) {
		TaskInfo task = taskDao.selectById(taskId);
		ResVo resVo=new ResVo(AppConst.SUCCESS, task);
		return resVo;
	}
	@Override
	public ResVo updateTask(TaskInfo task) {
		Integer cnt = taskDao.updateById(task);
		ResVo resVo=new ResVo();
		if (cnt!=1) {
			resVo.setStatus(AppConst.ERROR);
		}else {
			resVo.setStatus(AppConst.SUCCESS);
			try {
				taskManager.updateInstance(task);
			} catch (Exception e) {
				logger.error("任务ID:"+task.getId()+"在调度计划中更新出错:",e);
			}
		}
		return resVo;
	}
	@Override
	public void taskJoinInSchedule(String taskIds) {
		String[] taskIdArr=taskIds.split(",");
		for (int i = 0; i < taskIdArr.length; i++) {
			TaskInfo task=taskDao.selectById(taskIdArr[i]);
			try {
				taskManager.addInstance(task);
			} catch (Exception e) {
				logger.error("任务ID"+taskIdArr[i]+"加入调度计划出错:",e);
			}
		}
	}
	@Override
	public void taskRemoveFromSchedule(String taskIds) {
		String[] taskIdArr=taskIds.split(",");
		for (int i = 0; i < taskIdArr.length; i++) {
			TaskInfo task=taskDao.selectById(taskIdArr[i]);
			try {
				taskManager.removeInstance(task);
			} catch (Exception e) {
				logger.error("任务ID"+taskIdArr[i]+"从调度计划移除出错:",e);
			}
		}
	}
	@Override
	public List<TaskInfo> getAvailableTasks() {
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("is_deleted", 'N');
		List<TaskInfo> tasks = taskDao.selectByMap(paramMap);
		return tasks;
	}

}
