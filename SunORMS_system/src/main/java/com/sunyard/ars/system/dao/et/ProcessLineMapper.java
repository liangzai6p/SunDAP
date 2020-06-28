package com.sunyard.ars.system.dao.et;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.bean.et.ProcessLineBean;

public interface ProcessLineMapper {
    
	/**
	 * 根据条件获取差错流程线信息
	 * @param map
	 * @return
	 */
	public List<HashMap<String,Object>> selectLineByKey(HashMap<String,Object> map);
	
	/**
     * 根据条件获取差错流程线信息
     * @param record
     * @return
     */
	public List<ProcessLineBean> selectBySelective(HashMap<String,Object> map);
}