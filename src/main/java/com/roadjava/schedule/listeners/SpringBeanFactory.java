package com.roadjava.schedule.listeners;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 初始化spring的applicationcontext
 * @author zhao
 */
@Component 
public class SpringBeanFactory implements ApplicationContextAware{
	private static ApplicationContext ac=null;
	private static Logger logger=Logger.getLogger(SpringBeanFactory.class);
	public static <T> T  getBean(String beanId,Class<T> clazz){
		return ac.getBean(beanId, clazz);
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ac==null) {//springmvc的容器初始化完成时就不用做了
			logger.info("spring初始化完成事件被触发");
			ac=applicationContext;
		}
	}
}
