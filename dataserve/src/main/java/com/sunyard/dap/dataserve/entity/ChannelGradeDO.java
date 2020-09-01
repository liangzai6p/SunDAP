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
 * @since 2020-08-28
 */
@TableName("DM_CHANNEL_GRADE_TB")
public class ChannelGradeDO extends Model<ChannelGradeDO> {

    private static final long serialVersionUID=1L;

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
     * 交易评价
     */
    @TableField("BUSI_SCORE")
    private String busiScore;

    /**
     * 效率评价
     */
    @TableField("QUALITY_SCORE")
    private String qualityScore;

    /**
     * 客户满意度
     */
    @TableField("SATIS_SCORE")
    private String satisScore;

    /**
     * 成功率
     */
    @TableField("SUCCESS_RATE")
    private String successRate;

    /**
     * 市场覆盖率
     */
    @TableField("COVER_RATE")
    private String coverRate;


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

    public String getBusiScore() {
        return busiScore;
    }

    public void setBusiScore(String busiScore) {
        this.busiScore = busiScore;
    }

    public String getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(String qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getSatisScore() {
        return satisScore;
    }

    public void setSatisScore(String satisScore) {
        this.satisScore = satisScore;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getCoverRate() {
        return coverRate;
    }

    public void setCoverRate(String coverRate) {
        this.coverRate = coverRate;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DmChannelGradeTb{" +
        "branchNo=" + branchNo +
        ", branchName=" + branchName +
        ", channelNo=" + channelNo +
        ", channelName=" + channelName +
        ", busiScore=" + busiScore +
        ", qualityScore=" + qualityScore +
        ", satisScore=" + satisScore +
        ", successRate=" + successRate +
        ", coverRate=" + coverRate +
        "}";
    }
}
