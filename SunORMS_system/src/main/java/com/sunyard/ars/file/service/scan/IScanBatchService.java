package com.sunyard.ars.file.service.scan;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IScanBatchService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
