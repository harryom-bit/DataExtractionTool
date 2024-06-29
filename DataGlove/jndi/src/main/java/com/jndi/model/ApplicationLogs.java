package com.jndi.model;

public class ApplicationLogs {
	private String moduleName;
	private String fromDate;
	private String toDate;
	private String processModule;
	private String processSubModule;
	private int logLevel;
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getProcessModule() {
		return processModule;
	}
	public void setProcessModule(String processModule) {
		this.processModule = processModule;
	}
	public String getProcessSubModule() {
		return processSubModule;
	}
	public void setProcessSubModule(String processSubModule) {
		this.processSubModule = processSubModule;
	}
	public int getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}
}
