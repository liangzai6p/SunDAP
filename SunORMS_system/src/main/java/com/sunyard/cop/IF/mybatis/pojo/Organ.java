/**
 * 
 */
package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * 
 * @author YZ
 * 2017年3月21日  下午1:38:03
 */
public class Organ implements Serializable {
	
	private static final long serialVersionUID = 9178303201271011175L;

	private String organNo;

    private String organName;

    private String shname;

    private String organLevel;

    private String organType;

    private String parentOrgan;
    
    private String lastModiDate;
    
    private String bankNo;

    private String systemNo;

    private String projectNo;
	
	/**
	 * 
	 */
	public Organ() {
		// TODO Auto-generated constructor stub
	}

	public String getOrganNo() {
		return organNo;
	}

	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getShname() {
		return shname;
	}

	public void setShname(String shname) {
		this.shname = shname;
	}

	public String getOrganLevel() {
		return organLevel;
	}

	public void setOrganLevel(String organLevel) {
		this.organLevel = organLevel;
	}

	public String getOrganType() {
		return organType;
	}

	public void setOrganType(String organType) {
		this.organType = organType;
	}

	public String getParentOrgan() {
		return parentOrgan;
	}

	public void setParentOrgan(String parentOrgan) {
		this.parentOrgan = parentOrgan;
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
