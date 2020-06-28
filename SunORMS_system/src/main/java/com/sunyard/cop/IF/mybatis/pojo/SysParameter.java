/**
 * 
 */
package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * 
 * @author YZ
 * 2017年3月21日  下午1:53:56
 */
public class SysParameter implements Serializable {
	
	private static final long serialVersionUID = -8399660577532594911L;

	private String paramItem;
	
    private String paramValue;

    private String isModify;

    private String modifyUser;

    private String paramDesc;

    private String lastModiDate;

    private String contentAbout;
    
    private String systemNo;

    private String bankNo;

    private String projectNo;
    
    private String choosable;
	
	public SysParameter() {
		
	}

	public String getParamItem() {
		return paramItem;
	}

	public void setParamItem(String paramItem) {
		this.paramItem = paramItem;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getIsModify() {
		return isModify;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

	public String getLastModiDate() {
		return lastModiDate;
	}

	public void setLastModiDate(String lastModiDate) {
		this.lastModiDate = lastModiDate;
	}

	public String getContentAbout() {
		return contentAbout;
	}

	public void setContentAbout(String contentAbout) {
		this.contentAbout = contentAbout;
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

	public String getChoosable() {
		return choosable;
	}

	public void setChoosable(String choosable) {
		this.choosable = choosable;
	}
}
