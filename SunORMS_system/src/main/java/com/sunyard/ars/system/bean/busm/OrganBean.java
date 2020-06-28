package com.sunyard.ars.system.bean.busm;

import java.io.Serializable;

/**
 * @author: 	wwx
 * @Description:TODO(机构管理bean类)
 * @date:		2017年3月6日上午11:34:04
 */
public class OrganBean implements Serializable {
	
	// 序列号
	private static final long serialVersionUID = -264758009249170164L;
	// 机构编号
	private String organ_no;  
	// 机构名称
	private String organ_name;   
	// 机构简称
	private String shname;  
	// 机构等级
	private Integer organ_level;
	// 机构类型
	private String  organ_type;
	// 上级机构号
	private String  parent_organ;
	// 状态
	private String  status;
	//
	private String  brnrgncod;
	// 最后修改日期
	private String  last_modi_date;
	//银行标识
	private String  bank_no;
	// 系统标识
	private String  system_no;
	// 项目标识
	private String  project_no;
	//移动机构号
	private String dragOrgan;
	// 原父机构号
	private String dragParOrgan;
	// 新父机构号
	private String dragedNewParOrgan;
	
	private Integer is_center;
	private Integer is_zy;
	public String getDragOrgan() {
		return dragOrgan;
	}

	public void setDragOrgan(String dragOrgan) {
		this.dragOrgan = dragOrgan;
	}

	public String getDragParOrgan() {
		return dragParOrgan;
	}

	public void setDragParOrgan(String dragParOrgan) {
		this.dragParOrgan = dragParOrgan;
	}

	public String getDragedNewParOrgan() {
		return dragedNewParOrgan;
	}

	public void setDragedNewParOrgan(String dragedNewParOrgan) {
		this.dragedNewParOrgan = dragedNewParOrgan;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOrgan_no() {
		return organ_no;
	}
	
	public void setOrgan_no(String organ_no) {
		this.organ_no = organ_no;
	}
	
	public String getOrgan_name() {
		return organ_name;
	}
	
	public void setOrgan_name(String organ_name) {
		this.organ_name = organ_name;
	}
	
	public String getShname() {
		return shname;
	}
	
	public void setShname(String shname) {
		this.shname = shname;
	}
	
	public Integer getOrgan_level() {
		return organ_level;
	}
	
	public void setOrgan_level(Integer organ_level) {
		this.organ_level = organ_level;
	}
	
	public String getOrgan_type() {
		return organ_type;
	}
	
	public void setOrgan_type(String organ_type) {
		this.organ_type = organ_type;
	}
	
	public String getParent_organ() {
		return parent_organ;
	}
	
	public void setParent_organ(String parent_organ) {
		this.parent_organ = parent_organ;
	}
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBrnrgncod() {
		return brnrgncod;
	}
	
	public void setBrnrgncod(String brnrgncod) {
		this.brnrgncod = brnrgncod;
	}
	
	public String getLast_modi_date() {
		return last_modi_date;
	}
	
	public void setLast_modi_date(String last_modi_date) {
		this.last_modi_date = last_modi_date;
	}
	
	public String getBank_no() {
		return bank_no;
	}
	
	public void setBank_no(String bank_no) {
		this.bank_no = bank_no;
	}
	
	public String getSystem_no() {
		return system_no;
	}
	
	public void setSystem_no(String system_no) {
		this.system_no = system_no;
	}
	
	public String getProject_no() {
		return project_no;
	}
	
	public void setProject_no(String project_no) {
		this.project_no = project_no;
	}

	public Integer getIs_center() {
		return is_center;
	}

	public void setIs_center(Integer is_center) {
		this.is_center = is_center;
	}

	public Integer getIs_zy() {
		return is_zy;
	}

	public void setIs_zy(Integer is_zy) {
		this.is_zy = is_zy;
	}
	
}
