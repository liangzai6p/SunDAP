package com.sunyard.dap.intilligentSchedual.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

public class CenterCounterMonitorDO extends Model<CenterCounterMonitorDO> {
/** 序列号 */

    private static final long serialVersionUID = 7119503345936946218L;

/** 属性1 */

    private String attr1;
/** 属性2 */

    private String attr2;

/** 启用标志 */

    private String is_open;
/** 锁定标识 */

    private String is_lock;
/** 最后修改日期 */

    private String last_modi_date;
/** 银行号 */

    private String bank_no;
/** 系统号*/

    private String system_no;
/** 项目号*/

    private String project_no;


    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getIs_lock() {
        return is_lock;
    }

    public void setIs_lock(String is_lock) {
        this.is_lock = is_lock;
    }

    public String getLast_modi_date() {
        return last_modi_date;
    }

    public void setLast_modi_date(String last_modi_date) {
        this.last_modi_date = last_modi_date;
    }

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }

    public String getSystem_no() {
        return system_no;
    }

    public void setSystem_no(String system_no) {
        this.system_no = system_no;
    }

    public String getProject_no() {
        return project_no;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
    }
}
