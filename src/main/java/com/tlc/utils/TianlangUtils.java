package com.tlc.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TianlangUtils {

	/**
	 * 解析文本流
	 * 转化为章节List和内容List
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, String> BookUploadParseText (InputStream inputStream) throws Exception{
		//获取章节名称
		String encoding = "UTF-8";
		InputStreamReader is = new InputStreamReader(inputStream, encoding);
		BufferedReader br = new BufferedReader(is);
		
		//解析流，将章节名称放入章节集合    //nodeNames
		//将文件流以行的格式存入集合  //contents
		List<String> nodeNames = new ArrayList<>();//章节集合
		List<String> contents = new ArrayList<>();//每一行内容集合
		String line = null;
		while ((line = br.readLine()) != null) {
			contents.add(line);
			if ((line.indexOf("第") == 0 && line.indexOf("章") != -1) || 
					(line.indexOf("章") == 0 && line.indexOf(" ") == 1) || 
					(line.indexOf("章") == 1 && line.indexOf(" ") == 2)) {
				nodeNames.add(line);
			}
		}
		
		Map<String, String> result = new LinkedHashMap<String, String>(); 
		int temp = 0;
		for (int i = 0; i < nodeNames.size(); i++) {
			//取出章节名称
			String key = nodeNames.get(i);
			//下一章节名称
			String end = null;
			
			//如果是最后一个章节，放入一个基本不重复的字符串
			if (i == nodeNames.size() - 1) {
				end = UUID.randomUUID().toString();
			} else {
				end = nodeNames.get(i+1);
			}
			
			//本章节内容
			String contentStr = "";
			//循环所有行
			//行中包含章节名称的情况下，打开集合控制器
			//直到行中包含下一章节名称的情况下，关闭控制器
			//控制器判断过后再打开，下一个循环就会存入新的集合中
			for (int j = temp; j < contents.size(); j++) {
				temp = j;
				String content = contents.get(j);
				if (content.equals(end)) {
					break;
				}
				
				String str = content.replaceAll("\\s*", "");
				if (!str.equals("")) {
					contentStr += "<p class=\"content\">" + str + "</p></br>";
				}
			}
			
			result.put(key, contentStr);
		}
		return result;
	}
	
	/**
     * InputStream转化为byte[]数组
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	byte[] buffer = new byte[input.available()];
    	input.read(buffer);
    	out.write(buffer);
        return out.toByteArray();
    }

	public static void main(String[] args) {
		try {
			long start = new Date().getTime();
			String path = "C:\\Users\\Administrator\\Desktop\\all.txt";
			File file = new File(path);
			InputStream inputStream = new FileInputStream(file);
			Map<String, String> map = BookUploadParseText(inputStream);
			Set<String> keySet = map.keySet();
			String pathTxt = "C:\\Users\\Administrator\\Desktop\\all\\";
			
			int row = 1;
			Pattern pattern = Pattern.compile("[\\\\/:\\*\\?\\\"<>\\|\\#]");
			for (String key : keySet) {
				String fileName = key;
				Matcher matcher = pattern.matcher(fileName);
				fileName = matcher.replaceAll("");
				
				String filePath = pathTxt + row + "__" + fileName + ".txt";
				File fileTxt = new File(filePath);
				if (fileTxt.exists()) {
					fileTxt.delete();
				}
				fileTxt.createNewFile();
				FileOutputStream outputStream = new FileOutputStream(fileTxt);
				String content = map.get(key);
				byte[] bytes = content.getBytes("UTF-8");
				outputStream.write(bytes);
				outputStream.close();
				row = row + 1;
			}
			long end = new Date().getTime();
	        System.out.println("上传"+file.length()/1024+"KB文件耗时" + (int) ((end - start) / 1000) + "秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
