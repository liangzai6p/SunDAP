package com.sunyard.ars.system.service.log;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * @date   2018年6月19日
 * @Description 模型配置
 */
public interface IOperLogService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
	void query(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
}
