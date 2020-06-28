package com.sunyard.ars.system.dao.busm;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.bean.busm.Emp;

public interface EmpDao {
	
	//查  	
	Emp selectByUserNO(String userNo);
	//增加
	int insertSelective(Emp emp);
	//更新其他信息
	int updateSelective(Emp emp);
	//删 
	int delete(String userNo);
	//直接输密码能通过
	 List<HashMap<String, Object>> passwordEmp(@Param("organNo")String organNo,@Param("userNo")String userNo);
	//需要授权才能通过

	
}
