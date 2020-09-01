package com.sunyard.dap.dataserve.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 系统银行信息表
 * </p>
 *
 * @author yey.he
 * @since 2020-08-26
 */
@TableName("SM_BANKS_TB")
public class SiteDO extends Model<SiteDO> {

    private static final long serialVersionUID=1L;

    @TableField("ZONE_NO")
    private String zoneNo;

    @TableField("ZONE_NAME")
    private String zoneName;

    @TableField("BRANCH_NO")
    private String branchNo;

    @TableField("BRANCH_NAME")
    private String branchName;

    @TableId("SITE_NO")
    private String siteNo;

    @TableField("SITE_NAME")
    private String siteName;

    /**
     * 行长
     */
    @TableField("PRESIDENT")
    private String president;

    /**
     * 副行长
     */
    @TableField("VICE_PRESIDENT")
    private String vicePresident;

    /**
     * 地址
     */
    @TableField("ADDR")
    private String addr;

    /**
     * 电话
     */
    @TableField("TEL")
    private String tel;


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

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getVicePresident() {
        return vicePresident;
    }

    public void setVicePresident(String vicePresident) {
        this.vicePresident = vicePresident;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    protected Serializable pkVal() {
        return this.siteNo;
    }

    @Override
    public String toString() {
        return "SmBanksTb{" +
        "zoneNo=" + zoneNo +
        ", zoneName=" + zoneName +
        ", branchNo=" + branchNo +
        ", branchName=" + branchName +
        ", siteNo=" + siteNo +
        ", siteName=" + siteName +
        ", president=" + president +
        ", vicePresident=" + vicePresident +
        ", addr=" + addr +
        ", tel=" + tel +
        "}";
    }
}
