package com.sunyard.ars.file.service.ma;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IMaFlowService {
    ResponseBean execute(RequestBean requestBean) throws Exception;
}
