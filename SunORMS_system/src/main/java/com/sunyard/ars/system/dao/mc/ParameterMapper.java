package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.common.pojo.PropertyFilter;
import com.sunyard.ars.system.pojo.mc.TableInfo;
import com.sunyard.ars.system.pojo.mc.VersionModel;


public interface ParameterMapper {
	/**
	 * 参数版本SQL：
	 * @param map
	 * @return
	 */
	public List<HashMap<String,Object>> selectParamVersion(@Param("tableName")String tableName,@Param("vStartDate")String vStartDate,
			@Param("vEndDate")String vEndDate, @Param("bankCode")String bankCode);
	
	
	/**
	 * 设置版本结束日期
	 * @param tableName
	 */
	public int setParameterEndTime(@Param("tableName")String tableName,@Param("date")String date, @Param("bankCode")String bankCode);
	
	
	/**
	 * 参数放入历史表 
	 * @param tableName 
	 */
	public int addParameterInHisTable(@Param("tableName")String tableName, @Param("bankCode")String bankCode);
	
	
	/**
	 * 删除参数表
	 * @param tableName
	 */
	public void deleteParameter(@Param("tableName")String tableName, @Param("bankCode")String bankCode);
	
	/**
	 * 历史版本转入参数表
	 * @param tableName
	 * @param versionNo
	 */
	public void insertParameter(@Param("tableName")String tableName,@Param("versionNo")String versionNo, @Param("bankCode")String bankCode);
	
	/**
	 * 删除历史版本参数表
	 * @param tableName
	 * @param versionNo
	 */
	public void deleteHisParameter(@Param("tableName")String tableName,@Param("versionNo")String versionNo, @Param("bankCode")String bankCode);
	
	
	/**
	 * @param condMap 
	 * @Title: selectParamData
	 * @Description: 参数数据获取
	 */
	public List<HashMap<String,Object>> selectParamData(HashMap<String,Object> condMap);
	/*public List<HashMap<String,Object>> selectParamData(@Param("tableName")String tableName,@Param("tableCols")List<HashMap<String,Object>> tableCols,
							@Param("filterList")List<PropertyFilter> filterList, @Param("startDate")String startDate,@Param("endDate")String endDate);
	*/
	
	/**
	 * @Title: parameterDataCountDao
	 * @Description: 获取展现数据数量
	 */
	public int parameterDataCountDao(@Param("tableName")String tableName, @Param("condition")String condition,@Param("privOrgans")String... privOrgans);
	
	
	/**
	 * @Title: addParamData
	 * @Description: 参数添加
	 */
	//public void addParamData(@Param("tableName")String tableName,@Param("tableCols")List<HashMap<String,Object>> tableCols,@Param("fieldValues")List fieldValues);
	public void addParamData(@Param("addSql")String addSql);
	/**
	 * @Title: modifyParamData
	 * @Description: 参数修改
	 */
	public void modifyParamData(@Param("modifySql")String modifySql);
	
	/**
	 * @Title: parameterStopDao
	 * @Description: 参数停用
	 * @param tableInfos
	 * @param values
	 * @param user
	 * @return void
	 */
	public void parameterStopDao(List<TableInfo> tableInfos,List<String> values);
	
	/**
	 * 参数删除
	 * @param tableName
	 * @param rowId
	 */
	public void deleteParamData(@Param("tableName")String tableName,@Param("rowId")String rowId);


	public List<HashMap<String, Object>> selectParamTableType(@Param("userNo")String userNo);
}