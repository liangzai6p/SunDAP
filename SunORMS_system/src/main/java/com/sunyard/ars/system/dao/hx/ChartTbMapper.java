package com.sunyard.ars.system.dao.hx;

import java.util.List;
import java.util.Map;

public interface ChartTbMapper {
	//添加
	void save(Map<String,Object> map);
	//删除
	void  delete(Map<String,Object> map);
	//修改
	void update(Map<String,Object> map);
	//查询
	List<Map<String,Object>>  select(Map<String,Object> map);

	Integer getMaxId();

}
