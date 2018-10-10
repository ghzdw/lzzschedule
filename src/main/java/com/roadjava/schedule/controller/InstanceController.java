package com.roadjava.schedule.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.service.InstanceService;
import com.roadjava.schedule.service.TaskInfoService;
import com.roadjava.schedule.utils.AppConst;
import com.roadjava.schedule.vo.ResVo;
import com.roadjava.schedule.vo.TableGridVo;

@Controller
public class InstanceController {
	private static Logger logger=LoggerFactory.getLogger(InstanceController.class);
	@Resource
	private InstanceService instanceService;
	@RequestMapping("/toInstance")
	public String toTask(){
		return "action";
	}
	@RequestMapping("/getInstances") @ResponseBody
	public Map<String, Object> getTasks(){
		Map<String, Object> result = null;
		try {
			result = instanceService.getInstances();
		} catch (Exception e) {
			logger.error("查询实例列表出错",e);
		}
		return result;
	}
}
