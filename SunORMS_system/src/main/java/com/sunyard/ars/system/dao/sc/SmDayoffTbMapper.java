package com.sunyard.ars.system.dao.sc;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.bean.sc.SmDayoffTb;

public interface SmDayoffTbMapper {
    /**
	 * 新增用户信息信息
	 */
	public int insert(HashMap<String, Object> map);
	
	/**
	 * 导入数据
	 */
	public int insertOrUpdate(HashMap<String, Object> map);
	
	/**
	 * 删除节假日信息
	 */
	public int delete(HashMap<String, Object> map);
	
	/**
	 * 修改节假日信息
	 */
	public int update(HashMap<String, Object> map);
	
	/**
	 * 查询节假日信息
	 */
	public List<HashMap<String, Object>> select(HashMap<String, Object> map);
	
	/**
	 * 查询是否已经存在节假日
	 */
	public List<HashMap<String, Object>> selectDateName(HashMap<String, Object> map);
	
	/**
	 * 查询所有节假日
	 */
	public List<HashMap<String, Object>> smDayoffTbQuery();
}