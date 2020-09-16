package com.github.model.vo;

public class TaskVo {

	private String beanName;
	private String methodName;
	private String methodParams;
	private String cron;
	private Long interval;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodParams() {
		return methodParams;
	}

	public void setMethodParams(String methodParams) {
		this.methodParams = methodParams;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}
}
