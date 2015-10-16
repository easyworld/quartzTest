package com.dhu.quartzTest.domain;

import com.dhu.quartzTest.util.Constant;

public class ScheduleJob {
	private String jobName;
	/* job group */
	private String jobGroup;
	/* job状态 */
	private String jobStatus;

	/* 任务请求URL */
	private String url;
	/* 任务请求方式GET/POST */
	private String method;
	/* 任务请求参数 */
	private String params;
	/* cron表达式 */
	private String cronExpression;
	/* 任务描述 */
	private String desc;

	public ScheduleJob() {
		jobGroup = Constant.JOB_GROUP_NAME;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
