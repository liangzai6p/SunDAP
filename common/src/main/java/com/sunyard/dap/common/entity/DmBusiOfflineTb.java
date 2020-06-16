package com.sunyard.dap.common.entity;

import java.math.BigDecimal;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yey.he
 * @since 2020-06-12
 */
@TableName("DM_BUSI_OFFLINE_TB")
@Data
public class DmBusiOfflineTb extends Model<DmBusiOfflineTb> {

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
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyyMMdd")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DmBusiOfflineTb{" +
        "blocNo=" + blocNo +
        ", blocName=" + blocName +
        ", zoneNo=" + zoneNo +
        ", zoneName=" + zoneName +
        ", branchNo=" + branchNo +
        ", branchName=" + branchName +
        ", siteNo=" + siteNo +
        ", siteName=" + siteName +
        ", channelNo=" + channelNo +
        ", channelName=" + channelName +
        ", busiCount=" + busiCount +
        ", amount=" + amount +
        ", cusCount=" + cusCount +
        ", createTime=" + DateUtil.format(createTime,"yyyyMMdd") +
        "}";
    }
}
