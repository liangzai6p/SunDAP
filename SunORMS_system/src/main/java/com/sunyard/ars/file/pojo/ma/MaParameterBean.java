package com.sunyard.ars.file.pojo.ma;


/**
 * @author:		
 * @date:		 
 * @description: TODO(MaParameterBean)
 */
public class MaParameterBean{

	/** 图像序号  */
	private String serialNo;
	/** 批次号  */
    private String batchId;
    /** 日期  */
    private String occurDate;
    /** 柜员  */
    private String operatorNo;
    /** 机构号  */
    private String siteNo;
    /** 批内码  */
    private Integer inccodeinBatch;
    /** 主附件标识  */
    private String psLevel;
    /** 所属主件批内码  */
    private Integer primaryInccodein;
    /** 版面名称  */
    private String formName;
    /** 勾兑标志  */
    private String checkFlag;
    /** 差错标志  */
    private String errorFlag;
    /** 删除标志  */
    private String selfDelete;
    /** 图像状态  */
    private String processState;
    /** 流水号  */
    private String flowId;
    /** 录入人员  */
    private String inputWorker;
    /**添加的补扫内容*/
    private String patchDes;
    /**工作流项ID*/
    private Long workId;
    /**工作流实例ID*/
    private Long procinstId;
    
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getOccurDate() {
		return occurDate;
	}
	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}
	public String getOperatorNo() {
		return operatorNo;
	}
	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}
	public String getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}
	public Integer getInccodeinBatch() {
		return inccodeinBatch;
	}
	public void setInccodeinBatch(Integer inccodeinBatch) {
		this.inccodeinBatch = inccodeinBatch;
	}
	public String getPsLevel() {
		return psLevel;
	}
	public void setPsLevel(String psLevel) {
		this.psLevel = psLevel;
	}
	public Integer getPrimaryInccodein() {
		return primaryInccodein;
	}
	public void setPrimaryInccodein(Integer primaryInccodein) {
		this.primaryInccodein = primaryInccodein;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	public String getSelfDelete() {
		return selfDelete;
	}
	public void setSelfDelete(String selfDelete) {
		this.selfDelete = selfDelete;
	}
	public String getProcessState() {
		return processState;
	}
	public void setProcessState(String processState) {
		this.processState = processState;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getInputWorker() {
		return inputWorker;
	}
	public void setInputWorker(String inputWorker) {
		this.inputWorker = inputWorker;
	}
	public String getPatchDes() {
		return patchDes;
	}
	public void setPatchDes(String patchDes) {
		this.patchDes = patchDes;
	}
	public Long getWorkId() {
		return workId;
	}
	public void setWorkId(Long workId) {
		this.workId = workId;
	}
	public Long getProcinstId() {
		return procinstId;
	}
	public void setProcinstId(Long procinstId) {
		this.procinstId = procinstId;
	}
}
