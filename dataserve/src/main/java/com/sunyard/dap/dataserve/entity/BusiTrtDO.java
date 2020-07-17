package com.sunyard.dap.dataserve.entity;

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
 * @since 2020-07-17
 */
@TableName("DM_BUSI_TRT_TB")
public class BusiTrtDO extends Model<BusiTrtDO> {

    private static final long serialVersionUID=1L;

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
     * 渠道编号
     */
    @TableField("CHANNEL_NO")
    private String channelNo;

    /**
     * 渠道名称
     */
    @TableField("CHANNEL_NAME")
    private String channelName;

    /**
     * 业务编号
     */
    @TableField("BUSI_NO")
    private String busiNo;

    /**
     * 业务名称
     */
    @TableField("BUSI_NAME")
    private String busiName;

    /**
     * 业务量
     */
    @TableField("BUSI_COUNT")
    private String busiCount;

    /**
     * 客户量
     */
    @TableField("CUS_COUNT")
    private String cusCount;

    /**
     * 更新时间
     */
    @TableField("RECORD_TIME")
    private Date recordTime;


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

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getBusiNo() {
        return busiNo;
    }

    public void setBusiNo(String busiNo) {
        this.busiNo = busiNo;
    }

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public String getBusiCount() {
        return busiCount;
    }

    public void setBusiCount(String busiCount) {
        this.busiCount = busiCount;
    }

    public String getCusCount() {
        return cusCount;
    }

    public void setCusCount(String cusCount) {
        this.cusCount = cusCount;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DmBusiTrtTb{" +
        "zoneNo=" + zoneNo +
        ", zoneName=" + zoneName +
        ", branchNo=" + branchNo +
        ", branchName=" + branchName +
        ", siteNo=" + siteNo +
        ", siteName=" + siteName +
        ", channelNo=" + channelNo +
        ", channelName=" + channelName +
        ", busiNo=" + busiNo +
        ", busiName=" + busiName +
        ", busiCount=" + busiCount +
        ", cusCount=" + cusCount +
        ", recordTime=" + recordTime +
        "}";
    }
}
