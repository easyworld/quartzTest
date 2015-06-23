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

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @Description: 定时任务管理类
 * 
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2015
 * 
 * @author modified by Comsys-LZP's code.
 */
public class QuartzManager {

	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

	private static String JOB_GROUP_NAME = "WEB_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "WEB_TRIGGERGROUP_NAME";

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName
	 *            任务名
	 * @param cls
	 *            任务，需要继承job
	 * @param map
	 *            任务数据map
	 * @param time
	 *            cron表达式
	 */
	public static void addJob(String jobName, Class<? extends Job> cls,
			JobDataMap map, String time) {
		try {

			Scheduler sched = schedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(cls).setJobData(map)
					.withIdentity(jobName, JOB_GROUP_NAME).build(); // 任务名，任务组，任务执行类

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					.cronSchedule(time);// 触发器时间设定
			// 触发器
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(jobName, TRIGGER_GROUP_NAME)
					.withSchedule(scheduleBuilder).build();// 触发器名,触发器组
			sched.scheduleJob(jobDetail, trigger);
			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
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
	public static void modifyJobTime(String jobName, String time) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched
					.getTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));

			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(new JobKey(jobName,
						JOB_GROUP_NAME));
				Class<? extends Job> objJobClass = jobDetail.getJobClass();
				JobDataMap map = jobDetail.getJobDataMap();
				removeJob(jobName);
				addJob(jobName, objJobClass, map, time);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 暂停一个任务
	 * 
	 * @param jobName
	 */
	public static void pauseJob(String jobName) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
			sched.pauseJob(jobKey);
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
	 * @param jobName
	 */
	public static void resumeJob(String jobName) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
			sched.resumeJob(jobKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 恢复一个任务
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
	 * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * 
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.pauseTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));// 停止触发器
			sched.unscheduleJob(new TriggerKey(jobName, TRIGGER_GROUP_NAME));// 移除触发器
			sched.deleteJob(new JobKey(jobName, JOB_GROUP_NAME));// 删除任务
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
}
