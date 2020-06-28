package com.sunyard.ars.system.dao.busm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ArmsUserTaltTbMapper {
	//添加
	void save(Map<String,Object> map);
	//删除
	void  delete(Map<String,Object> map);
	//修改
	void update(Map<String,Object> map);
	//查询
	List<Map<String,Object>>  select(Map<String,Object> map);
	
	Integer getMaxId();
	
	/**
	 * 获取当前传入的userNo数据数量
	 * @param userNo
	 * @return
	 * @Date 2018/09/25
	 */
	Integer selectCountByUserNo(String userNo);
	
	List<Map<String,Object>>  selectGroup(@Param("userNO")String userNO,@Param("status")String status);
	Integer queryUserInfoFromPri(String userNo);
}
