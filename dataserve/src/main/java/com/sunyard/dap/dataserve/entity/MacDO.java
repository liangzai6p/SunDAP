package com.sunyard.dap.dataserve.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yey.he
 * @since 2020-08-12
 */
@TableName("DM_MAC_TB")
public class MacDO extends Model<MacDO> {

    private static final long serialVersionUID=1L;

    /**
     * 设备编号
     */
    @TableField("MAC_NO")
    private String macNo;

    /**
     * 设备型号
     */
    @TableField("MAC_MODEL")
    private String macModel;

    /**
     * 区域编号
     */
    @TableField("ZONE_NO")
    private String zoneNo;

    /**
     * 区域名称
     */
    @TableField("ZONE_NAME")
    private String zoneName;

    /**
     * 分行编号
     */
    @TableField("BRANCH_NO")
    private String branchNo;

    /**
     * 分行名称
     */
    @TableField("BRANCH_NAME")
    private String branchName;

    /**
     * 网点编号
     */
    @TableField("SITE_NO")
    private String siteNo;

    /**
     * 网点名称
     */
    @TableField("SITE_NAME")
    private String siteName;

    /**
     * 维护次数
     */
    @TableField("REPAIR_TIMES")
    private BigDecimal repairTimes;

    /**
     * 最近维护时间
     */
    @TableField("LAST_REPAIR_TIME")
    private Date lastRepairTime;

    /**
     * 状态（0-离线，1-在线，2-故障）
     */
    @TableField("STATUS")
    private String status;


    public String getMacNo() {
        return macNo;
    }

    public void setMacNo(String macNo) {
        this.macNo = macNo;
    }

    public String getMacModel() {
        return macModel;
    }

    public void setMacModel(String macModel) {
        this.macModel = macModel;
    }

    public String getZoneNo() {
        return zoneNo;
    }

    public void setZoneNo(String zoneNo) {
        this.zoneNo = zoneNo;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public BigDecimal getRepairTimes() {
        return repairTimes;
    }

    public void setRepairTimes(BigDecimal repairTimes) {
        this.repairTimes = repairTimes;
    }

    public Date getLastRepairTime() {
        return lastRepairTime;
    }

    public void setLastRepairTime(Date lastRepairTime) {
        this.lastRepairTime = lastRepairTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DmMacTb{" +
        "macNo=" + macNo +
        ", macModel=" + macModel +
        ", zoneNo=" + zoneNo +
        ", zoneName=" + zoneName +
        ", branchNo=" + branchNo +
        ", branchName=" + branchName +
        ", siteNo=" + siteNo +
        ", siteName=" + siteName +
        ", repairTimes=" + repairTimes +
        ", lastRepairTime=" + lastRepairTime +
        ", status=" + status +
        "}";
    }
}
