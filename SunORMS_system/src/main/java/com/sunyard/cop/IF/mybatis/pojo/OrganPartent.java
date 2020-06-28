/**
 * 
 */
package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * @author YZ 
 * 2017年3月21日 下午1:35:57
 */
public class OrganPartent implements Serializable {

	private static final long serialVersionUID = 7227271954375200907L;

	private String organNo;

	private String parentOrgan;

	private String organLevel;

	private String lastModiDate;

	private String bankNo;

	private String systemNo;

	private String projectNo;

	public OrganPartent() {

	}

	public String getOrganNo() {
		return organNo;
	}

	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}

	public String getParentOrgan() {
		return parentOrgan;
	}

	public void setParentOrgan(String parentOrgan) {
		this.parentOrgan = parentOrgan;
	}

	public String getOrganLevel() {
		return organLevel;
	}

	public void setOrganLevel(String organLevel) {
		this.organLevel = organLevel;
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
