package com.sunyard.ars.system.service.notice;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author:		 wwx
 * @date:		 2017年12月18日 下午2:42:35
 * @description: TODO(INoticeService)
 */
public interface INoticeService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
}
