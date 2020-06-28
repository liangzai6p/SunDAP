package com.sunyard.ars.system.dao.mc;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.QueryImg;

public interface QueryImgMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(QueryImg record);

    int insertSelective(QueryImg record);

    QueryImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QueryImg record);

    int updateByPrimaryKey(QueryImg record);
    	
	List<QueryImg> selectBySelective(QueryImg record);
	
	/**
	 * 获取分组数量
	 * @param modelId
	 * @return
	 */
	public int getQueryImgCount(@Param("modelId")Integer modelId);
}