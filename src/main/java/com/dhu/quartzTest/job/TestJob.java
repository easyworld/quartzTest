package com.dhu.quartzTest.job;

import java.util.Random;

public class TestJob {
	public void workRandom() {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 100; i++)
			sb.append((char) (r.nextInt(95) + 32));
		System.out.println(sb.toString());
	}

	public void word(String str) {
		System.out.println("In quartz " + str);
	}
}
