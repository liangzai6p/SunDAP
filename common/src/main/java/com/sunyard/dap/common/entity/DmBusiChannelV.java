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
@TableName("DM_BUSI_CHANNEL_V")
@Data
public class DmBusiChannelV extends Model<DmBusiChannelV> {

    private static final long serialVersionUID=1L;

    @TableField("CHANNEL_NO")
    private String channelNo;

    @TableField("CHANNEL_NAME")
    private String channelName;

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
        return "DmBusiChannelV{" +
        "channelNo=" + channelNo +
        ", channelName=" + channelName +
        ", busiCount=" + busiCount +
        ", amount=" + amount +
        ", cusCount=" + cusCount +
        ", recordTime=" + DateUtil.format(recordTime,"yyyyMMdd") +
        "}";
    }
}
