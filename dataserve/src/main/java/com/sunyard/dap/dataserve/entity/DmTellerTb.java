package com.sunyard.dap.dataserve.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yey.he
 * @since 2020-08-14
 */
@TableName("DM_TELLER_TB")
public class DmTellerTb extends Model<DmTellerTb> {

    private static final long serialVersionUID=1L;

    /**
     * 柜员工号
     */
    @TableField("TELLER_NO")
    private String tellerNo;

    /**
     * 柜员姓名
     */
    @TableField("TELLER_NAME")
    private String tellerName;

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
     * 星级
     */
    @TableField("STAR_LEVEL")
    private String starLevel;

    /**
     * 学历
     */
    @TableField("EDU_BACKGROUND")
    private String eduBackground;

    /**
     * 工龄
     */
    @TableField("SENIORITY")
    private String seniority;

    /**
     * 培训经验
     */
    @TableField("TRAIN_EXP")
    private String trainExp;

    /**
     * 任职履历
     */
    @TableField("OFFICE_RESUME")
    private String officeResume;

    /**
     * 岗位证书
     */
    @TableField("JOB_CERT")
    private String jobCert;

    /**
     * 状态（0-离线，1-在线）
     */
    @TableField("STATUS")
    private String status;


    public String getTellerNo() {
        return tellerNo;
    }

    public void setTellerNo(String tellerNo) {
        this.tellerNo = tellerNo;
    }

    public String getTellerName() {
        return tellerName;
    }

    public void setTellerName(String tellerName) {
        this.tellerName = tellerName;
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

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public String getEduBackground() {
        return eduBackground;
    }

    public void setEduBackground(String eduBackground) {
        this.eduBackground = eduBackground;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public String getTrainExp() {
        return trainExp;
    }

    public void setTrainExp(String trainExp) {
        this.trainExp = trainExp;
    }

    public String getOfficeResume() {
        return officeResume;
    }

    public void setOfficeResume(String officeResume) {
        this.officeResume = officeResume;
    }

    public String getJobCert() {
        return jobCert;
    }

    public void setJobCert(String jobCert) {
        this.jobCert = jobCert;
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
        return "DmTellerTb{" +
        "tellerNo=" + tellerNo +
        ", tellerName=" + tellerName +
        ", roleNo=" + roleNo +
        ", roleName=" + roleName +
        ", siteNo=" + siteNo +
        ", siteName=" + siteName +
        ", starLevel=" + starLevel +
        ", eduBackground=" + eduBackground +
        ", seniority=" + seniority +
        ", trainExp=" + trainExp +
        ", officeResume=" + officeResume +
        ", jobCert=" + jobCert +
        ", status=" + status +
        "}";
    }
}
