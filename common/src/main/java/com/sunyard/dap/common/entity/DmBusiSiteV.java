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
@TableName("DM_BUSI_SITE_V")
@Data
public class DmBusiSiteV extends Model<DmBusiSiteV> {

    private static final long serialVersionUID=1L;

    @TableField("SITE_NO")
    private String siteNo;

    @TableField("SITE_NAME")
    private String siteName;

    @TableField("BUSI_COUNT")
    private BigDecimal busiCount;

    @TableField("AMOUNT")
    private BigDecimal amount;

    @TableField("CUS_COUNT")
    private BigDecimal cusCount;

    @TableField("RECORD_TIME")
    @JsonFormat(pattern = "yyyyMMdd")
    private Date recordTime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DmBusiSiteV{" +
        "siteNo=" + siteNo +
        ", siteName=" + siteName +
        ", busiCount=" + busiCount +
        ", amount=" + amount +
        ", cusCount=" + cusCount +
        ", recordTime=" + DateUtil.format(recordTime,"yyyyMMdd") +
        "}";
    }
}
