package com.sunyard.ars.system.bean.sc;

import java.io.Serializable;

public class SmDayoffTb implements Serializable {
    private String offDate;

    private String dateName;

    private String dateMsg;

    private String isOpen;

    private String isLock;

    private String lastModiDate;

    private String bankNo;

    private String systemNo;

    private String projectNo;

    private static final long serialVersionUID = 1L;

    public String getOffDate() {
        return offDate;
    }

    public void setOffDate(String offDate) {
        this.offDate = offDate;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getDateMsg() {
        return dateMsg;
    }

    public void setDateMsg(String dateMsg) {
        this.dateMsg = dateMsg;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getLastModiDate() {
        return lastModiDate;
    }

    public void setLastModiDate(String lastModiDate) {
        this.lastModiDate = lastModiDate;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", offDate=").append(offDate);
        sb.append(", dateName=").append(dateName);
        sb.append(", dateMsg=").append(dateMsg);
        sb.append(", isOpen=").append(isOpen);
        sb.append(", isLock=").append(isLock);
        sb.append(", lastModiDate=").append(lastModiDate);
        sb.append(", bankNo=").append(bankNo);
        sb.append(", systemNo=").append(systemNo);
        sb.append(", projectNo=").append(projectNo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}