package com.dhu.quartzTest.controller;

import java.util.List;

import net.sf.json.JSONArray;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dhu.quartzTest.job.TestJob;
import com.dhu.quartzTest.util.Constant;
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
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("plist", QuartzManager.getPlannedJobList());
		mav.addObject("rlist", QuartzManager.getRunningJobList());
		return mav;
	}

	/**
	 * 查询计划中任务
	 * 
	 * @return
	 */
	@RequestMapping(value = "getPlannedJobList", produces = "text/html;charset=UTF-8")
	public @ResponseBody String getPlannedJobList() {
		List<?> list = QuartzManager.getPlannedJobList();
		String json = JSONArray.fromObject(list).toString();
		return json;
	}

	/**
	 * 查询正在执行的任务
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "getRunningJobList", produces = "text/html;charset=UTF-8")
	public @ResponseBody String getRunningJobList(String param) {
		List<?> list = QuartzManager.getRunningJobList();
		String json = JSONArray.fromObject(list).toString();
		return json;
	}

	@RequestMapping(value = "addJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String addJob(String name, String group, String time,
			String str) {
		JobDataMap map = new JobDataMap();
		map.put("str", str);
		try {
			QuartzManager.addJob(name, group,
					String.valueOf(System.currentTimeMillis()),
					Constant.TRIGGER_GROUP_NAME, TestJob.class, map, time);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "start", produces = "text/html;charset=UTF-8")
	public @ResponseBody String start() {
		try {
			if (QuartzManager.isStarted()) {
				QuartzManager.shutdownJobs();
				return Constant.SHUTDOWN;
			} else {
				QuartzManager.startJobs();
				return Constant.START;
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
