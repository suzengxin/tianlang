package com.tlc.service;

public interface H2Service {

	/**
	 * 登陆验证
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean loginValid(String username, String password) throws Exception;
	
}
