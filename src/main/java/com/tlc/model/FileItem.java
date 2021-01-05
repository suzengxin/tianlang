package com.tlc.model;

import java.io.Serializable;
import java.util.Date;

import lombok.ToString;

@ToString
public class FileItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
    private Date time;
    private Long size;
    private String path;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}