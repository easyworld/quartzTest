package com.dhu.quartzTest.service;

/** 
 * @Description:  
 * 
 * @Title: QuartzManager.java 
 * @Package com.joyce.quartz 
 * @Copyright: Copyright (c) 2014 
 * 
 * @author Comsys-LZP 
 * @date 2014-6-26 下午03:15:52 
 * @version V2.0 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhu.quartzTest.domain.ScheduleJob;
import com.dhu.quartzTest.job.HttpRequestJob;

/**
 * @Description: 定时任务管理类
 * 
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2015
 * 
 * @author modified by Comsys-LZP's code.
 */
@Service
public class QuartzService {

	@Autowired
	private Scheduler scheduler;

	// private String JOB_GROUP_NAME = Constant.JOB_GROUP_NAME;
	// private String TRIGGER_GROUP_NAME = Constant.TRIGGER_GROUP_NAME;

	public List<ScheduleJob> getPlannedJobList() throws SchedulerException {
		Scheduler scheduler = this.scheduler;
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler
					.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				ScheduleJob job = new ScheduleJob();
				job.setJobName(jobKey.getName());
				job.setJobGroup(jobKey.getGroup());
				job.setDesc("触发器:" + trigger.getKey());
				job.setUrl(scheduler.getJobDetail(jobKey).getJobDataMap()
						.getString("url"));
				Trigger.TriggerState triggerState = scheduler
						.getTriggerState(trigger.getKey());
				job.setJobStatus(triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}
				jobList.add(job);
			}
		}
		return jobList;
	}

	public List<ScheduleJob> getRunningJobList() throws SchedulerException {
		Scheduler scheduler = this.scheduler;
		List<JobExecutionContext> executingJobs = scheduler
				.getCurrentlyExecutingJobs();
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(
				executingJobs.size());
		for (JobExecutionContext executingJob : executingJobs) {
			ScheduleJob job = new ScheduleJob();
			JobDetail jobDetail = executingJob.getJobDetail();
			JobKey jobKey = jobDetail.getKey();
			Trigger trigger = executingJob.getTrigger();
			job.setJobName(jobKey.getName());
			job.setJobGroup(jobKey.getGroup());
			job.setDesc("触发器:" + trigger.getKey());
			Trigger.TriggerState triggerState = scheduler
					.getTriggerState(trigger.getKey());
			job.setJobStatus(triggerState.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				job.setCronExpression(cronExpression);
			}
			jobList.add(job);
		}
		return jobList;
	}

	/**
	 * 获取一个任务的参数Map
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @return
	 * @throws SchedulerException
	 */
	public JobDataMap getOneJobMap(String jobName, String jobGroupName)
			throws SchedulerException {
		Scheduler scheduler = this.scheduler;
		return scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName))
				.getJobDataMap();
	}

	/**
	 * @Description: 添加一个定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务类名，需要继承job
	 * @param map
	 *            任务数据map
	 * @param time
	 *            cron表达式
	 * 
	 */
	public void addJob(String jobName, String jobGroupName, String triggerName,
			String triggerGroupName, Class<? extends Job> jobClass,
			JobDataMap map, String time) throws SchedulerException {
		Scheduler sched = this.scheduler;
		JobDetail jobDetail = JobBuilder.newJob(jobClass).setJobData(map)
				.withIdentity(jobName, jobGroupName).build();// 任务名，任务组，任务执行类

		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
				.cronSchedule(time);// 触发器时间设定

		// 触发器
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(triggerName, triggerGroupName)
				.withSchedule(scheduleBuilder).build();// 触发器名,触发器组
		sched.scheduleJob(jobDetail, trigger);
	}

	/**
	 * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 */
	public void modifyJobTime(String jobName, String jobGroupName, String time)
			throws SchedulerException {
		Scheduler sched = this.scheduler;
		List<? extends Trigger> list = sched.getTriggersOfJob(JobKey.jobKey(
				jobName, jobGroupName));
		if (list == null || list.isEmpty()) {
			return;
		}
		CronTrigger trigger = (CronTrigger) list.get(0);
		String oldTime = trigger.getCronExpression();

		if (!oldTime.equalsIgnoreCase(time)) {
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					.cronSchedule(time);
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder()
					.withIdentity(trigger.getKey())
					.withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			sched.rescheduleJob(trigger.getKey(), trigger);
		}
	}

	/**
	 * 修改一个任务的URL(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param name
	 * @param group
	 * @param url
	 * @throws SchedulerException
	 */
	public void modifyJobUrl(String name, String group, String url)
			throws SchedulerException {
		Trigger trigger = scheduler
				.getTriggersOfJob(JobKey.jobKey(name, group)).get(0);

		JobDataMap map = new JobDataMap();
		map.put("url", url);
		JobDetail jobDetail = JobBuilder.newJob(HttpRequestJob.class)
				.setJobData(map).withIdentity(name, group).build();
		removeJob(name, group);
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 暂停一个任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 */
	public void pauseJob(String jobName, String jobGroup)
			throws SchedulerException {
		Scheduler sched = this.scheduler;
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		sched.pauseJob(jobKey);
	}

	/**
	 * 恢复一个任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 */
	public void resumeJob(String jobName, String jobGroup)
			throws SchedulerException {
		Scheduler sched = this.scheduler;
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		sched.resumeJob(jobKey);
	}

	/**
	 * @Description: 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * 
	 */
	public void removeJob(String jobName, String jobGroupName)
			throws SchedulerException {
		Scheduler sched = this.scheduler;
		sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
	}

	/**
	 * 立刻执行一个任务,如果scheduler未开启则开启
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param data
	 *            执行参数
	 */
	public void startJobNow(String jobName, String jobGroupName, JobDataMap data)
			throws SchedulerException {
		Scheduler sched = this.scheduler;
		if (sched.isShutdown() || sched.isInStandbyMode())
			sched.start();
		sched.triggerJob(JobKey.jobKey(jobName, jobGroupName), data);
	}

	/**
	 * @Description:启动所有定时任务
	 * 
	 * 
	 */
	public void startJobs() throws SchedulerException {
		scheduler.start();
	}

	public void pauseJobs() throws SchedulerException {
		scheduler.pauseAll();
	}

	/**
	 * 让进程进入standby模式，而不是关闭
	 * 
	 * @throws SchedulerException
	 */
	public void standByJobs() throws SchedulerException {
		Scheduler sched = this.scheduler;
		if (sched.isStarted() && !sched.isInStandbyMode()) {
			sched.standby();
		}
	}

	/**
	 * @Description:关闭并删除所有定时任务
	 * 
	 */
	public void shutdownJobs() throws SchedulerException {
		Scheduler sched = this.scheduler;
		if (!sched.isShutdown()) {
			sched.shutdown();
		}
	}

	public boolean isInStandbyMode() throws SchedulerException {
		return this.scheduler.isInStandbyMode();
	}

}
