package com.roadjava.schedule.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.roadjava.schedule.beans.Instance;
import com.roadjava.schedule.beans.InstanceStatus;
import com.roadjava.schedule.beans.InstanceType;
import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.dao.InstanceDao;
import com.roadjava.schedule.service.InstanceService;
import com.roadjava.schedule.utils.DateUtil;
import com.roadjava.schedule.utils.ScheUtils;
import com.roadjava.schedule.vo.TableGridVo;

@Service
public class InstanceServiceImpl implements InstanceService {
    @Resource
    private InstanceDao instanceDao;
	@Override
	public Map<String, Object> getInstances() {
		Map<String, Object> retMap=new HashMap<>();
		List<Map<String, Object>> list=instanceDao.getInstances();
		Integer total = instanceDao.selectCount(null);
		retMap.put("total", total);
		retMap.put("rows", list);
		return retMap;
	}

	@Override
	public void batchAdd(List<Instance> instances) {
		instanceDao.batchAdd(instances);
	}

	@Override
	public List<Instance> selectInsJustAdd(Date now) {
		List<Instance> list=new ArrayList<>();
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("biz_date", DateUtil.getPatternDateString("yyyy-MM-dd", now));
		paramMap.put("status", InstanceStatus.READY.name());
		List<Map<String, Object>> selectList = instanceDao.selectInsJustAdd(paramMap);
		for(Map<String, Object> m:selectList){
			Instance instance=new Instance();
			instance.setId(Long.valueOf(m.get("id").toString()));
			instance.setTaskId(Long.valueOf(m.get("task_id").toString()));
			instance.setStatus(m.get("status").toString());
			instance.setCron(m.get("cron")!=null?m.get("cron").toString():"");
			list.add(instance);
		}
		return list;
	}

	@Override
	public void batchUpdate(List<Instance> justIns) {
		for(Instance i:justIns){
			instanceDao.updateById(i);
		}
	}

	@Override
	public void updateInstanceStatus(Long instanceId, String status) {
		Instance instance=new Instance();
		instance.setId(instanceId);
		instance.setStatus(status);
		instanceDao.updateById(instance);
	}

}
