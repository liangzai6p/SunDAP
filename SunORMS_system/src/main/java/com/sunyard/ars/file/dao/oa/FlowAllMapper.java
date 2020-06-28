package com.sunyard.ars.file.dao.oa;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;



import com.sunyard.ars.file.pojo.oa.FlowAll;

public interface FlowAllMapper {
    
	 FlowAll selectByPrimaryKey(BigDecimal seqId);
	 List<HashMap<String, Object>> select(Map<String, Object> map);
//	int executeSql(@Param("sql")String sql);
}