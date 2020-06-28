package com.sunyard.ars.common.pojo;

import java.math.BigDecimal;

public class TmpBatch{

    private String batchId;

    private String batchLock;

    private String batchTotalPage;

    private BigDecimal businessId;

    private String batchCommit;

    private String inputDate;

    private String tempDataTable;

    private String inputWorker;

    private String machineId;

    private String needProcess;

    private String occurDate;

    private String operatorNo;

    private String progressFlag;

    private String siteNo;

    private String inputTime;

    private String imageStatus;

    private String isInvalid;

    private String priorityLevel;

    private String worker;

    private String workTime;
    
    private Long procinstId;
    
    private String areaCode;
    
    
    private String ocrFactorFlag;
    
    private String contentId;
    
    



	public String getOcrFactorFlag() {
		return ocrFactorFlag;
	}

	public void setOcrFactorFlag(String ocrFactorFlag) {
		this.ocrFactorFlag = ocrFactorFlag;
	}

	public Long getProcinstId() {
		return procinstId;
	}

	public void setProcinstId(Long procinstId) {
		this.procinstId = procinstId;
	}

	public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public String getBatchLock() {
        return batchLock;
    }

    public void setBatchLock(String batchLock) {
        this.batchLock = batchLock == null ? null : batchLock.trim();
    }

    public String getBatchTotalPage() {
        return batchTotalPage;
    }

    public void setBatchTotalPage(String batchTotalPage) {
        this.batchTotalPage = batchTotalPage == null ? null : batchTotalPage.trim();
    }

    public BigDecimal getBusinessId() {
        return businessId;
    }

    public void setBusinessId(BigDecimal businessId) {
        this.businessId = businessId;
    }

    public String getBatchCommit() {
        return batchCommit;
    }

    public void setBatchCommit(String batchCommit) {
        this.batchCommit = batchCommit == null ? null : batchCommit.trim();
    }

    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate == null ? null : inputDate.trim();
    }

    public String getTempDataTable() {
        return tempDataTable;
    }

    public void setTempDataTable(String tempDataTable) {
        this.tempDataTable = tempDataTable == null ? null : tempDataTable.trim();
    }

    public String getInputWorker() {
        return inputWorker;
    }

    public void setInputWorker(String inputWorker) {
        this.inputWorker = inputWorker == null ? null : inputWorker.trim();
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId == null ? null : machineId.trim();
    }

    public String getNeedProcess() {
        return needProcess;
    }

    public void setNeedProcess(String needProcess) {
        this.needProcess = needProcess == null ? null : needProcess.trim();
    }

    public String getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(String occurDate) {
        this.occurDate = occurDate == null ? null : occurDate.trim();
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo == null ? null : operatorNo.trim();
    }

    public String getProgressFlag() {
        return progressFlag;
    }

    public void setProgressFlag(String progressFlag) {
        this.progressFlag = progressFlag == null ? null : progressFlag.trim();
    }

    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo == null ? null : siteNo.trim();
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime == null ? null : inputTime.trim();
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus == null ? null : imageStatus.trim();
    }

    public String getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(String isInvalid) {
        this.isInvalid = isInvalid == null ? null : isInvalid.trim();
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel == null ? null : priorityLevel.trim();
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker == null ? null : worker.trim();
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime == null ? null : workTime.trim();
    }

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}


    
}