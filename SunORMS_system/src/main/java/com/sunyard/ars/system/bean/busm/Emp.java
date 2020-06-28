package com.sunyard.ars.system.bean.busm;

import java.math.BigDecimal;

public class Emp {
	 private BigDecimal empId;
	 private String organNo;
	 private String userNo;
	 private String empPwd;
	 private String userRoll;
	 private String empLev;
	 private String empStatus;
	 private String userStatus;
	 private String targetUserNo;
	 private String startTime;
	 private String endTime;
	 private String dataFlag;
	 public String getDataFlag() {
		return dataFlag;
	}
	public void setDataFlag(String dataFlag) {
		this.dataFlag = dataFlag;
	}
	public String getOrganNo() {
		return organNo;
	}
	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}
	
	 public BigDecimal getEmpId() {
		return empId;
	}
	public void setEmpId(BigDecimal empId) {
		this.empId = empId;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getEmpPwd() {
		return empPwd;
	}
	public void setEmpPwd(String empPwd) {
		this.empPwd = empPwd;
	}
	public String getUserRoll() {
		return userRoll;
	}
	public void setUserRoll(String userRoll) {
		this.userRoll = userRoll;
	}

	public String getEmpLev() {
		return empLev;
	}
	public void setEmpLev(String empLev) {
		this.empLev = empLev;
	}
	public String getEmpStatus() {
		return empStatus;
	}
	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getTargetUserNo() {
		return targetUserNo;
	}
	public void setTargetUserNo(String targetUserNo) {
		this.targetUserNo = targetUserNo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	 
}
