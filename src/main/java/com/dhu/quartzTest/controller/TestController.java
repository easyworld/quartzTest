package com.dhu.quartzTest.controller;

import java.util.Map;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dhu.quartzTest.job.TestJob;
import com.dhu.quartzTest.util.QuartzManager;

@Controller
public class TestController {

	@RequestMapping(value = "404")
	public ModelAndView pageNotFound() {
		return new ModelAndView("404");
	}

	@RequestMapping("error")
	public ModelAndView error() {
		return new ModelAndView("error");
	}

	@RequestMapping("/index")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "test", produces = "text/html;charset=UTF-8")
	public @ResponseBody String test(String param) {
		JobDataMap map = new JobDataMap();
		map.put("str", param);
		QuartzManager.addJob("test1", TestJob.class, map, "*/5 * * * * ?");
		QuartzManager.startJobs();
		return "success";
	}

	@RequestMapping(value = "changetime", produces = "text/html;charset=UTF-8")
	public @ResponseBody String changetime(String param) {
		QuartzManager.modifyJobTime("test1", param);
		return "success";
	}
}
