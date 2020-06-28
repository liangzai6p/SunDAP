package com.sunyard.aos.common.comm;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IBaseService {
    ResponseBean execute(RequestBean requestBean) throws Exception;
}
