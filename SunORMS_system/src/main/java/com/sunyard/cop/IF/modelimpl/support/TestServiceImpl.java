package com.sunyard.cop.IF.modelimpl.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.model.support.ITestService;

@Service("testService")
@SuppressWarnings({ "rawtypes", "unchecked" })
@Transactional
public class TestServiceImpl implements ITestService {

	public TestServiceImpl() {

	}

	@Override
	public ResponseBean config(RequestBean req) throws Exception {
		ResponseBean retBean = new ResponseBean();
		Map queryMap = new HashMap();
		Map requestMaps = req.getSysMap();
		String oper_type = String.valueOf(requestMaps.get("oper_type"));
		// 查询全部组件
		if ("handle".equalsIgnoreCase(oper_type)) {
			queryMap.put("rs", "back success");
		}
		retBean.setRetCode(SunIFErrorMessage.SUCCESS);
		retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		retBean.setRetMap(queryMap);
		return retBean;
	}

}
