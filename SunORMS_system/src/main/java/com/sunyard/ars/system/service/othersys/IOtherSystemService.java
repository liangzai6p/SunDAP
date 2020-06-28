package com.sunyard.ars.system.service.othersys;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 外系统交易接口
 * @author zgz
 *
 */
public interface IOtherSystemService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
	/**
	 * 获取交易码，每个实现类对应一个，用于区分功能。
	 * @return
	 */
	String getTranCode();
}
