package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * 拖拽页面对象
 * 
 * @author zxx
 */
public class PageDetail implements Serializable {

	private static final long serialVersionUID = -6172754546571481980L;

	private String pageId;

	private String pageName;

	/* 对应的model中的容器ID */
	private String containerId;

	/* 容器中的HTML内容 */
	private String content;

	private String pageJs;

	private String pageVersion;

	/* 是否删除：0：是；1：否 */
	private String pageDel;

	private String modelId;

	private String lastModiDate;

	private String expireDate;

	private String bankNo;

	private String systemNo;

	private String projectNo;

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
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

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getPageVersion() {
		return pageVersion;
	}

	public void setPageVersion(String pageVersion) {
		this.pageVersion = pageVersion;
	}

	public String getPageDel() {
		return pageDel;
	}

	public void setPageDel(String pageDel) {
		this.pageDel = pageDel;
	}

	public String getPageJs() {
		return pageJs;
	}

	public void setPageJs(String pageJs) {
		this.pageJs = pageJs;
	}

	public String getLastModiDate() {
		return lastModiDate;
	}

	public void setLastModiDate(String lastModiDate) {
		this.lastModiDate = lastModiDate;
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

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

}
