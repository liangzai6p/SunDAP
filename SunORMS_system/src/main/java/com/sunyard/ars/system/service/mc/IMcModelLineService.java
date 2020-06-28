package com.sunyard.ars.system.service.mc;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * ClassName: IMcModelLineService 
 * @Description: 关联模型配置
 * @author zs
 * @date 2018年10月18日
 */
public interface IMcModelLineService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
}
