package com.sunyard.ars.file.service.file;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * @date 2018年5月24日14:52:56
 *
 */
public interface IFileVoucherChkService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
	void add(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void update(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void query(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception;
}
