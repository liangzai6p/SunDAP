package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

public class ChatHistory  implements Serializable{
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 2216034130073494024L;

	private String chatId;

    private String bankNo;

    private String systemNo;

    private String projectNo;

    private String chatFrom;

    private String chatTo;

    private String sendTime;

    private String status;

    private String chatcontent;

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getChatFrom() {
		return chatFrom;
	}

	public void setChatFrom(String chatFrom) {
		this.chatFrom = chatFrom;
	}

	public String getChatTo() {
		return chatTo;
	}

	public void setChatTo(String chatTo) {
		this.chatTo = chatTo;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChatcontent() {
		return chatcontent;
	}

	public void setChatcontent(String chatcontent) {
		this.chatcontent = chatcontent;
	}

    
}