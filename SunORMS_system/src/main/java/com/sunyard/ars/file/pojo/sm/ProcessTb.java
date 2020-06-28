package com.sunyard.ars.file.pojo.sm;

public class ProcessTb {

	private String obj_no = "";//网点号/柜员号
	private String obj_name = "";//网点名称/柜员名称
	private int flowCount = 0;	//流水总数
	private int flowNoCheckCount = 0;	//流水未勾对数
	private int flowCheckCount = 0;	 //已勾对流水数
	private int scanCount = 0;	//已扫描数
	private int batchTwoCount = 0;	//待补录数
	private int batchOverTwoCount = 0;	//已补录数
	private int batchOverThreeCount = 0;	//已审核数
	private int forceManCount = 0;	//强制补录总数
	private int forceNoManCount = 0;	//强制补录剩余数
	

	
	
	public String getObj_no() {
		return obj_no;
	}
	public void setObj_no(String obj_no) {
		this.obj_no = obj_no;
	}
	public String getObj_name() {
		return obj_name;
	}
	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
	}
	public int getFlowCount() {
		return flowCount;
	}
	public void setFlowCount(int flowCount) {
		this.flowCount = flowCount;
	}
	public int getFlowNoCheckCount() {
		return flowNoCheckCount;
	}
	public void setFlowNoCheckCount(int flowNoCheckCount) {
		this.flowNoCheckCount = flowNoCheckCount;
	}
	public int getFlowCheckCount() {
		return flowCheckCount;
	}
	public void setFlowCheckCount(int flowCheckCount) {
		this.flowCheckCount = flowCheckCount;
	}
	public int getScanCount() {
		return scanCount;
	}
	public void setScanCount(int scanCount) {
		this.scanCount = scanCount;
	}
	public int getBatchTwoCount() {
		return batchTwoCount;
	}
	public void setBatchTwoCount(int batchTwoCount) {
		this.batchTwoCount = batchTwoCount;
	}
	public int getBatchOverTwoCount() {
		return batchOverTwoCount;
	}
	public void setBatchOverTwoCount(int batchOverTwoCount) {
		this.batchOverTwoCount = batchOverTwoCount;
	}
	public int getBatchOverThreeCount() {
		return batchOverThreeCount;
	}
	public void setBatchOverThreeCount(int batchOverThreeCount) {
		this.batchOverThreeCount = batchOverThreeCount;
	}
	public int getForceManCount() {
		return forceManCount;
	}
	public void setForceManCount(int forceManCount) {
		this.forceManCount = forceManCount;
	}
	public int getForceNoManCount() {
		return forceNoManCount;
	}
	public void setForceNoManCount(int forceNoManCount) {
		this.forceNoManCount = forceNoManCount;
	}
	
	
	
}
