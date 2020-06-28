package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.McTable;

public interface McTableMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(McTable record);

    int insertSelective(McTable record);

    McTable selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(McTable record);

    int updateByPrimaryKey(McTable record);
    
    /**
     * 按条件查询表定义表
     * @param record
     * @return
     */
    List<McTable> selectBySelective(McTable record);
    
	/**
	 * 查询表定义表是否已经创建
	 * @param tableName
	 * @param dbType
	 * @return
	 */
	public int selectTable(@Param("tableName")String tableName,@Param("dbType")String dbType);
	
	
	/**
	 * @Title: selectIndexInfo
	 * @Description: 查询Oracle数据库中创建的索引信息
	 * @return List 
	 */
	public int selectIndexInfo(@Param("tableName")String tableName,@Param("dbType")String dbType);
	
	/**
	 * 查询表空间是否存在
	 * @param tableSpace
	 * @param dbType
	 * @return
	 */
	int selectTableSpaceInfo(@Param("tableSpace")String tableSpace,@Param("dbType")String dbType);
	
	/**
	 * 根据表id查询配置的表结构
	 * @param tableId
	 * @return
	 */
	List<HashMap<String,Object>> selectTableInfoById(@Param("tableId")Integer tableId);
	
	/**
	 * 根据表名查询表结构
	 * @param tableName
	 * @return
	 */
	List<HashMap<String,Object>> selectTableInfoByName(@Param("tableName")String tableName);
	
	/**
	 * 查询重要字段
	 * @param tableId
	 * @return
	 */
	List<HashMap<String,Object>> selectImpCol(@Param("tableId")Integer tableId);	
	
	/**
	 * @Title: dropTable
	 * @Description:根据表名删除表
	 * @param tableName
	 * @throws Exception
	 * @return void
	 */
	public void dropTable(@Param("tableName")String tableName);
	
	/**
	 * @Title: createTable
	 * @Description: 创建表
	 */
	public void createTable(@Param("createSql")String createSql);
	
	/**
	 * @Title:删除索引
	 * 
	 * @param tableName
	 * @return
	 */
	public void deleteIndex(@Param("tableName")String tableName);
	
	/**
	 * @Title: createOrUpdateIndex
	 * @Description: 创建索引
	 * @return void
	 */
	public void createOrUpdateIndex(@Param("indexSql")String indexSql);
	
	/**
	 * @Title: createComment
	 * @Description: 创建注释
	 */
	public void createComment(@Param("commentSql")String commentSql);	
	
	/**
	 * @Title: alterTableDao
	 * @Description: 更改表结构
	 */
	public void alterTable(@Param("alterSql")String alterSql);

	public List<Map<String,Object>> getTableInfoByType(String type);
	
	/**
	 * 查询实验室表名编号
	 * @return
	 */
	int selectLabTableNum();

	
	/**
     * 按条件查询表定义表
     * @param record
     * @return
     */
    List<McTable> getTableList(McTable record);

}