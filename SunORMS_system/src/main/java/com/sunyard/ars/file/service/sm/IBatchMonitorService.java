package com.sunyard.ars.file.service.sm;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IBatchMonitorService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
