package com.sunyard.ars.system.service.brss;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@FunctionalInterface
public interface IResuperviseService {

	ResponseBean execute(RequestBean requestBean) throws Exception;
}
