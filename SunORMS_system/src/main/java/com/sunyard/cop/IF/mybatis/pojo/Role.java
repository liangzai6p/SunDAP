/**
 * 
 */
package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * @author YZ 
 * 2017年3月21日 下午1:51:48
 */
public class Role implements Serializable {

	private static final long serialVersionUID = 6663892215777831914L;

	private String roleNo;
	
	private String roleName;

	private String roleDes;

	private String roleMode;
	
	private String isOpen;
	
	private String lastModiDate;
	
	private String bankNo;

	private String systemNo;

	private String projectNo;

	/**
	 * 
	 */
	public Role() {
		
	}

	public String getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDes() {
		return roleDes;
	}

	public void setRoleDes(String roleDes) {
		this.roleDes = roleDes;
	}

	public String getRoleMode() {
		return roleMode;
	}

	public void setRoleMode(String roleMode) {
		this.roleMode = roleMode;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
