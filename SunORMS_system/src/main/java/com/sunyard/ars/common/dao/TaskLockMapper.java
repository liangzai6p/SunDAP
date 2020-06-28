package com.sunyard.ars.common.dao;

import com.sunyard.ars.common.pojo.TaskLock;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface TaskLockMapper {

    //查询锁
   TaskLock selectLock(String dataId);

   Map<String,Object> selectLockFormId(@Param("dataId") String dataId);

   //删除锁
    int taskDelLock(String dataId);

    //新增锁
    int taskAddLock(TaskLock taskLock);

    int taskDelLockToUser(@Param("userNo") String userNo,@Param("taskType")String taskType);
}
