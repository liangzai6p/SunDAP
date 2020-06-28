package com.sunyard.ars.system.bean.et;

public class ProcessChartBean {
    
	/* @Fields chartNo : 流程图编号 */
	private Integer chartNo; 
	/*@Fields chartName : 流程图名称*/ 
	private String chartName; 
	/*@Fields chartDescription : 流程图描述*/ 
	private String chartDescription; 
	/*@Fields organArea : 所属行社*/ 
	private String organArea;
	/*@Fields roleNo :使用角色*/ 
	private String roleNo;
	/*@Fields otherEssentialFactor : 其他要素*/ 
	private String otherEssentialFactor;
	/*@Fields chartStart : TODO(用一句话描述这个变量表示什么)*/ 
	private String chartStart;
	/*@Fields chartStart : TODO(用一句话描述这个变量表示什么)*/ 
	private String slipLevel;
	public Integer getChartNo() {
		return chartNo;
	}
	public void setChartNo(Integer chartNo) {
		this.chartNo = chartNo;
	}
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public String getChartDescription() {
		return chartDescription;
	}
	public void setChartDescription(String chartDescription) {
		this.chartDescription = chartDescription;
	}
	public String getOrganArea() {
		return organArea;
	}
	public void setOrganArea(String organArea) {
		this.organArea = organArea;
	}
	public String getRoleNo() {
		return roleNo;
	}
	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}
	public String getOtherEssentialFactor() {
		return otherEssentialFactor;
	}
	public void setOtherEssentialFactor(String otherEssentialFactor) {
		this.otherEssentialFactor = otherEssentialFactor;
	}
	public String getChartStart() {
		return chartStart;
	}
	public void setChartStart(String chartStart) {
		this.chartStart = chartStart;
	}
	public String getSlipLevel() {
		return slipLevel;
	}
	public void setSlipLevel(String slipLevel) {
		this.slipLevel = slipLevel;
	}
	

}