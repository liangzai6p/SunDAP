package com.sunyard.ars.system.bean.et;

public class EtRiskSourceDef {
    
    private String sourceNo;

    
    private String sourceType;

    
    private String sourceName;

    
    private String isNeed;

    
    private String remark;

    
    private String modiUser;

    
    private String modiTime;

    
    private String modiRemark;

    
    private String state;

    
    public String getSourceNo() {
        return sourceNo;
    }

    
    public void setSourceNo(String sourceNo) {
        this.sourceNo = sourceNo == null ? null : sourceNo.trim();
    }

    
    public String getSourceType() {
        return sourceType;
    }

    
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.trim();
    }

    
    public String getSourceName() {
        return sourceName;
    }

    
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    
    public String getIsNeed() {
        return isNeed;
    }

    
    public void setIsNeed(String isNeed) {
        this.isNeed = isNeed == null ? null : isNeed.trim();
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    
    public String getModiUser() {
        return modiUser;
    }

    
    public void setModiUser(String modiUser) {
        this.modiUser = modiUser == null ? null : modiUser.trim();
    }

    
    public String getModiTime() {
        return modiTime;
    }

    
    public void setModiTime(String modiTime) {
        this.modiTime = modiTime == null ? null : modiTime.trim();
    }

    
    public String getModiRemark() {
        return modiRemark;
    }

    
    public void setModiRemark(String modiRemark) {
        this.modiRemark = modiRemark == null ? null : modiRemark.trim();
    }

    
    public String getState() {
        return state;
    }

    
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }
}