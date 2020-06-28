package com.sunyard.ars.system.service.sc;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IExchangeManageService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
