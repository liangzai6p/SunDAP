package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

public class PageElement implements Serializable {

	private static final long serialVersionUID = 257007539884431721L;
	
	private String elementId;
	
	private String labelName;
	
	private String elementName;
	
	private String elementType;
	
	private String elementHtml;
	
	private String elementJs;
	
	private String systemNo;

    private String bankNo;

    private String projectNo;

	
	public PageElement(){
		
	}
	
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getElementHtml() {
		return elementHtml;
	}

	public void setElementHtml(String elementHtml) {
		this.elementHtml = elementHtml;
	}

	public String getElementJs() {
		return elementJs;
	}

	public void setElementJs(String elementJs) {
		this.elementJs = elementJs;
	}

	public String getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
