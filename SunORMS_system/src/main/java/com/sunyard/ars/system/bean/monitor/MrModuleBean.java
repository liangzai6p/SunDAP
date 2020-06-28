package com.sunyard.ars.system.bean.monitor;

import java.io.Serializable;

/**
 * 自定义模板管理 实体类
 * 
 * @author:	lx
 * @date:	2019年5月14日 上午11:33:34
 */
public class MrModuleBean implements Serializable {

	/** 序列号 */
	private static final long serialVersionUID = -3369681613229764620L;
	/** 菜单名称 */
	private String menu_name;
	/** 菜单描述 */
	private String menu_desc;
	/** 菜单编号 */
	private String menu_id;
	/** 文件路径 */
	private String menu_url;
	/** 启用标志 */
	private String is_open;
	/** 锁定标识 */
	private String is_lock;
	/** 最后修改日期 */
	private String last_modi_date;
	/** 银行号 */
	private String bank_no;
	/** 系统号 */
	private String system_no;
	/** 项目号 */
	private String project_no;
	
	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getMenu_desc() {
		return menu_desc;
	}

	public void setMenu_desc(String menu_desc) {
		this.menu_desc = menu_desc;
	}

	public String getMenu_url() {
		return menu_url;
	}

	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
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
