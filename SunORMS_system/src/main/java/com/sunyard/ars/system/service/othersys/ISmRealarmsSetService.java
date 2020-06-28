package com.sunyard.ars.system.service.othersys;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface ISmRealarmsSetService {

	ResponseBean execute(RequestBean requestBean) throws Exception;
	
	void add(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void update(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void query(RequestBean requestBean, ResponseBean responseBean) throws Exception;
}
