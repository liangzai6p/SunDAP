package com.sunyard.ars.system.dao.hx;

import java.util.List;
import java.util.Map;

import com.sunyard.ars.system.bean.hx.NodeTbBean;

public interface INodeTBMapper {
	//添加
	void save(Map<String,Object> map);
	//删除
	void  delete(Map<String,Object> map);
	//修改
	void update(Map<String,Object> map);
	//查询
	List<Map<String,Object>>  select(Map<String,Object> map);

	Integer getMaxId();
	
	//查询节点的节点号+流程号是否已经存在
	List<Map<String,Object>>  selectIsExist(NodeTbBean model);
}

