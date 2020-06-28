package com.sunyard.ars.system.service.busm;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author:		 wwx
 * @date:		 2017年12月19日 下午5:13:55
 * @description: TODO(IRoleService)
 */
public interface IRoleService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
}
