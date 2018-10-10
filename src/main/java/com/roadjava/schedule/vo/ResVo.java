package com.roadjava.schedule.vo;

import com.roadjava.schedule.utils.AppConst;

public class ResVo {
	private String status;
	private Object data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ResVo() {
	}
	public ResVo(String status, Object data) {
		this.status = status;
		this.data = data;
	}
	public ResVo(String status) {
		this.status = status;
	}
	
}
