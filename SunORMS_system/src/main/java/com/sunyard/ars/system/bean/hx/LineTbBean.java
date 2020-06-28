package com.sunyard.ars.system.bean.hx;

public class LineTbBean {
	private  	Integer		id;
	private  	String		boforeNodeNo;
	private 	String		afterNodeNo;		
	private 	String		nodeContent;
	private 	String		isDefaultSelect;
	private 	String		transferMethod;	
	private 	String		flowChart;
	private 	String		lineRoles;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBoforeNodeNo() {
		return boforeNodeNo;
	}
	public void setBoforeNodeNo(String boforeNodeNo) {
		this.boforeNodeNo = boforeNodeNo;
	}
	public String getAfterNodeNo() {
		return afterNodeNo;
	}
	public void setAfterNodeNo(String afterNodeNo) {
		this.afterNodeNo = afterNodeNo;
	}
	public String getNodeContent() {
		return nodeContent;
	}
	public void setNodeContent(String nodeContent) {
		this.nodeContent = nodeContent;
	}
	public String getIsDefaultSelect() {
		return isDefaultSelect;
	}
	public void setIsDefaultSelect(String isDefaultSelect) {
		this.isDefaultSelect = isDefaultSelect;
	}
	public String getTransferMethod() {
		return transferMethod;
	}
	public void setTransferMethod(String transferMethod) {
		this.transferMethod = transferMethod;
	}
	public String getFlowChart() {
		return flowChart;
	}
	public void setFlowChart(String flowChart) {
		this.flowChart = flowChart;
	}
	public String getLineRoles() {
		return lineRoles;
	}
	public void setLineRoles(String lineRoles) {
		this.lineRoles = lineRoles;
	}
}
