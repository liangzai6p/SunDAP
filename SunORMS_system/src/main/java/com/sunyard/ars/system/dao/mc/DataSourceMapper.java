package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.DataSource;

public interface DataSourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DataSource record);

    int insertSelective(DataSource record);

    DataSource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataSource record);

    int updateByPrimaryKey(DataSource record);
    
    
	/**
	 * @Title: getByTableNameDao 
	 * @Description: 获取数据源
	 */
	public DataSource selectByName(@Param("name")String name);
	
	
	/**
	 * 更具条件查询数据源
	 * @param dataSource
	 * @return
	 */
	public List<DataSource> selectBySelective(DataSource dataSource);
	
	
	
    
}