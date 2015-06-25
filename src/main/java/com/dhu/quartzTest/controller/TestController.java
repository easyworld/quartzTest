package com.dhu.quartzTest.controller;

import java.util.List;

import net.sf.json.JSONArray;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
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
		try {
			mav.addObject("plist", QuartzManager.getPlannedJobList());
			mav.addObject("rlist", QuartzManager.getRunningJobList());
		} catch (SchedulerException e) {
			// do nothing
		}
		return mav;
	}

	/**
	 * 查询计划中任务
	 * 
	 * @return
	 */
	@RequestMapping(value = "getPlannedJobList", produces = "text/html;charset=UTF-8")
	public @ResponseBody String getPlannedJobList() {
		try {
			List<?> list = QuartzManager.getPlannedJobList();
			String json = JSONArray.fromObject(list).toString();
			return json;
		} catch (SchedulerException e) {
			return "";
		}
	}

	/**
	 * 查询正在执行的任务
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "getRunningJobList", produces = "text/html;charset=UTF-8")
	public @ResponseBody String getRunningJobList(String param) {
		try {
			List<?> list = QuartzManager.getRunningJobList();
			String json = JSONArray.fromObject(list).toString();
			return json;
		} catch (SchedulerException e) {
			return "";
		}
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

	@RequestMapping(value = "editJobTime", produces = "text/html;charset=UTF-8")
	public @ResponseBody String editJobTime(String name, String group,
			String time) {
		try {
			QuartzManager.modifyJobTime(name, group, time);
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

	@RequestMapping(value = "isStart", produces = "text/html;charset=UTF-8")
	public @ResponseBody String isStart() {
		try {
			return QuartzManager.isStarted() ? "true" : "false";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "pauseJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String pauseJob(String name, String group) {
		try {
			QuartzManager.pauseJob(name, group);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "resumeJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String resumeJob(String name, String group) {
		try {
			QuartzManager.resumeJob(name, group);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "deleteJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String deleteJob(String name, String group) {
		try {
			QuartzManager.removeJob(name, group);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 立刻执行任务
	 * 
	 * @param name
	 * @param group
	 * @return
	 */
	@RequestMapping(value = "runJobNow", produces = "text/html;charset=UTF-8")
	public @ResponseBody String runJobNow(String name, String group) {
		try {
			JobDataMap map = QuartzManager.getOneJobMap(name, group);
			QuartzManager.startJobNow(name, group, map);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
