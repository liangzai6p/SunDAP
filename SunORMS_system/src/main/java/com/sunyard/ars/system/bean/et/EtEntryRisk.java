package com.sunyard.ars.system.bean.et;

import java.math.BigDecimal;

public class EtEntryRisk {
    
    private BigDecimal id;

    
    private BigDecimal entryid;

    
    private BigDecimal sourceNo;

    
    private String optuser;

    
    private String createtime;

    
    public BigDecimal getId() {
        return id;
    }

    
    public void setId(BigDecimal id) {
        this.id = id;
    }

    
    public BigDecimal getEntryid() {
        return entryid;
    }

    
    public void setEntryid(BigDecimal entryid) {
        this.entryid = entryid;
    }

    
    public BigDecimal getSourceNo() {
        return sourceNo;
    }

    
    public void setSourceNo(BigDecimal sourceNo) {
        this.sourceNo = sourceNo;
    }

    
    public String getOptuser() {
        return optuser;
    }

    
    public void setOptuser(String optuser) {
        this.optuser = optuser == null ? null : optuser.trim();
    }

    
    public String getCreatetime() {
        return createtime;
    }

    
    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }
}