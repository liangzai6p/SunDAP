package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;

public interface RelateMapper {
	
	/**
	 * 查询表字段关联
	 * @param map
	 * @return
	 */
	int selectRelate(HashMap<String,Object> map );
	
    
}