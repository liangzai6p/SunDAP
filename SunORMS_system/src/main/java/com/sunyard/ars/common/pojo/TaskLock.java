package com.sunyard.ars.common.pojo;

/**
 * 任务锁对象
 */
public class TaskLock {
    private String  dataId;  //数据ID
    private String  lockUserNo; // 锁定人
    private String  lockTime;  // 锁定时间
    private String  taskType;  //任务单类型
    private String  unLockTime;//解锁时间


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getLockUserNo() {
        return lockUserNo;
    }

    public void setLockUserNo(String lockUserNo) {
        this.lockUserNo = lockUserNo;
    }

    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getUnLockTime() {
        return unLockTime;
    }

    public void setUnLockTime(String unLockTime) {
        this.unLockTime = unLockTime;
    }
}
