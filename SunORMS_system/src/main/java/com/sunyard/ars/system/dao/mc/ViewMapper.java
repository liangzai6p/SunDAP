package com.sunyard.ars.system.dao.mc;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.View;

public interface ViewMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(View record);

    int insertSelective(View record);

    View selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(View record);

    int updateByPrimaryKey(View record);
    
	/**
	 * @Title: getByViewNameDao 
	 * @Description: 获取视图信息
	 * @param viewName
	 */
	public View getByViewNameDao(@Param("viewName")String viewName);
	
	public List<View> selectViewBySelective(View record);
}