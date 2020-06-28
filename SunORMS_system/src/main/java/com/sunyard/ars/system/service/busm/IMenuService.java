package com.sunyard.ars.system.service.busm;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

public interface IMenuService {
    ResponseBean execute(RequestBean requestBean) throws Exception;
}
