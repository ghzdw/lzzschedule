package com.roadjava.schedule.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;
import com.roadjava.schedule.beans.BaseBean;

public class ScheUtils {
	private static Logger logger=LoggerFactory.getLogger(ScheUtils.class);
	/**
	 * 
	 * @param list:要转换的list
	 * @param c:需要转为什么类型
	 * @return
	 */
	public static <T> List<T> map2Bean(List<Map<String, Object>> list,Class<T> c){
		List<T> retList=new ArrayList<>();
		//获取对象实例
		Class<? extends Object> clazz = c.getClass();
		//获取对象以及父类的属性集合并放入fieldMap
		Map<String, Field> fieldMap=new HashMap<>();
		Field[] clzFields = clazz.getDeclaredFields();
		for(Field f:clzFields){
			fieldMap.put(f.getName(), f);
		}
		Class<?> superclass = clazz.getSuperclass();
		try {
			if (superclass!=null&&superclass.newInstance() instanceof BaseBean) {
				 Field[] superClzFields = superclass.getDeclaredFields();
				 for(Field f:superClzFields){
						fieldMap.put(f.getName(), f);
				 }
			}
		} catch (Exception e) {
			logger.error("父类实例创建出错:",e);
			return null;
		} 
		for(Map<String, Object> map:list){
			Set<Entry<String, Object>> entrySet = map.entrySet();
			T obj=null;
			try {
				obj = (T)clazz.newInstance();
			} catch (Exception e) {
				logger.error("创建类型对象出错:",e);
			} 
			for (Iterator<Entry<String, Object>> iterator = entrySet.iterator(); iterator.hasNext();) {
				Entry<String, Object> entry =iterator.next();
				 String key = underLine2Camel(entry.getKey());
				 Field field = fieldMap.get(key);
				 field.setAccessible(true);
				 try {
					field.set(obj, entry.getValue());
				} catch (Exception e) {
					logger.error("构造bean对象出错:",e);
					return null;
				} 
			}
			retList.add(obj);
		}
		return retList;
	}
	private static String underLine2Camel(String str){
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
	}
}
