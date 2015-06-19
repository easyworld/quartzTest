package com.dhu.quartzTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
	@Autowired
	org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean jobtask;

	public void setString(String str) {
		jobtask.setArguments(new Object[] { str });
	}
}
