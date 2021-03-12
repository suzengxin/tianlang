package com.tlc.service.impl;

import java.util.ArrayList;
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

	@Override
	public List<Map<String, Object>> loadPoetry() throws Exception {
		try {
			String poetrySql = "SELECT id, name FROM POETRY;";
			List<Map<String, Object>> portryList = mySqlDao.queryForList(poetrySql);
			
			String poetryContentSql = "SELECT id, poetry_id AS pid, content FROM POETRY_CONTENT;";
			List<Map<String, Object>> poetryContentList = mySqlDao.queryForList(poetryContentSql);
			
			for (Map<String, Object> map : portryList) {
				int id = Integer.parseInt(map.get("id").toString());
				List<Map<String, Object>> contentList = new ArrayList<Map<String,Object>>();

				for (Map<String, Object> content : poetryContentList) {
					int pid = Integer.parseInt(content.get("pid").toString());
					if (pid == id) {
						contentList.add(content);
					}
				}
				
				map.put("CONTENT", contentList);
			}
			
			return portryList;
		} catch (Exception e) {
			throw new Exception("加载首页数据功能异常", e);
		}
	}

}
