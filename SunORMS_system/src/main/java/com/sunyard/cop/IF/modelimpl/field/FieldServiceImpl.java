package com.sunyard.cop.IF.modelimpl.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.model.field.IFieldService;
import com.sunyard.cop.IF.mybatis.dao.IFFieldMapper;
import com.sunyard.cop.IF.mybatis.pojo.IFField;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * 查询数据字典实现类
 * 
 * @author YZ 2017年3月20日 下午1:43:36
 */
@Service("fieldService")
@Transactional
public class FieldServiceImpl implements IFieldService {

	private Logger logger = LoggerFactory.getLogger(FieldServiceImpl.class);

	@Resource
	private IFFieldMapper ifFieldMapper;

	/**
	 * 
	 */
	public FieldServiceImpl() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseBean getFields(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();

		Map sysMap = requestBean.getSysMap();
		// String oper_type = (String) sysMap.get("oper_type");
		String fieldTime = (String) sysMap.get("fieldTime");

		IFField field = new IFField();
		User user = BaseUtil.getLoginUser();
		field.setBankNo(user.getBankNo());
		field.setProjectNo(user.getProjectNo());
		field.setSystemNo(user.getSystemNo());

		ArrayList list = null;
		if (!BaseUtil.isBlank(fieldTime)) {
			field.setLastModiDate(fieldTime);
			list = ifFieldMapper.getFieldsByModifyTime(field);
			logger.info("查询 " + fieldTime + " 之后更新的数据字典");
		} else {
			field.setLastModiDate("");
			list = ifFieldMapper.getAllField(field);
			logger.info("查询所有数据字典 ");
		}
		HashMap retMap = new HashMap();
		for (Object oMap : list) {
			Map map = BaseUtil.convertMapKeyValue((Map) oMap);
			String field_value = (String) map.get("field_value");
			String parent_field = (String) map.get("parent_field");
			if (retMap.get(parent_field) == null) {
				retMap.put(parent_field, field_value);
			} else {
				retMap.put(parent_field, retMap.get(parent_field) + "&" + field_value);
			}
			String last_modi_date = (String) map.get("last_modi_date");
			if (BaseUtil.isBlank(fieldTime) || last_modi_date.compareTo(fieldTime) > 0) {
				fieldTime = last_modi_date;
			}
		}
		retMap.put("fieldTime", fieldTime);
		responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		responseBean.setRetMap(retMap);

		return responseBean;
	}
}
