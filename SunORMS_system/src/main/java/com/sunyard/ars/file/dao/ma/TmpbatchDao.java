package com.sunyard.ars.file.dao.ma;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;



public interface TmpbatchDao {
    
	 
	 List<HashMap<String, Object>> select(Map<String, Object> map);

	 List<HashMap<String, Object>> selectLock(Map<String, Object> map);
}