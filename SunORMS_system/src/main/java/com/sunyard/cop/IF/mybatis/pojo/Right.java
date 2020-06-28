/**
 * 
 */
package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * @author YZ 
 * 2017年3月21日 下午1:50:13
 */
public class Right implements Serializable {

	private static final long serialVersionUID = 1583955184443565799L;

	private String menuId;
	
	private String buttonId;

	private String roleNo;

	private String isOpen;
	
	private String isLock;

	private String lastModiDate;
	
	private String bankNo;

	private String systemNo;

	private String projectNo;
	
	private String operNodes;

	/**
	 * 
	 */
	public Right() {
		// TODO Auto-generated constructor stub
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
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

	
	public String getOperNodes() {
		return operNodes;
	}

	public void setOperNodes(String operNodes) {
		this.operNodes = operNodes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getIsLock() {
		return isLock;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

}
