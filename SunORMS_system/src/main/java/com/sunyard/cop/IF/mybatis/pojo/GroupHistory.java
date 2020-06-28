package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

public class GroupHistory implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 181367888510245870L;

	private String chatId;

    private String bankNo;

    private String systemNo;

    private String projectNo;

    private String chatFrom;

    private String sendTime;

    private String groupId;

    private String chatContent;


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

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getChatContent() {
		return chatContent;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}
 
}