package com.roadjava.schedule.beans;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

/*
 * 任务每天的运行状态，运行日志都是跟一个实例相关联的，因此每一个有效的任务每天都会生成一个实例;
 * 每一次任务执行都会生成一个实例
 */
public class Instance extends BaseBean{
	private Long taskId;
	private String parentIds;// ,分割 1,2,3
	@TableField(exist=false)
	private String  sonIds;//,分割 1,2,3
	private String status;// READY RUNNING FAIL AUTO_SUCC HAND_SUCC,默认READY
	private String bizDate;//一般都是传个业务日期，sql和shell中都能获取到日期，传它干嘛?
	private String type;//手工HAND,自动AUTO;默认AUTO
	@TableField(exist=false)
	private String cron;
	@TableField(exist=false)
	private Date   nextFireTime;//下次触发时间，用来判断是否misFire
	
	
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public Date getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBizDate() {
		return bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	public String getSonIds() {
		return sonIds;
	}
	public void setSonIds(String sonIds) {
		this.sonIds = sonIds;
	}
	@Override
	public String toString() {
		return "Instance [taskId=" + taskId + ", parentIds=" + parentIds + ", sonIds=" + sonIds + ", status=" + status
				+ ", bizDate=" + bizDate + ", type=" + type + ", cron=" + cron + ", nextFireTime=" + nextFireTime
				+ ", getId()=" + getId() + ", getCreator()=" + getCreator() + ", getModifier()=" + getModifier()
				+ ", getCreateTime()=" + getCreateTime() + ", getModifyTime()=" + getModifyTime() + ", getIsDeleted()="
				+ getIsDeleted() + "]";
	}
}
