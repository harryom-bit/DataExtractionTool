package com.jndi.model;

import java.sql.Date;

public class tableFromDate {
	private String tableName;
	private Date fromDate;
	private int bufferDays;
	
	public int getBufferDays() {
		return bufferDays;
	}
	public void setBufferDays(int bufferDays) {
		this.bufferDays = bufferDays;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	

}
