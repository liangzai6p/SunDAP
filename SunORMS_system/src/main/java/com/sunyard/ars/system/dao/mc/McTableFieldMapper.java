package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.ars.system.pojo.mc.McTableField;


public interface McTableFieldMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(McTableField record);

    int insertSelective(McTableField record);

    McTableField selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(McTableField record);

    int updateByPrimaryKey(McTableField record);
    
    /**
     * 按条件查询表字段
     * @param record
     * @return
     */
    List<McTableField> selectBySelective(McTableField record);
    
    
    /**
     * 根据tableId查询表自定义的模型字段
     * @param tableId
     * @return
     */
	public List<HashMap<String,Object>> selectTableFieldByTableId(@Param("tableId")int tableId);
	
	/**
	 * @Title: getMcTableFieldFromSm
	 * @Description: 根据表ID获取表字段,从系统配置模块中获取
	 * @param tableId
	 * @throws Exception
	 * @return List<McMcTableFieldTb>
	 */
	public List<McTableField> getMcTableFieldFromSm(int tableId);
	
	
	/**
	 * @Title: 查询具体的表的字段
	 * @return: list字段结果集
	 */
	public int findTableCol(@Param("table")String table, @Param("column")String column);
	
	
	/**
	 * @根据表Id获取指定模型表的必要字段
	 * @param tableId
	 * @return 
	 * @throws Exception
	 */
	public List<McTableField> findSystemFieldByTableId(@Param("tableId")int tableId);
	
	/**
	 * @Title: getMcTableFieldDao
	 * @Description: 根据表ID获取表字段
	 * @param tableId
	 * @return List<McTableField>
	 */
	public List<McTableField> getTableFieldDao(@Param("tableId")Integer tableId);
	
	
}