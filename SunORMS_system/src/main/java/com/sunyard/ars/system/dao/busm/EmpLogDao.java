package com.sunyard.ars.system.dao.busm;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import com.sunyard.ars.system.bean.busm.EmpLog;

public interface EmpLogDao {
	int add(EmpLog empLog);
	ArrayList<EmpLog> query(Map condition);
	List<String> getempMethod(Map<String, Object> map);
	List<String> getempMoudle();
}
