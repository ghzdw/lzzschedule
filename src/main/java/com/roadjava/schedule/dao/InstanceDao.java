package com.roadjava.schedule.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.roadjava.schedule.beans.Instance;

public interface InstanceDao extends BaseMapper<Instance>{

	void batchAdd(List<Instance> instances);

	List<Map<String, Object>> selectInsJustAdd(Map<String, Object> paramMap);

	List<Map<String, Object>> getInstances();

}
