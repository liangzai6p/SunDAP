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
 * @since 2020-08-20
 */
@TableName("DM_BUSI_DETAIL_ERROR_TB")
public class BusiErrorDetailDo extends Model<BusiErrorDetailDo> {

    private static final long serialVersionUID=1L;

    /**
     * 系统流水号
     */
    @TableField("TASK_ID")
    private String taskId;

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
     * 岗位编号
     */
    @TableField("ROLE_NO")
    private String roleNo;

    /**
     * 岗位名称
     */
    @TableField("ROLE_NAME")
    private String roleName;

    /**
     * 员工编号
     */
    @TableField("STAFF_NO")
    private String staffNo;

    /**
     * 员工姓名
     */
    @TableField("STAFF_NAME")
    private String staffName;

    /**
     * 客户类型（单位/个人/二三类账户）
     */
    @TableField("CUS_TYPE")
    private String cusType;

    /**
     * 客户编号
     */
    @TableField("CUS_NO")
    private String cusNo;

    /**
     * 客户名称
     */
    @TableField("CUS_NAME")
    private String cusName;

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
     * 业务小类编号
     */
    @TableField("STYPE_NO")
    private String stypeNo;

    /**
     * 业务小类名称
     */
    @TableField("STYPE_NAME")
    private String stypeName;

    /**
     * 自助设备编号
     */
    @TableField("MAC_NO")
    private String macNo;

    /**
     * 币种
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 交易金额
     */
    @TableField("TRANS_AMOUNT")
    private BigDecimal transAmount;

    /**
     * 创建日期
     */
    @TableField("CREATE_TIME")
    private Date createTime;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getRoleNo() {
        return roleNo;
    }

    public void setRoleNo(String roleNo) {
        this.roleNo = roleNo;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getCusNo() {
        return cusNo;
    }

    public void setCusNo(String cusNo) {
        this.cusNo = cusNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
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

    public String getStypeNo() {
        return stypeNo;
    }

    public void setStypeNo(String stypeNo) {
        this.stypeNo = stypeNo;
    }

    public String getStypeName() {
        return stypeName;
    }

    public void setStypeName(String stypeName) {
        this.stypeName = stypeName;
    }

    public String getMacNo() {
        return macNo;
    }

    public void setMacNo(String macNo) {
        this.macNo = macNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DmBusiDetailErrorTb{" +
        "taskId=" + taskId +
        ", zoneNo=" + zoneNo +
        ", zoneName=" + zoneName +
        ", branchNo=" + branchNo +
        ", branchName=" + branchName +
        ", siteNo=" + siteNo +
        ", siteName=" + siteName +
        ", channelNo=" + channelNo +
        ", channelName=" + channelName +
        ", roleNo=" + roleNo +
        ", roleName=" + roleName +
        ", staffNo=" + staffNo +
        ", staffName=" + staffName +
        ", cusType=" + cusType +
        ", cusNo=" + cusNo +
        ", cusName=" + cusName +
        ", busiNo=" + busiNo +
        ", busiName=" + busiName +
        ", stypeNo=" + stypeNo +
        ", stypeName=" + stypeName +
        ", macNo=" + macNo +
        ", currency=" + currency +
        ", transAmount=" + transAmount +
        ", createTime=" + createTime +
        "}";
    }
}
