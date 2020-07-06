package com.sunyard.dap.dataserve.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yey.he
 * @since 2020-07-03
 */
@TableName("DM_BUSI_RT_TB")
@Data
public class BusiRtDO extends Model<BusiRtDO> {

    private static final long serialVersionUID=1L;

    /**
     * 实体编号
     */
    @TableField("BLOC_NO")
    private String blocNo;

    /**
     * 实体名称
     */
    @TableField("BLOC_NAME")
    private String blocName;

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
     * 业务量
     */
    @TableField("BUSI_COUNT")
    private String busiCount;

    /**
     * 金额
     */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /**
     * 客户量
     */
    @TableField("CUS_COUNT")
    private String cusCount;

    /**
     * 日期
     */
    @TableField("RECORD_TIME")
    private Date recordTime;


    public String getBlocNo() {
        return blocNo;
    }

    public void setBlocNo(String blocNo) {
        this.blocNo = blocNo;
    }

    public String getBlocName() {
        return blocName;
    }

    public void setBlocName(String blocName) {
        this.blocName = blocName;
    }

    public String getZoneNo() {
        return zoneNo;
    }

    public void setZoneNo(String zoneNo) {
        this.zoneNo = zoneNo;
    }
}
