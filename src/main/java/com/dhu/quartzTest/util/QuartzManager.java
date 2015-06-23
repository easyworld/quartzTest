package com.dhu.quartzTest.util;

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
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.dhu.quartzTest.domain.ScheduleJob;

/**
 * @Description: 定时任务管理类
 * 
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2015
 * 
 * @author modified by Comsys-LZP's code.
 */
public class QuartzManager {
	/*先使用new的方式生成scheduler，后期优化成bean注入,*/
	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

	// private static String JOB_GROUP_NAME = Constant.JOB_GROUP_NAME;
	// private static String TRIGGER_GROUP_NAME = Constant.TRIGGER_GROUP_NAME;

	public static List<ScheduleJob> getPlannedJobList() {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<ScheduleJob> getRunningJobList() {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
	public static void addJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName,
			Class<? extends Job> jobClass, JobDataMap map, String time) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(jobClass).setJobData(map)
					.withIdentity(jobName, jobGroupName).build();// 任务名，任务组，任务执行类

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					.cronSchedule(time);// 触发器时间设定

			// 触发器
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(triggerName, triggerGroupName)
					.withSchedule(scheduleBuilder).build();// 触发器名,触发器组
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 */
	public static void modifyJobTime(String jobName, String jobGroupName,
			String time) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();

			TriggerKey triggerKey = TriggerKey
					.triggerKey(jobName, jobGroupName);

			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);

			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
						.cronSchedule(time);
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
						.withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				sched.rescheduleJob(triggerKey, trigger);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 暂停一个任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 */
	public static void pauseJob(String jobName, String jobGroup) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			sched.pauseJob(jobKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 恢复一个任务
	 * 
	 * @param jobName
	 * @param jobGroup
	 */
	public static void resumeJob(String jobName, String jobGroup) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			sched.resumeJob(jobKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 * 
	 */
	public static void removeJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.pauseTrigger(new TriggerKey(triggerName, triggerGroupName));// 停止触发器
			sched.unscheduleJob(new TriggerKey(triggerName, triggerGroupName));// 移除触发器
			sched.deleteJob(new JobKey(jobName, jobGroupName));// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 立刻执行一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param data
	 *            执行参数
	 */
	public static void startJobNow(String jobName, String jobGroupName,
			JobDataMap data) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.triggerJob(JobKey.jobKey(jobName, jobGroupName), data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:启动所有定时任务
	 * 
	 * 
	 */
	public static void startJobs() {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:关闭所有定时任务
	 * 
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isStarted() {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			return sched.isStarted();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
