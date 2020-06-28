package com.sunyard.ars.system.service.mc;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * @date   2019年4月12日
 * @Description 模型实验室版本管理
 */
public interface IVersionManageService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
