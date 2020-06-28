package com.sunyard.ars.system.service.sc;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IBlackListService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
