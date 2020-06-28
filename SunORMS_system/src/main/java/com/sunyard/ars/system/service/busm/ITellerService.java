package com.sunyard.ars.system.service.busm;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author:		zgz
 * @date:		2018年05月24日
 * @Description:柜员信息service
 */
public interface ITellerService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
	void add(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void update(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void query(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
}
