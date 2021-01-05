package com.tlc.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tlc.config.PropertysConfig;
import com.tlc.config.SystemFactory;
import com.tlc.model.FileItem;
import com.tlc.service.FileService;
import com.tlc.utils.FileUtil;

@Controller
@RequestMapping(value = "/music")
public class MusicController {
	
	@Autowired
	private SystemFactory systemFactory;
	
	/**
	 * 音乐页面
	 * @param model
	 * @return
	 */
	@GetMapping
	public String index(Model model) {
		List<FileItem> result = new ArrayList<FileItem>();
		try {
			FileService fileService = systemFactory.getFileService(PropertysConfig.SYSTEM);
			List<FileItem> list = fileService.queryFileItems(PropertysConfig.SYSTEM_MUSIC_PATH);
			for (FileItem file : list) {
				if (file.getName().indexOf(".mp3") != -1) {
		            file.setUrl("/music/single/"+file.getName());
					result.add(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("musics", result);
		return "music";
	}
	
	/**
	 * 获取歌曲
	 * @param name
	 * @return
	 */
	@GetMapping("/single/{name}")
	public ResponseEntity<Object> querySingleFile(@PathVariable(value = "name") String name) {
		try {
			String path = PropertysConfig.SYSTEM_MUSIC_PATH + "/" + name;
			FileService fileService = systemFactory.getFileService(PropertysConfig.SYSTEM);
			File file = fileService.getFile(path);
	        ResponseEntity<Object> export = FileUtil.export(file);
	        return export;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	/**
	 * 获取歌词
	 * @param name
	 * @return
	 */
	@GetMapping("/lrc/{name}")
	public ResponseEntity<Object> queryLrcFile(@PathVariable(value = "name") String name) {
		try {
			String path = PropertysConfig.SYSTEM_MUSIC_PATH + "/" + name;
			FileService fileService = systemFactory.getFileService(PropertysConfig.SYSTEM);
			File file = fileService.getFile(path);

			String result = new String();
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            byte[] b = new byte[1024];
	            int n;
	            while ((n = fis.read(b)) != -1) {
	                bos.write(b, 0, n);
	            }
	            fis.close();
	            bos.close();
	            byte[] buffer = bos.toByteArray();
	            result = new String(buffer, "GBK");
			}
			
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
}
