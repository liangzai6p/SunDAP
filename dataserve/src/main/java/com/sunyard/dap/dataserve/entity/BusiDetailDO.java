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
 * @since 2020-08-05
 */
@TableName("DM_BUSI_DETAIL_TB")
@Data
public class BusiDetailDO extends Model<BusiDetailDO> {

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

    /**
     * 完成时间
     */
    @TableField("COMPLETE_TIME")
    private Date completeTime;

    /**
     * 交易状态
     */
    @TableField("TRANS_STATE")
    private String transState;



    @Override
    protected Serializable pkVal() {
        return null;
    }


}
