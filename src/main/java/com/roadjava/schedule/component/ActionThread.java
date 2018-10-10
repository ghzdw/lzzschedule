package com.roadjava.schedule.component;

import com.roadjava.schedule.listeners.SpringBeanFactory;
import com.roadjava.schedule.service.InstanceService;
import com.roadjava.schedule.service.impl.InstanceServiceImpl;

public class ActionThread implements Runnable{
	private Long id;
	private String status;
	public ActionThread() {}
	
	public ActionThread(Long id, String status) {
		this.id = id;
		this.status = status;
	}

	@Override
	public void run() {
		InstanceService bean = SpringBeanFactory.getBean("instanceServiceImpl", InstanceServiceImpl.class);
		bean.updateInstanceStatus(id, status);
	}

}
