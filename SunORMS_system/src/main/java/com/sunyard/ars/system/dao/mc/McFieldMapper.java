package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.McField;

public interface McFieldMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(McField record);

    int insertSelective(McField record);

    McField selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(McField record);

    int updateByPrimaryKey(McField record);
    
    /** 
	 * @Title: getMcFieldByTableId 
	 * @Description: 根据表ID获取表字段
	 * @param tableId
	 * @return List<McField>
	 */
	public List<McField> getMcFieldByTableId(Integer tableId);
	
	/**
	 * @Title: getMcMcFieldTbByName 
	 * @Description:  根据字段名查询
	 * @param name,tableType
	 * @throws Exception
	 * @return McField
	 */
	public McField getMcFieldByName(@Param("name")String name,@Param("tableType")String tableType);
	
	/**
	 * @param name
	 * @return 根据表类型获取
	 * @throws Exception
	 */
	public List<McField> getMcFieldByTableType(@Param("tabletype")String tabletype);
	
	/**
	 * 获取指定表的英文和中文字段*/
	public List<Object[]> getMcFieldsByTableId(int tableId);
	
	/**
	 * @根据表Id获取指定模型表的必要字段
	 * @param tableId
	 * @return 
	 * @throws Exception
	 */
	public List<McField> findSystemMcFieldByTableId(@Param("tableId")Integer tableId);
	
	/**
	 * 获取指定表的英文和中文字段*/
	public String getCheckMcFields(String serialNo);
	
	/**
	 * 获取指定表的英文和中文字段*/
	public List<Object[]> getFlowFieldsInfo(String flowFields);
	
	/**
	 * 按条件查询
	 * @param field
	 * @return
	 */
	public List<McField> selectBySelective(McField field);
	
	/**
	 * 获取表系统字段
	 * @param tableId
	 * @return
	 */
	public List<HashMap<String,Object>> selectSysField(Integer tableId);
	
	/**
	 * 查询存在该字段的表
	 * @param fieldId
	 * @return
	 */
	public List<String> selectTableNames(@Param("fieldId") Integer fieldId);
	
	/**
	 * 查询字段在表中是否存在
	 * @return
	 */
	public int selectTableCol(@Param("dbType")String dbType,@Param("tableName")String tableName,@Param("column")String column);

	/**
	 * 所有模型必须配置字段
	 * @return
	 */
	List<McField> getArmsMustFiedl();
}