package com.sunyard.ars.system.dao.et;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.bean.et.ProcessChartBean;

public interface ProcessChartMapper {
    
	/**
	 * 根据条件获取单据信息
	 * @param map
	 * @return
	 */
	public List<HashMap<String,Object>> selectChartByKey(HashMap<String,Object> map);
	
	/**
     * 根据对象中非空属性值作为查询条件查询
     * @param record
     * @return
     */
	public List<ProcessChartBean> selectBySelective(HashMap<String,Object> map);
}