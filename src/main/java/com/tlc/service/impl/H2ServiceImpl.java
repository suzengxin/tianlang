package com.tlc.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.tlc.config.MySqlDao;
import com.tlc.service.H2Service;

@Service
public class H2ServiceImpl implements H2Service{
	
	@Autowired
	private MySqlDao mySqlDao;
	
	@Override
	public boolean loginValid(String username, String password) throws Exception {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("username", username);
			params.addValue("password", password);
			String sql = "SELECT * FROM USER_INFO WHERE username = :username AND password = :password;";
			List<Map<String, Object>> list = mySqlDao.getNamedParamterDao().queryForList(sql, params);
			if (list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new Exception("登录验证功能异常", e);
		}
	}

}
