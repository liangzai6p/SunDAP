package com.sunyard.ars.system.service.et;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author:		zgz
 * @date:		2018年05月25日
 * @Description:差错单处理
 */
public interface INodeHandleService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
