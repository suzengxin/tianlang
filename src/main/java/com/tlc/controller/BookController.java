package com.tlc.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tlc.config.PropertysConfig;
import com.tlc.config.SystemFactory;
import com.tlc.model.FileItem;
import com.tlc.service.FileService;

@Controller
@RequestMapping(value = "/book")
public class BookController {
	
	@Autowired
	private SystemFactory systemFactory;

	/**
	 * 书籍首页
	 * @param model
	 * @return
	 */
	@GetMapping(value = "")
	public String book(Model model) {
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			
			FileService fileService = systemFactory.getFileService(PropertysConfig.SYSTEM);
			String path = PropertysConfig.SYSTEM_BOOK_PATH;
			List<FileItem> list = fileService.queryFileItems(path);
			for (FileItem fileItem : list) {
				String author = fileItem.getName();
				String pathUrl = path + "/" + author;
				List<FileItem> bookList = fileService.queryFileItems(pathUrl);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", author);
				map.put("books", bookList);
				result.add(map);
			}
			
			model.addAttribute("authors", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "book";
	}
	
	/**
	 * 根据作家名称获取书籍列表
	 * @param model
	 * @return
	 */
	@GetMapping("/{author}")
	public String author(Model model, @PathVariable(value = "author") String author) {
		try {
			FileService fileService = systemFactory.getFileService(PropertysConfig.SYSTEM);
			String path = PropertysConfig.SYSTEM_BOOK_PATH + "/" + author;
			List<FileItem> list = fileService.queryFileItems(path);
			model.addAttribute("books", list);
			model.addAttribute("author", author);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "book_author";
	}
	
	/**
	 * 根据书籍名称获取章节列表
	 * @param model
	 * @return
	 */
	@GetMapping("/{author}/{book}")
	public String node(Model model, @PathVariable(value = "author") String author, @PathVariable(value = "book") String book) {
		try {
			FileService fileService = systemFactory.getFileService(PropertysConfig.SYSTEM);
			String path = PropertysConfig.SYSTEM_BOOK_PATH + "/" + author +"/" + book;
			List<FileItem> list = fileService.queryFileItems(path);
			List<FileItem> result = new ArrayList<FileItem>();
			for (int i = 1; i < list.size(); i++) {
				//根据序号排序
				for (FileItem fileItem : list) {
					String fileName = fileItem.getName();
					if (fileName.indexOf("__") != -1) {
						int index = Integer.parseInt(fileName.split("__")[0]);
						if (index == i) {
							result.add(fileItem);
							break;
						}
					}
				}
			}
			model.addAttribute("nodes", result);
			model.addAttribute("author", author);
			model.addAttribute("book", book);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "book_node";
	}
	
	/**
	 * 根据章节名称获取章节详情
	 * @param model
	 * @return
	 */
	@GetMapping("/{author}/{book}/{node}")
	public String content(Model model, @PathVariable(value = "author") String author, @PathVariable(value = "book") String book, @PathVariable(value = "node") String node) {
		try {
			FileService fileService = systemFactory.getFileService(PropertysConfig.SYSTEM);
			String path = PropertysConfig.SYSTEM_BOOK_PATH + "/" + author + "/" + book + "/" + node + ".txt";
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
	            result = new String(buffer, "UTF-8");
			}
			
			model.addAttribute("content", result);
			model.addAttribute("author", author);
			model.addAttribute("book", book);
			model.addAttribute("node", node);
			
			String pathBook = PropertysConfig.SYSTEM_BOOK_PATH + "/" + author + "/" + book;
			Map<String, String> map = getPrevNextNode(pathBook, node);
			model.addAttribute("prev", map.get("prev"));
			model.addAttribute("next", map.get("next"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "book_content";
	}
	
	private Map<String, String> getPrevNextNode(String path, String node) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("prev", node);
		result.put("next", node);
		try {
			FileService fileService = systemFactory.getFileService(PropertysConfig.SYSTEM);
			List<FileItem> list = fileService.queryFileItems(path);
			List<FileItem> nodeNames = new ArrayList<FileItem>();
			for (int i = 1; i < list.size(); i++) {
				for (FileItem fileItem : list) {
					String fileName = fileItem.getName();
					int index = Integer.parseInt(fileName.split("__")[0]);
					if (index == i) {
						nodeNames.add(fileItem);
						break;
					}
				}
			}
			
			for (int i = 0; i < nodeNames.size(); i++) {
				String nodeName = nodeNames.get(i).getName();
				if (nodeName.equals(node+".txt")) {
					if (nodeNames.size() > 1) {
						if (i > 0 && i != nodeNames.size() - 1) {
							result.put("prev", nodeNames.get(i - 1).getName().replaceAll(".txt", ""));
							result.put("next", nodeNames.get(i + 1).getName().replaceAll(".txt", ""));
						} else if (i == 0) {
							result.put("prev", node);
							result.put("next", nodeNames.get(i + 1).getName().replaceAll(".txt", ""));
						} else if (i == nodeNames.size() - 1) {
							result.put("prev", nodeNames.get(i - 1).getName().replaceAll(".txt", ""));
							result.put("next", node);
						} else {
							result.put("prev", node);
							result.put("next", node);
						}
					} else {
						result.put("prev", node);
						result.put("next", node);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
