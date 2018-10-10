package com.roadjava.schedule.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.roadjava.schedule.beans.Instance;
import com.roadjava.schedule.vo.TableGridVo;

public interface InstanceService {

	Map<String, Object> getInstances();

	void batchAdd(List<Instance> instances);

	List<Instance> selectInsJustAdd(Date now);

	void batchUpdate(List<Instance> justIns);
	
	void updateInstanceStatus(Long instanceId,String status); 

}
