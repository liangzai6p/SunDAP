package com.sunyard.cop.IF.modelimpl.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.support.ISysParamService;
import com.sunyard.cop.IF.mybatis.dao.SysParameterMapper;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * 参数设置实现类
 * 
 * @author
 */
@Service("sysParamService")
@Transactional
public class SysParamServiceImpl implements ISysParamService {

	private Logger logger = LoggerFactory.getLogger(SysParamServiceImpl.class);
	
	private ResponseBean responseBean = new ResponseBean();

	@Resource
	private SysParameterMapper sysParamMapper;

	/**
	 * 
	 */
	public SysParamServiceImpl() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseBean sysParamConfig(RequestBean req) throws Exception {
		User user = BaseUtil.getLoginUser();
		Map sysMap = req.getSysMap();
		String operType = (String) sysMap.get("oper_type");
		if ("query".equalsIgnoreCase(operType)) { // 查询系统参数
			Map queryMap = new HashMap();
			SysParameter selectParam = new SysParameter();
			selectParam.setIsModify("1");
			selectParam.setBankNo(user.getBankNo());
			selectParam.setProjectNo(user.getProjectNo());
			selectParam.setSystemNo(user.getSystemNo());
			ArrayList array = selectSysParam(selectParam);
			if (array != null) {
				responseBean.setRetCode("IF0000");
				responseBean.setRetMsg("参数信息查询成功!");
			} else {
				responseBean.setRetCode("IF0001");
				responseBean.setRetMsg("参数信息查询失败");
			}
			queryMap.put("list", array);
			responseBean.setRetMap(queryMap);
		} else if ("updateExternal".equalsIgnoreCase(operType)) {
			String userNo = (user.getUserNo() == null) ? "default" : user.getUserNo();
			ArrayList reqList = (ArrayList) req.getParameterList();
			SysParameter updateParam = (SysParameter) reqList.get(0);
			updateParam.setBankNo(user.getBankNo());
			updateParam.setProjectNo(user.getProjectNo());
			updateParam.setSystemNo(user.getSystemNo());
			updateParam.setModifyUser(userNo);
			updateParam.setLastModiDate(SunIFCommonUtil.getFomateTimeString("yyyyMMddHHmmss"));
			updateExternalParam(updateParam);
		} else { // 更新系统参数
			Object jsonData = sysMap.get("jsonData");
			JSONArray a = JSONArray.fromObject(jsonData);
			updateSysParam(a);
			logger.info("系统参数更新成功");
		}
		return responseBean;
	}

	@SuppressWarnings("rawtypes")
	private ArrayList<Map> selectSysParam(SysParameter sysParam) {
		return sysParamMapper.selectSysParam(sysParam);
	}

	private void updateExternalParam(SysParameter sysParam) {
		sysParamMapper.updateSysParam(sysParam);
		SunIFConstant.PARAM_MAP.put(sysParam.getParamItem(), sysParam.getParamValue());
		responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	private void updateSysParam(JSONArray arr) {
		String date = SunIFCommonUtil.getFomateTimeString("yyyyMMddHHmmss");
		User user = BaseUtil.getLoginUser();
		String userNo = (user.getUserNo() == null) ? "default" : user.getUserNo();
		for (int i = 0, n = arr.size(); i < n; i++) {
			JSONObject j = JSONObject.fromObject(arr.get(i));
			if ("".equals(j.get("value"))) {
				continue;
			}
			String key = String.valueOf(j.get("name")).trim();
			String value = String.valueOf(j.get("value")).trim();
			SysParameter updateParam = new SysParameter();
			updateParam.setParamItem(key);
			updateParam.setParamValue(value);
			updateParam.setModifyUser(userNo);
			updateParam.setLastModiDate(date);
			updateParam.setBankNo(user.getBankNo());
			updateParam.setProjectNo(user.getProjectNo());
			updateParam.setSystemNo(user.getSystemNo());
			sysParamMapper.updateSysParam(updateParam);
			SunIFConstant.PARAM_MAP.put(key, value);
		}
		responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}
}
