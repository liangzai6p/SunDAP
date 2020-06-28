package com.sunyard.ars.file.service.scan;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * @Description 影像采集service接口
 * @date 2018年5月24日14:52:56
 *
 */
public interface IVoucherChkService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
