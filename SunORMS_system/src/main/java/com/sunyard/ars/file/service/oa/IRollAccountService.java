package com.sunyard.ars.file.service.oa;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IRollAccountService {
    ResponseBean execute(RequestBean requestBean) throws Exception;
}
