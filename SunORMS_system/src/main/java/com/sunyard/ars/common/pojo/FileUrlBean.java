package com.sunyard.ars.common.pojo;

public class FileUrlBean {
	private String fileName; //图像名称
	private String url;      //图像url 
	private int useTime;  //url使用次数
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getUseTime() {
		return useTime;
	}
	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}
	
	
	
}
