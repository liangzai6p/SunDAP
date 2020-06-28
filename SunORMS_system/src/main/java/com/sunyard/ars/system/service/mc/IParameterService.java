package com.sunyard.ars.system.service.mc;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * @date   2018年6月19日
 * @Description 模型配置
 */
public interface IParameterService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
