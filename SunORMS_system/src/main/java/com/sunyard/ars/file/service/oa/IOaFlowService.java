package com.sunyard.ars.file.service.oa;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IOaFlowService {
    ResponseBean execute(RequestBean requestBean) throws Exception;
}
