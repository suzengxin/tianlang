package com.tlc.service;

import java.io.File;
import java.util.List;

import com.tlc.model.FileItem;

public interface FileService {

	/**
	 * 查询文件列表
	 * @param path 路径
	 * @return
	 * @throws Exception
	 */
	public List<FileItem> queryFileItems(String path) throws Exception;

	/**
	 * 获取文件
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public File getFile(String path) throws Exception;
	
	/**
	 * 文件夹存在的情况下
	 * 先删除文件夹再新建文件夹
	 * @param path
	 * @throws Exception
	 */
	public void createDelFile(String path) throws Exception;
	
	/**
	 * 删除文件
	 * @param path
	 * @throws Exception
	 */
	public void deleteFile(String path) throws Exception;
	
	/**
	 * 新建文件
	 * @param path
	 * @param bt
	 * @throws Exception
	 */
	public void createFile(String path, byte[] bytes) throws Exception;
}
