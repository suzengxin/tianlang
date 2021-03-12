package com.tlc.service;

import java.util.List;
import java.util.Map;

public interface H2Service {

	/**
	 * 登陆验证
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean loginValid(String username, String password) throws Exception;
	
	/**
	 * 加载首页数据
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> loadPoetry() throws Exception;
	
}
