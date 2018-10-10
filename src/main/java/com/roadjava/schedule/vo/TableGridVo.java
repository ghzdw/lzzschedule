package com.roadjava.schedule.vo;

import java.util.List;

public class TableGridVo {
	private Integer total;
	private List<?> rows;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public TableGridVo(Integer total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}
	public TableGridVo() {
	}
	
}
