package com.sunyard.ars.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CommonMapper {
	
    /**
     * 执行指定sql查询语句
     * @param sql
     * @return
     */
    List<Map<String, Object>> executeSelect(@Param(value="sql") String sql);

    /**
     * 执行指定更新语句（增、删、改）
     * @param sql
     */
    int executeUpdate(@Param(value="sql") String sql);
    
}