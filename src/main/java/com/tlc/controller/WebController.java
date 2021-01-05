package com.tlc.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tlc.service.H2Service;

@Controller
public class WebController {
	
	@Autowired
	private H2Service h2Service;
	
	/**
	 * 首页
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String index(Model model) {
		return "index";
	}
	
	/**
	 * 主题页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/theme", method=RequestMethod.GET)
	public String changeTheme(HttpServletRequest request, HttpServletResponse response){
		//获取cookies
		String theme = "white";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("theme")) {
					theme = cookie.getValue();
				}
			}
		}
		Cookie cookie = new Cookie("theme", theme);
		response.addCookie(cookie);
		return "theme";
	}
	
	/**
	 * 登录页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) {
		return "login";
	}
	
	/**
	 * 登录验证
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login/valid", method=RequestMethod.POST)
	public String loginValid(HttpServletRequest request, HttpServletResponse response, Model model,
			@PathParam(value = "username") String username, @PathParam(value = "password") String password) {
		String msg = new String();
		try {
			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				return "账号密码不能为空";
			} else {
				username = username.replaceAll("\\s*", "");
				password = password.replaceAll("\\s*", "");
			}
			
			boolean loginValid = h2Service.loginValid(username, password);
			if (loginValid) {
				Cookie cookie = new Cookie("_xmd", username + ":" + password);
				cookie.setPath("/");
				cookie.setMaxAge(1800);
				response.addCookie(cookie);
			} else {
				Cookie cookie = new Cookie("_xmd", "");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				msg = "账号或密码错误";
			}
		} catch (Exception e) {
			msg = "登录功能异常";
			e.printStackTrace();
		}
		return msg;
	}
	
}
