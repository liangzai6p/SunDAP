package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * 拖拽页面模板对象
 * 
 * @author zxx
 */
public class PageModel implements Serializable {

	private static final long serialVersionUID = -2069003881135142132L;

	private String modelId;

	private String modelName;

	private String modelContent;

	private String modelJs;

	private String modelVersion;

	/* 是否删除：0：是；1：否 */
	private String modelDel;

	private String lastModiDate;

	private String expireDate;

	private String bankNo;

	private String systemNo;

	private String projectNo;

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelContent() {
		return modelContent;
	}

	public void setModelContent(String modelContent) {
		this.modelContent = modelContent;
	}

	public String getModelJs() {
		return modelJs;
	}

	public void setModelJs(String modelJs) {
		this.modelJs = modelJs;
	}

	public String getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}

	public String getModelDel() {
		return modelDel;
	}

	public void setModelDel(String modelDel) {
		this.modelDel = modelDel;
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

}
