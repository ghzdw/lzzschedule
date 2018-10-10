package com.roadjava.schedule.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	private static Logger logger=LoggerFactory.getLogger(DateUtil.class);
	private static String DEFAULT_PATTERN="yyyy-MM-dd HH:mm:ss";
	public static String getPatternDateString(String pattern,Date date){
		if (pattern==null) pattern=DEFAULT_PATTERN;
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	public static Date getPatternDate(String pattern,String date){
		if (pattern==null) pattern=DEFAULT_PATTERN;
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.error("日期格式化出错:",e);
		}
		return null;
	}
}
