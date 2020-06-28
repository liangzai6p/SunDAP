package com.sunyard.ars.system.service.sc;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author:		zgz
 * @date:		2018年05月25日
 * @Description:凭证类型定义
 */
public interface IVoucherInfoService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
	void add(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void update(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void query(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
}
