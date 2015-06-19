package com.dhu.quartzTest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dhu.quartzTest.service.TestService;

@Controller
public class TestController {
	@Autowired
	private TestService testService;

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
		testService.setString(param);
		return "success";
	}
}
