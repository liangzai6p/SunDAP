package com.sunyard.ars.system.dao.et;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.bean.et.ProcessNodeBean;

public interface ProcessNodeMapper {
    
	/**
	 * 根据条件获取差错节点信息
	 * @param map
	 * @return
	 */
	public List<HashMap<String,Object>> selectNodeByKey(HashMap<String,Object> map);
	
	/**
     * 根据条件获取差错节点信息
     * @param record
     * @return
     */
	public List<ProcessNodeBean> selectBySelective(HashMap<String,Object> map);

	/**
	 * 获取结束节点集合
	 * @return
	 */
	public List<ProcessNodeBean> selectEndNodeList();
	
	/**
	 * 查询结束所有结束节点
	 * @return
	 */
	public List<HashMap<String,Object>> selectFlowEndNodes();
}