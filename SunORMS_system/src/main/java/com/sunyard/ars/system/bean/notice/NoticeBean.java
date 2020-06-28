package com.sunyard.ars.system.bean.notice;

import java.io.Serializable;

/**
 * @author:		 wwx
 * @date:		 2017年12月18日
 * @description: TODO(NoticeBean)
 */
public class NoticeBean implements Serializable {

	/** 序列号  */
	private static final long serialVersionUID = -7497109435380140379L;
	
	/** 公告ID */
	private String notice_id;
	/** 公告标题 */
	private String notice_title;  
	/** 公告内容 */
	private String notice_content;   
	/** 发布时间 */
	private String publish_time;  
	/** 发布机构号 */
	private String publish_organ;
	/** 发布机构名 */
	private String publish_organ_name;
	/** 发布人 */
	private String  publish_user;
	/** 附件 */
	private String  file_url;
	/** 阅读状态 */
	private String  read_state;
	/** 阅读时间 */
	private String  read_time;
	/** 可角色阅 */
	private String  roles;
	/** 可阅机构 */
	private String  organs;
	
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
	
	public String getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}
	public String getNotice_title() {
		return notice_title;
	}
	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}
	public String getNotice_content() {
		return notice_content;
	}
	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}
	public String getPublish_organ() {
		return publish_organ;
	}
	public void setPublish_organ(String publish_organ) {
		this.publish_organ = publish_organ;
	}
	public String getPublish_organ_name() {
		return publish_organ_name;
	}
	public void setPublish_organ_name(String publish_organ_name) {
		this.publish_organ_name = publish_organ_name;
	}
	public String getPublish_user() {
		return publish_user;
	}
	public void setPublish_user(String publish_user) {
		this.publish_user = publish_user;
	}
	public String getFile_url() {
		return file_url;
	}
	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
	public String getRead_state() {
		return read_state;
	}
	public void setRead_state(String read_state) {
		this.read_state = read_state;
	}
	public String getRead_time() {
		return read_time;
	}
	public void setRead_time(String read_time) {
		this.read_time = read_time;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String getOrgans() {
		return organs;
	}
	public void setOrgans(String organs) {
		this.organs = organs;
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
