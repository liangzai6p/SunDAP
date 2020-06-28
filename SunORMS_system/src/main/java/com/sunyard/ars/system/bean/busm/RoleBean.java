package com.sunyard.ars.system.bean.busm;

import java.io.Serializable;

/**
 * @author:		 wwx
 * @date:		 2017年12月19日 下午5:05:13
 * @description: TODO(RoleBean)
 */
public class RoleBean implements Serializable {

	/** 序列号 */
	private static final long serialVersionUID = -3535272297787180385L;
	
	/** 角色号  */
	private String role_no;
	/** 角色名  */
	private String role_name;
	/** 描述  */
	private String role_des;
	
	//角色级别
	public String getRole_level() {
		return role_level;
	}
	public void setRole_level(String role_level) {
		this.role_level = role_level;
	}
	private String role_level;
	/** 类别  */
	private String role_mode;
	
	/** 启用标志  */
	private String is_open;
	/** 锁定标识  */
	private String is_lock;
	/** 最后修改日期  */
	private String last_modi_date;
	/** 银行号  */
	private String bank_no;
	/** 系统号  */
	private String system_no;
	/** 项目号  */
	private String project_no;
	
	public String getRole_no() {
		return role_no;
	}
	public void setRole_no(String role_no) {
		this.role_no = role_no;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getRole_des() {
		return role_des;
	}
	public void setRole_des(String role_des) {
		this.role_des = role_des;
	}
	public String getRole_mode() {
		return role_mode;
	}
	public void setRole_mode(String role_mode) {
		this.role_mode = role_mode;
	}
	public String getIs_open() {
		return is_open;
	}
	public void setIs_open(String is_open) {
		this.is_open = is_open;
	}
	public String getIs_lock() {
		return is_lock;
	}
	public void setIs_lock(String is_lock) {
		this.is_lock = is_lock;
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
}
