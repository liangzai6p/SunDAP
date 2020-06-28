package com.sunyard.cop.IF.modelimpl.field;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.field.IFieldParamService;
import com.sunyard.cop.IF.modelimpl.support.LoginServiceImpl;
import com.sunyard.cop.IF.mybatis.dao.IFFieldMapper;
import com.sunyard.cop.IF.mybatis.pojo.IFField;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("fieldParamService")
@Transactional
public class FieldParamImpl implements IFieldParamService {

	@Resource
	private IFFieldMapper IFFieldDao;

	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Override
	public ResponseBean fildParam(RequestBean req) throws Exception {
		ResponseBean retBean = new ResponseBean();
		Map sysMap = req.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");

		if ("asysc".equalsIgnoreCase(oper_type)) {// 异步加载数据字典
			ArrayList requestList = (ArrayList) req.getParameterList();
			IFField fieldBean = (IFField) requestList.get(0);
			String parentField = fieldBean.getParentField();
			Map asayMap = new HashMap();
			ArrayList list = this.asyscSelect(parentField);
			asayMap.put("list", list);
			retBean.setRetCode("IF0000");
			retBean.setRetMap(asayMap);
			retBean.setRetMsg("初始化成功");
		} else if (SunIFConstant.INSERT.equalsIgnoreCase(oper_type)) {// 新增数据字典
			ArrayList requestList = (ArrayList) req.getParameterList();
			IFField fieldBean = (IFField) requestList.get(0);
			String parentField = fieldBean.getParentField();
			String field_id = fieldBean.getFieldId();
			Map map = new HashMap();
			if ("000000".equalsIgnoreCase(parentField)) {
				field_id = field_id + "0000";
			} else {
				field_id = getFieldIdValue(parentField);
			}
			fieldBean.setFieldId(field_id);
			this.insertField(fieldBean, retBean);

		} else if (SunIFConstant.DELETE.equalsIgnoreCase(oper_type)) {// 删除数据字典
			ArrayList requestList = (ArrayList) req.getParameterList();
			IFField fieldBean = (IFField) requestList.get(0);
			this.deleteField(fieldBean, retBean);
		} else if (SunIFConstant.UPDATE.equalsIgnoreCase(oper_type)) {
			ArrayList requestList = (ArrayList) req.getParameterList();
			IFField fieldBean = (IFField) requestList.get(0);
			this.updateField(fieldBean, retBean);
		}

		return retBean;
	}

	/**
	 * 异步加载方法
	 * 
	 * @param parentField
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList asyscSelect(String parentField) {
		User user = BaseUtil.getLoginUser();
		IFField field = new IFField();
		field.setBankNo(user.getBankNo());
		field.setSystemNo(user.getSystemNo());
		field.setProjectNo(user.getProjectNo());
		field.setParentField(parentField);
		return IFFieldDao.asyscSelect(field);
	}

	/**
	 * 后台自动生成字典号
	 * 
	 * @param parentField
	 * @return
	 * @throws Exception
	 */
	private String getFieldIdValue(String parentField) throws Exception {
		// 表明当前新增的数据字典需要后台根据对应的父级字典项自动生成 字典号，计算方式如下：
		// （1）父级字典项包含子数据字典，则根据所有子数据字典中字典号最大值加1；
		// （2）父级字典项无子数据字典，则直接取父级字典的字典号值加1；
		String temp = "";
		String field_id = "";
		IFField fieldBean = new IFField();
		fieldBean.setParentField(parentField);
		User user = BaseUtil.getLoginUser();
		fieldBean.setBankNo(user.getBankNo());
		fieldBean.setProjectNo(user.getProjectNo());
		fieldBean.setSystemNo(user.getSystemNo());
		int count = IFFieldDao.countParent(fieldBean);
		if (count <= 0) {
			temp = parentField;
		} else {
			temp = "";
			temp = IFFieldDao.maxField(fieldBean);
			temp = BaseUtil.filterSqlParam(temp);
		}
		while (true) {
			//代码扫描，添加非空约束
			Objects.requireNonNull(temp);
			field_id = addone(temp.trim());
			fieldBean.setFieldId(field_id);
			// 判断field_id对应的字典号是否已经存在，如果存在，则重新生成字典号
			int flag = 0;
			flag = IFFieldDao.countField(fieldBean);
			if (flag <= 0) {
				break;
			}
			temp = field_id;

		}
		return field_id;
	}

	/**
	 * 构造字典号
	 */
	private String addone(String field_id) {
		if (field_id == null || field_id.length() != 6) {// 字典号[field_id]的标准长度为6位
			return field_id;
		}
		String retStr = "";
		try {
			String str = String.valueOf(Integer.valueOf(field_id.substring(2)) + 1); // 截取field_id的后四位加1
			// 补齐str为四位
			str = str.length() == 1 ? ("000" + str) : str;
			str = str.length() == 2 ? ("00" + str) : str;
			str = str.length() == 3 ? ("0" + str) : str;
			str = str.length() > 4 ? (str.substring(0, 4)) : str;
			retStr = field_id.substring(0, 2) + str;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return field_id;
		}
		return retStr;
	}

	/**
	 * 新增数据字典，插入一条记录，并修改其父节点（isParent为"1"）
	 * 
	 * @param fieldBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertField(IFField fieldBean, ResponseBean retBean) {
		User user = BaseUtil.getLoginUser();
		String parentField = fieldBean.getParentField();
		String field_id = fieldBean.getFieldId();
		String last_modi_date = SunIFCommonUtil.getFomateTimeString("yyyyMMddHHmmss");
		fieldBean.setBankNo(user.getBankNo());
		fieldBean.setSystemNo(user.getSystemNo());
		fieldBean.setProjectNo(user.getProjectNo());
		fieldBean.setLastModiDate(last_modi_date);
		fieldBean.setIsParent("0");
		IFFieldDao.insertField(fieldBean);
		fieldBean.setIsParent("1");
		fieldBean.setFieldId(parentField);
		fieldBean.setLastModiDate(last_modi_date);
		IFFieldDao.updateParent(fieldBean);
		retBean.setRetMsg("添加成功");
		retBean.setRetCode("IF0000");
		Map maps = new HashMap();
		maps.put("field_id", field_id);
		maps.put("last_modi_date", last_modi_date);
		retBean.setRetMap(maps);
	}

	/**
	 * 删除数据字典
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deleteField(IFField fieldBean, ResponseBean retBean) {
		User user = BaseUtil.getLoginUser();
		fieldBean.setBankNo(user.getBankNo());
		fieldBean.setSystemNo(user.getSystemNo());
		fieldBean.setProjectNo(user.getProjectNo());
		Map maps = new HashMap();
		String parent_field = fieldBean.getParentField();
		String field_id = fieldBean.getFieldId();
		String last_modi_date = SunIFCommonUtil.getFomateTimeString("yyyyMMddHHmmss");
		fieldBean.setParentField(field_id);
		int count = IFFieldDao.countParent(fieldBean);
		if (count > 0) {
			retBean.setRetCode("IF0001");
			retBean.setRetMsg("hasChildren");
		} else {
			fieldBean.setParentField(parent_field);
			IFFieldDao.deleteField(fieldBean);
			int flag = IFFieldDao.countParent(fieldBean);
			fieldBean.setFieldId(parent_field);
			fieldBean.setLastModiDate(last_modi_date);
			if (flag <= 0) {
				fieldBean.setIsParent("0");
				IFFieldDao.updateParent(fieldBean);
			} else {
				fieldBean.setIsParent("1");
				IFFieldDao.updateParent(fieldBean);
			}
			maps.put("last_modi_date", last_modi_date);
			retBean.setRetMap(maps);
			retBean.setRetCode("IF0000");
			retBean.setRetMsg("删除成功");
		}
	}

	/**
	 * 更新数据字典
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateField(IFField fieldBean, ResponseBean retBean) {
		User user = BaseUtil.getLoginUser();
		fieldBean.setBankNo(user.getBankNo());
		fieldBean.setSystemNo(user.getSystemNo());
		fieldBean.setProjectNo(user.getProjectNo());
		Map maps = new HashMap();
		String last_modi_date = SunIFCommonUtil.getFomateTimeString("yyyyMMddHHmmss");
		String parent_id = fieldBean.getParentField();
		fieldBean.setLastModiDate(last_modi_date);
		IFFieldDao.updateField(fieldBean);
		fieldBean.setFieldId(parent_id);
		IFFieldDao.updateParentTime(fieldBean);
		maps.put("last_modi_date", last_modi_date);
		retBean.setRetMap(maps);
		retBean.setRetCode("IF0000");
		retBean.setRetMsg("修改成功");
	}
}
