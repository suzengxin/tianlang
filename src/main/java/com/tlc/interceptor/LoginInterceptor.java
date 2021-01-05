package com.tlc.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tlc.service.H2Service;

public class LoginInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private H2Service h2Service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			String username = new String();
			String password = new String();
			
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("_xmd")) {
						String value = cookie.getValue();
						String[] split = value.split(":");
						if (split.length == 2) {
							username = split[0];
							password = split[1];
						}
						break;
					}
				}
			}
			
			if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
				boolean loginValid = h2Service.loginValid(username, password);
				if (loginValid) {
					Cookie cookie = new Cookie("_xmd", username + ":" + password);
					cookie.setPath("/");
					cookie.setMaxAge(1800);
					response.addCookie(cookie);
					return true;
				} else {
					Cookie cookie = new Cookie("_xmd", "");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
			
			response.sendRedirect("/login");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
