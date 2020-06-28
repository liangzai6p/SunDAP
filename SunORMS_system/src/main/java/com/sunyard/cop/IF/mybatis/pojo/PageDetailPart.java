package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * 拖拽页面对象
 * 
 * @author zxx
 */
public class PageDetailPart implements Serializable {

	private static final long serialVersionUID = -6172754546571481980L;

	private String pageId;

	/* 对应的model中的容器ID */
	private String containerId;

	/* 容器中的HTML内容 */
	private String content;

	private String pageVersion;

	private String bankNo;

	private String systemNo;

	private String projectNo;

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPageVersion() {
		return pageVersion;
	}

	public void setPageVersion(String pageVersion) {
		this.pageVersion = pageVersion;
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

}
