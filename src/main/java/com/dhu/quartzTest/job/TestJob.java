package com.dhu.quartzTest.job;

import java.util.Random;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("任务成功运行");
		System.out.println("任务名称 = ["
				+ context.getJobDetail().getKey().getName() + "]");
		System.out
				.println("任务输出 = ["
						+ context.getMergedJobDataMap()
								.getString("str") + "]");
	}

	public void workRandom() {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 100; i++)
			sb.append((char) (r.nextInt(95) + 32));
		System.out.println(sb.toString());
	}
}
