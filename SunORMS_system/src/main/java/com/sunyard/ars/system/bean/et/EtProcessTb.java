package com.sunyard.ars.system.bean.et;


/**
 * EtProcessTb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtProcessTb{

	private String formId;      	//通知书编号
	private String nodeNo;			//当前流程节点号
	private String formType;		//通知书类型
	private String createTime;		//处理时间
	private String preNodeNo;		//暂不用
	private String dealOrganNo;		//处理构机号
	private String dealOrganName;	//处理机构名称
	private String dealUserNo;		//处理人号
	private String dealUserName;	//处理人姓名
	private String dealDate;		//处理日期	
	private String dealDescription;	//处理描述
	private String dealResultText;		//处理结果
	private String labelProcess;	//处理流程标签	
	private String processShowLevel;//处理信息查看级别
	
	/** add 增加2个处理时的数据 czd    */
	private String backDate;		//下一节点应反馈日期
	private String overFlag;		//是否逾期
	
	
	// 监测员或主管办理差错单时 新增一个条件
	private String riskType;
	
	// 监测员或主管办理预警单时 新增4个条件
	private String riskSourceNo;
	private String riskSourceType;
	private String riskSourceName;
	private String riskSourceRemark;
	private String nextDealDate;
	
	public String getFormId() {
		return (this.formId!=null?this.formId.trim():this.formId);
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getNodeNo() {
		return this.nodeNo;
	}

	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}

	public String getPreNodeNo() {
		return this.preNodeNo;
	}

	public void setPreNodeNo(String preNodeNo) {
		this.preNodeNo = preNodeNo;
	}

	public String getDealOrganNo() {
		return this.dealOrganNo;
	}

	public void setDealOrganNo(String dealOrganNo) {
		this.dealOrganNo = dealOrganNo;
	}

	public String getDealOrganName() {
		return this.dealOrganName;
	}

	public void setDealOrganName(String dealOrganName) {
		this.dealOrganName = dealOrganName;
	}

	public String getDealUserNo() {
		return this.dealUserNo;
	}

	public void setDealUserNo(String dealUserNo) {
		this.dealUserNo = dealUserNo;
	}

	public String getDealUserName() {
		return this.dealUserName;
	}

	public void setDealUserName(String dealUserName) {
		this.dealUserName = dealUserName;
	}

	public String getDealDate() {
		return this.dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getDealDescription() {
		return (this.dealDescription!=null?this.dealDescription.trim():this.dealDescription);
	}

	public void setDealDescription(String dealDescription) {
		this.dealDescription = dealDescription;
	}

	public String getDealResultText() {
		return dealResultText;
	}

	public void setDealResultText(String dealResultText) {
		this.dealResultText = dealResultText;
	}

	public String getLabelProcess() {
		return labelProcess;
	}

	public void setLabelProcess(String labelProcess) {
		this.labelProcess = labelProcess;
	}

	public String getProcessShowLevel() {
		return processShowLevel;
	}

	public void setProcessShowLevel(String processShowLevel) {
		this.processShowLevel = processShowLevel;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getRiskSourceNo() {
		return riskSourceNo;
	}

	public void setRiskSourceNo(String riskSourceNo) {
		this.riskSourceNo = riskSourceNo;
	}

	public String getRiskSourceType() {
		return riskSourceType;
	}

	public void setRiskSourceType(String riskSourceType) {
		this.riskSourceType = riskSourceType;
	}

	public String getRiskSourceName() {
		return riskSourceName;
	}

	public void setRiskSourceName(String riskSourceName) {
		this.riskSourceName = riskSourceName;
	}

	public String getRiskSourceRemark() {
		return riskSourceRemark;
	}

	public void setRiskSourceRemark(String riskSourceRemark) {
		this.riskSourceRemark = riskSourceRemark;
	}

	public String getBackDate() {
		return backDate;
	}

	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}

	public String getOverFlag() {
		return overFlag;
	}

	public void setOverFlag(String overFlag) {
		this.overFlag = overFlag;
	}
	
	public String getNextDealDate() {
		return nextDealDate;
	}

	public void setNextDealDate(String nextDealDate) {
		this.nextDealDate = nextDealDate;
	}
}