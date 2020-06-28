package com.sunyard.ars.system.dao.monitor;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * ...	数据库操作接口
 * 
 * @author:	
 * @date:	2017年12月7日 下午5:11:45
 */
public interface MonitorModuleDao {
	/**
	 * 查询存储过程
	 */
	public void callProcedure(HashMap<String, Object> map);
	
	/**
	 * 查询函数
	 */
	public void callFunction(HashMap<String, Object> map);
	/**
	 * 查询 ... 信息
	 */
	public List<HashMap<String, Object>> select(HashMap<String, Object> map);
	/**
	 * 查询1
	 */
	public List<HashMap<String, Object>> select1(HashMap<String, Object> map);
	/**
	 * 查询2
	 */
	public List<HashMap<String, Object>> select2(HashMap<String, Object> map);
	/**
	 * 查询3
	 */
	public List<HashMap<String, Object>> select3(HashMap<String, Object> map);
	/**
	 * 查询4
	 */
	public List<HashMap<String, Object>> select4(HashMap<String, Object> map);
	/**
	 * 预警信息查询
	 */
	public List<HashMap<String, Object>> selectWarn(HashMap<String, Object> map);
	
	public List<HashMap<String, Object>> getValueBySearchBarSql(HashMap<String, Object> map);

	
}
