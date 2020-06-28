package com.sunyard.ars.system.bean.busm;

public class ArmsUserTaltTbBean {
	private		String	bankcode;		//机构
	private		String	status;			//状态0有效1已收回99无效
	private		String	revTime;		//收回时间
	private		String	revDate;		//收回日期
	private		String	expDate;		//到期日
	private		String	effectDate;		//生效日期
	private		String	transtime;		//交易时间
	private		String	transdate;		//交易日期
	private		String	prmRoleNo;		//被转授权用户原角色
	private		String	roleNo;			//转授权角色
	private		String	targetUserNo;	//被转授权用户
	private		String	userNo;			//转授权用户
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRevTime() {
		return revTime;
	}
	public void setRevTime(String revTime) {
		this.revTime = revTime;
	}
	public String getRevDate() {
		return revDate;
	}
	public void setRevDate(String revDate) {
		this.revDate = revDate;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}
	public String getTranstime() {
		return transtime;
	}
	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}
	public String getTransdate() {
		return transdate;
	}
	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}
	public String getPrmRoleNo() {
		return prmRoleNo;
	}
	public void setPrmRoleNo(String prmRoleNo) {
		this.prmRoleNo = prmRoleNo;
	}
	public String getRoleNo() {
		return roleNo;
	}
	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}
	public String getTargetUserNo() {
		return targetUserNo;
	}
	public void setTargetUserNo(String targetUserNo) {
		this.targetUserNo = targetUserNo;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
}
