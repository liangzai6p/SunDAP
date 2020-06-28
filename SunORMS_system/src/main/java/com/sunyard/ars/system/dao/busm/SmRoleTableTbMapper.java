package com.sunyard.ars.system.dao.busm;

import java.util.List;
import java.util.Map;

import com.sunyard.ars.system.bean.busm.SmRoleTableTB;

public interface SmRoleTableTbMapper {
	
	//根据角色查询表的数据！
	public  List<Map<String, Object>> getTablesByRoleNo(String roleNo);
	
	//保存个人拥有表的数据
	public void  save(SmRoleTableTB  model);
	
	//删除个人的表信息！
	public  void  deleteByRoleNo(String roleNo);

}
