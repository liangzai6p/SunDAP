package com.sunyard.ars.system.dao.busm;

import java.util.List;
import java.util.Map;

import com.sunyard.ars.system.bean.busm.SmUserBusiTbBean;

public interface SmUserBusiTbMapper {
	//添加
	void save(SmUserBusiTbBean model);
	//删除
	void  delete(Map<String,Object> map);
	//查询
	List<Map<String,Object>>  select(Map<String,Object> map);
	void deleteBusNoByUserno(String userNo);

}
