package com.dhu.quartzTest.controller;

import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dhu.quartzTest.job.HttpRequestJob;
import com.dhu.quartzTest.service.QuartzService;
import com.dhu.quartzTest.util.Constant;

@Controller
public class TestController {

	private static Logger _log = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private QuartzService quartzService;

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
			mav.addObject("plist", quartzService.getPlannedJobList());
			mav.addObject("rlist", quartzService.getRunningJobList());
		} catch (SchedulerException e) {
			// do nothing
		}
		return mav;
	}

	/**
	 * cron表达式说明
	 * 
	 * @return
	 */
	@RequestMapping("/cron_readme")
	public ModelAndView readme() {
		return new ModelAndView("cron_readme");
	}

	/**
	 * 查询计划中任务
	 * 
	 * @return
	 */
	@RequestMapping(value = "getPlannedJobList", produces = "text/html;charset=UTF-8")
	public @ResponseBody String getPlannedJobList() {
		try {
			List<?> list = quartzService.getPlannedJobList();
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
			List<?> list = quartzService.getRunningJobList();
			String json = JSONArray.fromObject(list).toString();
			return json;
		} catch (SchedulerException e) {
			return "";
		}
	}

	/**
	 * 获取任务日志
	 * 
	 * @param name
	 * @param group
	 * @return
	 */
	@RequestMapping(value = "getLogs", produces = "text/html;charset=UTF-8")
	public @ResponseBody String getLogs(String name, String group) {
		if (name == null || group == null)
			return "";
		try {
			String json = quartzService.getLogs(name, group);
			return json;
		} catch (SchedulerException e) {
			return "";
		}
	}

	@RequestMapping(value = "addJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String addJob(String name, String group, String time,
			String url, String method,
			@RequestParam(required = false) String params) {
		JobDataMap map = new JobDataMap();
		map.put("url", url);
		map.put("method", method);
		if (params != null && !params.isEmpty())
			map.put("params", params);
		try {
			quartzService.addJob(name, group,
					String.valueOf(System.currentTimeMillis()),
					Constant.TRIGGER_GROUP_NAME, HttpRequestJob.class, map,
					time);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 修改job的cron表达式和url
	 * 
	 * @param name
	 * @param group
	 * @param time
	 * @param url
	 * @param method
	 * @param params
	 *            可为空
	 * @return
	 */
	@RequestMapping(value = "editJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String editJob(String name, String group, String time,
			String url, String method,
			@RequestParam(required = false) String params) {
		try {
			quartzService.modifyJobTime(name, group, time);
			JobDataMap map = new JobDataMap();
			map.put("url", url);
			map.put("method", method);
			if (params != null && !params.isEmpty())
				map.put("params", params);
			quartzService.modifyJobUrl(name, group, map);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "start", produces = "text/html;charset=UTF-8")
	public @ResponseBody String start() {
		try {
			if (quartzService.isInStandbyMode()) {
				quartzService.startJobs();
				return Constant.START;

			} else {
				quartzService.standByJobs();
				return Constant.SHUTDOWN;
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 测试方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "start2", produces = "text/html;charset=UTF-8")
	public @ResponseBody String start2() {
		try {
			quartzService.startJobs();
			return Constant.START;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "isInStandbyMode", produces = "text/html;charset=UTF-8")
	public @ResponseBody String isInStandbyMode() {
		try {
			return quartzService.isInStandbyMode() ? "true" : "false";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "pauseJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String pauseJob(String name, String group) {
		try {
			quartzService.pauseJob(name, group);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "resumeJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String resumeJob(String name, String group) {
		try {
			quartzService.resumeJob(name, group);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "deleteJob", produces = "text/html;charset=UTF-8")
	public @ResponseBody String deleteJob(String name, String group) {
		try {
			quartzService.removeJob(name, group);
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
			JobDataMap map = quartzService.getOneJobMap(name, group);
			quartzService.startJobNow(name, group, map);
			return Constant.SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * url请求测试方法
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/test")
	public @ResponseBody String test(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("method is " + request.getMethod() + "; params:");
		for (Object o : request.getParameterMap().entrySet()) {
			Entry e = (Entry) o;
			sb.append((String) e.getKey() + " = "
					+ ((String[]) (e.getValue()))[0] + ";");
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		_log.info(sb.toString());
		return Constant.SUCCESS;
	}
}
