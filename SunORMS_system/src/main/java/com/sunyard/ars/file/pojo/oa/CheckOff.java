package com.sunyard.ars.file.pojo.oa;



/**
 * 轧账表
 * @date   2018年6月1日
 * @Description CheckOff.java
 */
public class CheckOff{
    
    private String occurDate;

    
    private String siteNo;

    
    private String operatorNo;

    
    private String cityNo;

    
    private String streamCheckoff;

    
    private String subjectCheckoff;

    
    private String feeCheckoff;

    
    private String allCheckoff;

    
    private String checkWorker;

    
    private String forceReason;

    
    private String forceFlag;

    
    private String checkDate;

    private String archiveDate;
    



	public String getArchiveDate() {
		return archiveDate;
	}


	public void setArchiveDate(String archiveDate) {
		this.archiveDate = archiveDate;
	}


	public String getOccurDate() {
        return occurDate;
    }

    
    public void setOccurDate(String occurDate) {
        this.occurDate = occurDate == null ? null : occurDate.trim();
    }

    
    public String getSiteNo() {
        return siteNo;
    }

    
    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo == null ? null : siteNo.trim();
    }

    
    public String getOperatorNo() {
        return operatorNo;
    }

    
    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo == null ? null : operatorNo.trim();
    }

    
    public String getCityNo() {
        return cityNo;
    }

    
    public void setCityNo(String cityNo) {
        this.cityNo = cityNo == null ? null : cityNo.trim();
    }

    
    public String getStreamCheckoff() {
        return streamCheckoff;
    }

    
    public void setStreamCheckoff(String streamCheckoff) {
        this.streamCheckoff = streamCheckoff == null ? null : streamCheckoff.trim();
    }

    
    public String getSubjectCheckoff() {
        return subjectCheckoff;
    }

    
    public void setSubjectCheckoff(String subjectCheckoff) {
        this.subjectCheckoff = subjectCheckoff == null ? null : subjectCheckoff.trim();
    }

    
    public String getFeeCheckoff() {
        return feeCheckoff;
    }

    
    public void setFeeCheckoff(String feeCheckoff) {
        this.feeCheckoff = feeCheckoff == null ? null : feeCheckoff.trim();
    }

    
    public String getAllCheckoff() {
        return allCheckoff;
    }

    
    public void setAllCheckoff(String allCheckoff) {
        this.allCheckoff = allCheckoff == null ? null : allCheckoff.trim();
    }

    
    public String getCheckWorker() {
        return checkWorker;
    }

    
    public void setCheckWorker(String checkWorker) {
        this.checkWorker = checkWorker == null ? null : checkWorker.trim();
    }

    
    public String getForceReason() {
        return forceReason;
    }

    
    public void setForceReason(String forceReason) {
        this.forceReason = forceReason == null ? null : forceReason.trim();
    }

    
    public String getForceFlag() {
        return forceFlag;
    }

    
    public void setForceFlag(String forceFlag) {
        this.forceFlag = forceFlag == null ? null : forceFlag.trim();
    }

    
    public String getCheckDate() {
        return checkDate;
    }

    
    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate == null ? null : checkDate.trim();
    }
}