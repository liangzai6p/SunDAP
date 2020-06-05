package com.sunyard.dap.common.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yey.he
 * @since 2020-06-03
 */
@TableName("TEST")
public class Test extends Model<Test> {

    private static final long serialVersionUID=1L;

    @TableId("TID")
    private BigDecimal tid;

    @TableField("TNAME")
    private String tname;

    @TableField("TDESC")
    private String tdesc;


    public BigDecimal getTid() {
        return tid;
    }

    public void setTid(BigDecimal tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTdesc() {
        return tdesc;
    }

    public void setTdesc(String tdesc) {
        this.tdesc = tdesc;
    }

    @Override
    protected Serializable pkVal() {
        return this.tid;
    }

    @Override
    public String toString() {
        return "Test{" +
        "tid=" + tid +
        ", tname=" + tname +
        ", tdesc=" + tdesc +
        "}";
    }
}
