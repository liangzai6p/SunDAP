/**
 * 
 */
package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * @author YZ 
 * 2017年3月21日 下午1:28:59
 */
public class MenuCustompage implements Serializable {

	private static final long serialVersionUID = 3304950867969321191L;

	private String menuId;

	private String pageId;

	private String lastModiDate;

	private String bankNo;

	private String systemNo;

	private String projectNo;

	/**
	 * 
	 */
	public MenuCustompage() {
		
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
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
