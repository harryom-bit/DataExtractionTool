package com.jndi.model;

import java.sql.Date;

public class TableFilters {
	private String meterId;
	private String sdpId;
	private Date from;
	private Date to;
	private String adoId;
	private Date affectedDate;
	private String timeSeries;
	private int bufferDays;
	private String caseNumber;
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	
	public int getBufferDays() {
		return bufferDays;
	}
	public void setBufferDays(int bufferDays) {
		this.bufferDays = bufferDays;
	}
	public String getTimeSeries() {
		return timeSeries;
	}
	public void setTimeSeries(String timeSeries) {
		this.timeSeries = timeSeries;
	}
	public Date getAffectedDate() {
		return affectedDate;
	}
	public void setAffectedDate(Date affectedDate) {
		this.affectedDate = affectedDate;
	}
	public String getAdoId() {
		return adoId;
	}
	public void setAdoId(String adoId) {
		this.adoId = adoId;
	}
	public String getMeterId() {
		return meterId;
	}
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}
	public String getSdpId() {
		return sdpId;
	}
	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	@Override
	public String toString() {
		return "TableFilters [meterId=" + meterId + ", sdpId=" + sdpId + ", from=" + from + ", to=" + to + ", adoId="
				+ adoId + ", affectedDate=" + affectedDate + ", timeSeries=" + timeSeries + ", bufferDays=" + bufferDays
				+ ", caseNumber=" + caseNumber + "]";
	}	
	
}
