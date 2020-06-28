package com.sunyard.ars.system.service.busm;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author:		zheng.jw
 * @date:		2017年12月15日 上午10:15:37
 * @Description:机构信息service
 */
public interface IOrganInfoService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
}
