package com.sunyard.ars.system.service.mc;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * @date   2018年6月19日
 * @Description 表字段配置
 */
public interface IMcTableFieldService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
