package com.sunyard.ars.system.dao.othersys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.TableInfo;

public interface ModelAnalyzeMapper {
	
	/**
	 * 删除当天数据
	 * @param tableName
	 * @param modelId
	 * @param dataDate
	 * @return
	 */
	int deleteModelDataByModelInfo(@Param("tableName") String tableName, 
			@Param("modelId") String modelId, @Param("dataDate") String dataDate);
	
	/**
	 * 删除历史数据
	 * @param tableName
	 * @param modelId
	 * @param dateDay
	 * @return
	 */
	int deleteHisDataByModelInfo(@Param("tableName") String tableName, 
			@Param("modelId") String modelId, @Param("dateDay") String dateDay);
	
	/**
	 * 获取表字段
	 * @param tableId
	 * @return
	 */
	List<TableInfo> getTableInfoByTableId(Integer tableId);
	
	/**
	 * 插入历史表
	 * @return
	 */
	int insertHistTable(@Param("hisTableName")String hisTableName, 
			@Param("tableName")String tableName, @Param("modelId")String modelId);
	/**
	 * 插入历史表(带字段)
	 * @return
	 */
	int insertHistTableColums(@Param("hisTableName")String hisTableName, 
			@Param("tableName")String tableName, @Param("modelId")String modelId,
			@Param("modelColumns")String modelColumns);
	
	/**
	 * 删除临时表已处理数据
	 * @return
	 */
	int deleteTempTable(@Param("tableName")String tableName, @Param("modelId")String modelId);
	
	/**
	 * 获取最大行号
	 * @param tableName
	 * @param hisTableName
	 * @return
	 */
	int getMaxRowNum(@Param("tableName")String tableName,@Param("hisTableName")String hisTableName);
	
	/**
	 * 根据视图编号获取视图列信息
	 * @param viewId 视图编号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getViewColsInfoByViewId(@Param("viewId")Integer viewId);
	
	/**
	 * 视图列条件实例化
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getViewConditionInfoByViewId(@Param("viewId")Integer viewId);
}
