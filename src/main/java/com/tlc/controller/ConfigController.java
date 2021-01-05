package com.tlc.controller;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tlc.config.PropertysConfig;
import com.tlc.config.SystemFactory;
import com.tlc.model.ResultResponse;
import com.tlc.service.FileService;
import com.tlc.utils.TianlangUtils;

@Controller
@RequestMapping(value = "/config")
public class ConfigController {
	
	@Autowired
	private SystemFactory systemFactory;
	
	/**
	 * 服务管理首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String config() {
		return "config";
	}

	/**
	 * 书籍上传功能
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/book/upload", method=RequestMethod.POST)
	public ResultResponse bookUpload(String author, String book, MultipartFile file) {
		ResultResponse result = null;
		if (StringUtils.isBlank(author)) {
			result = new ResultResponse(ResultResponse.FAIL, "书籍作者不能为空", null);
			return result;
		}
		if (StringUtils.isBlank(book)) {
			result = new ResultResponse(ResultResponse.FAIL, "书籍名称不能为空", null);
			return result;
		}
		if (file.getSize() == 0) {
			result = new ResultResponse(ResultResponse.FAIL, "书籍文件不能为空", null);
			return result;
		}
		
		try {
			long start = new Date().getTime();
			//解析书籍转化成章
			Map<String, String> map = TianlangUtils.BookUploadParseText(file.getInputStream());
			//获取service
			FileService service = systemFactory.getFileService(PropertysConfig.SYSTEM);
			//获取系统路径
			String path = PropertysConfig.SYSTEM_BOOK_PATH;
			//判断书籍文件夹是否存在，存在删除文件夹，不存在创建文件夹
			String bookPath = path + "/" + author + "/" + book;
			service.createDelFile(bookPath);
			//将全本存入文件夹
			String allNodesPath = bookPath + "/" + "0__all.txt";
			byte[] allNodesBytes = TianlangUtils.toByteArray(file.getInputStream());
			service.createFile(allNodesPath, allNodesBytes);
			
			//将章节文件存入文件夹
			int row = 1;
			Pattern pattern = Pattern.compile("[\\\\/:\\*\\?\\\"<>\\|\\#]");
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				//获取章节内容
				String content = map.get(key);
				byte[] nodeBytes = content.getBytes("UTF-8");
				
				//获取章节名称
				Matcher matcher = pattern.matcher(key);
				String fileName = matcher.replaceAll("");
				
				//拼接路径
				String filePath = bookPath + "/" + row + "__" + fileName + ".txt";
				
				//创建文件
				service.createFile(filePath, nodeBytes);
				
				row = row + 1;
			}
			long end = new Date().getTime();
	        System.out.println("书籍："+book+"\r\n大小："+allNodesBytes.length/1024+"KB\r\n耗时：" + (int) ((end - start) / 1000) + "秒");
			result = new ResultResponse(ResultResponse.SUCCESS, ResultResponse.REQUEST_SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResultResponse(ResultResponse.FAIL, ResultResponse.REQUEST_FAIL, null);
		}
		
		return result;
	}
	
}
