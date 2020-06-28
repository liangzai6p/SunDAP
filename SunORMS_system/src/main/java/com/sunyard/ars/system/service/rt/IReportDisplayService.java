package com.sunyard.ars.system.service.rt;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author:		zgz
 * @date:		2018年05月25日
 * @Description:报表展现
 */
public interface IReportDisplayService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
	void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
}
