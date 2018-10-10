package com.roadjava.schedule.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.service.TaskInfoService;
import com.roadjava.schedule.utils.AppConst;
import com.roadjava.schedule.vo.ResVo;
import com.roadjava.schedule.vo.TableGridVo;

@Controller
public class TaskInfoController {
	private static Logger logger=LoggerFactory.getLogger(TaskInfoController.class);
	@Resource
	private TaskInfoService taskService;
	@RequestMapping("/toTask")
	public String toTask(){
		return "task";
	}
	@RequestMapping("/addTask") @ResponseBody
	public ResVo addTask(TaskInfo task){
		ResVo resVo = null;
		try {
			resVo = taskService.addTask(task);
		} catch (Exception e) {
			resVo=new ResVo(AppConst.ERROR);
		}
		return resVo;
	}
	@RequestMapping("/getTasks") @ResponseBody
	public TableGridVo getTasks(){
		TableGridVo result = null;
		try {
			result = taskService.getTasks();
		} catch (Exception e) {
			logger.error("查询任务列表出错",e);
		}
		return result;
	}
	@RequestMapping("/queryTaskById") @ResponseBody
	public ResVo queryTaskById(@RequestParam("taskId") String taskId){
		ResVo result = null;
		try {
			result = taskService.queryTaskById(taskId);
		} catch (Exception e) {
			logger.error("根据任务ID查询任务出错:",e);
		}
		return result;
	}
	@RequestMapping("/updateTask") @ResponseBody
	public ResVo updateTask(TaskInfo task){
		ResVo resVo = null;
		try {
			resVo = taskService.updateTask(task);
		} catch (Exception e) {
			resVo=new ResVo(AppConst.ERROR);
		}
		return resVo;
	}
	@RequestMapping("/taskJoinInSchedule") @ResponseBody
	public void taskJoinInSchedule(@RequestParam("taskIds") String taskIds){
		try {
			 taskService.taskJoinInSchedule(taskIds);
		} catch (Exception e) {
			logger.error("根据任务ID查询任务出错:",e);
		}
	}
	@RequestMapping("/taskRemoveFromSchedule") @ResponseBody
	public void taskRemoveFromSchedule(@RequestParam("taskIds") String taskIds){
		try {
			taskService.taskRemoveFromSchedule(taskIds);
		} catch (Exception e) {
			logger.error("根据任务ID查询任务出错:",e);
		}
	}
}
