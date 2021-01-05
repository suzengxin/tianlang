package com.tlc.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tlc.model.FileItem;
import com.tlc.service.FileService;

@Service(value = "LINUX")
public class LinuxServiceImpl implements FileService{

	@Override
	public List<FileItem> queryFileItems(String path) throws Exception {
		ArrayList<FileItem> list = new ArrayList<FileItem>();
		try {
			File file = new File(path);
			//判断路径是否存在
			if (file.exists()) {
				File[] files = file.listFiles();
		        if (files != null) {
		        	for (File f : files) {
		        		FileItem FileItem = new FileItem();
			            FileItem.setName(f.getName());
			            FileItem.setSize(f.length());
			            FileItem.setTime(new Date(f.lastModified()));
			            if (!f.isFile()) {
			            	FileItem.setPath(path + "/" + f.getName());
			            }
			            list.add(FileItem);
			        }
		        }
	        }
			//路径不存在的情况下自动创建
			else {
				file.mkdirs();
			}
		} catch (Exception e) {
			throw new Exception("查询文件列表不正确："+path, e);
		}
		return list;
	}

	@Override
	public File getFile(String path) throws Exception {
		try {
			return new File(path);
		} catch (Exception e) {
			throw new Exception("获取文件功能异常："+path, e);
		}
	}

	@Override
	public void createDelFile(String path) throws Exception {
		try {
			File file = new File(path);
			if (file.exists()) {
				deleteFile(path);
			}
			file.mkdirs();
		} catch (Exception e) {
			throw new Exception("新建文件夹功能异常：" + path, e);
		}
	}
	
	@Override
	public void deleteFile(String path) throws Exception {
		try {
			File file = new File(path);
			//判断文件夹是否存在
			if (file.exists()) {
				//判断路径是否是目录
				if (file.isDirectory()) {
					//获取目录下的文件
					String[] list = file.list();
					//先删除目录下的文件
					for (String name : list) {
						String childPath = path + "/" + name;
						deleteFile(childPath);
					}
					//在删除目录
					file.delete();
				} else {
					file.delete();
				}
			}
		} catch (Exception e) {
			throw new Exception("删除文件夹功能异常：" + path, e);
		}
	}

	@Override
	public void createFile(String path, byte[] bytes) throws Exception {
		try {
			FileOutputStream outputStream = new FileOutputStream(path);
			outputStream.write(bytes);
			outputStream.close();
		} catch (Exception e) {
			throw new Exception("新建文件功能异常："+path, e);
		}
	}

}
