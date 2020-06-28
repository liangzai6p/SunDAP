package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

public class Button implements Serializable {

	/** 序列号 */
	private static final long serialVersionUID = -756419963479064702L;
	
	private String buttonId;
	
	private String buttonName ;
	
	private String menuId;
    
    private String isOpen;
    
    private String isLock;
    
    private String lastModiDate;
    
    private String systemNo;

    private String bankNo;

    private String projectNo;

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public String getIsLock() {
		return isLock;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public String getLastModiDate() {
		return lastModiDate;
	}

	public void setLastModiDate(String lastModiDate) {
		this.lastModiDate = lastModiDate;
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
    
}
