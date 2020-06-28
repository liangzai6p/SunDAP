package com.sunyard.ars.system.service.mc;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * @date   2018年6月15日
 * @Description 数据源配置
 */
public interface IMcDataSourceService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
