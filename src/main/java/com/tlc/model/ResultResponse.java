package com.tlc.model;

public class ResultResponse {

	public static final Integer FAIL = 0;
	
	public static final Integer SUCCESS = 1;
	
	public static final String REQUEST_FAIL = "请求失败";
	
	public static final String REQUEST_SUCCESS = "请求成功";
	
	Integer status;
	
	String msg;
	
	Object data;

	public ResultResponse() {
		super();
	}
	
	public ResultResponse(Integer status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static Integer getFail() {
		return FAIL;
	}

	public static Integer getSuccess() {
		return SUCCESS;
	}

	public static String getRequestFail() {
		return REQUEST_FAIL;
	}

	public static String getRequestSuccess() {
		return REQUEST_SUCCESS;
	}
	
}
