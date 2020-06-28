package com.sunyard.ars.system.dao.sc;

import java.util.List;
import java.util.Map;

public interface IAccnoLifeCircleMapper {
	//查询
	List<Map<String,Object>>  select(Map<String,Object> map);
	
	//查询证件信息
	List<Map<String,Object>> selectCardInfo(String str);
	
	//人员信息
	List<Map<String,Object>> selectPsersonInfo(String str);
	
	//签约信息
	List<Map<String,Object>> selectSignInfo(String str);
	
	//账户冻结信息  
	List<Map<String,Object>> selectAcctNoDJInfo(String str);
	
	//强行划扣信息
	List<Map<String,Object>> selectForceHQInfo(String str);

	List selectSeeData(Map<String, Object> map);
}
